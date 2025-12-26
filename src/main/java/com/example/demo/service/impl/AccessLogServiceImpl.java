@Service
public class AccessLogServiceImpl implements AccessLogService {

    private final AccessLogRepository repo;
    private final DigitalKeyRepository keyRepo;
    private final GuestRepository guestRepo;
    private final KeyShareRequestRepository shareRepo;

    public AccessLogServiceImpl(
            AccessLogRepository repo,
            DigitalKeyRepository keyRepo,
            GuestRepository guestRepo,
            KeyShareRequestRepository shareRepo) {
        this.repo = repo;
        this.keyRepo = keyRepo;
        this.guestRepo = guestRepo;
        this.shareRepo = shareRepo;
    }

    @Override
    public AccessLog createLog(AccessLog log) {

        if (log == null) {
            throw new IllegalArgumentException("access log required");
        }

        if (log.getAccessTime() == null) {
            throw new IllegalArgumentException("access time required");
        }

        Instant now = Instant.now();

        // ❌ Future access
        if (log.getAccessTime().isAfter(now)) {
            throw new IllegalArgumentException("future access not allowed");
        }

        if (log.getDigitalKey() == null || log.getDigitalKey().getId() == null) {
            throw new IllegalArgumentException("digital key required");
        }

        if (log.getGuest() == null || log.getGuest().getId() == null) {
            throw new IllegalArgumentException("guest required");
        }

        // ✅ Fetch key
        DigitalKey key = keyRepo.findById(log.getDigitalKey().getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Key not found"));

        // ❌ Key inactive
        if (!Boolean.TRUE.equals(key.getActive())) {
            return deny(log, key, "key inactive");
        }

        // ❌ Key expired
        if (key.getExpiresAt() != null && key.getExpiresAt().isBefore(now)) {
            return deny(log, key, "key expired");
        }

        // ✅ Fetch guest
        Guest guest = guestRepo.findById(log.getGuest().getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Guest not found"));

        // ❌ Guest inactive
        if (!Boolean.TRUE.equals(guest.getActive())) {
            return deny(log, key, guest, "guest inactive");
        }

        // =============================
        // 🔐 ACCESS RULES
        // =============================

        boolean isOwner = false;

        if (key.getBooking() != null &&
            key.getBooking().getGuest() != null &&
            key.getBooking().getGuest().getId() != null) {

            isOwner = key.getBooking()
                         .getGuest()
                         .getId()
                         .equals(guest.getId());
        }

        boolean hasValidShare = false;

        if (!isOwner) {
            List<KeyShareRequest> shares =
                    shareRepo.findBySharedWithId(guest.getId());

            for (KeyShareRequest share : shares) {

                if (share.getDigitalKey() == null ||
                    share.getDigitalKey().getId() == null ||
                    share.getShareStart() == null ||
                    share.getShareEnd() == null) {
                    continue;
                }

                boolean sameKey =
                        share.getDigitalKey().getId().equals(key.getId());

                boolean approved =
                        "APPROVED".equals(share.getStatus());

                boolean withinWindow =
                        !log.getAccessTime().isBefore(share.getShareStart()) &&
                        !log.getAccessTime().isAfter(share.getShareEnd());

                if (sameKey && approved && withinWindow) {
                    hasValidShare = true;
                    break;
                }
            }
        }

        boolean allowed = isOwner || hasValidShare;

        log.setDigitalKey(key);
        log.setGuest(guest);

        if (allowed) {
            log.setResult("SUCCESS");
            log.setReason("access granted");
        } else {
            log.setResult("DENIED");
            log.setReason("unauthorized access");
        }

        return repo.save(log);
    }

    private AccessLog deny(AccessLog log, DigitalKey key, String reason) {
        log.setDigitalKey(key);
        log.setResult("DENIED");
        log.setReason(reason);
        return repo.save(log);
    }

    private AccessLog deny(AccessLog log, DigitalKey key, Guest guest, String reason) {
        log.setDigitalKey(key);
        log.setGuest(guest);
        log.setResult("DENIED");
        log.setReason(reason);
        return repo.save(log);
    }

    @Override
    public List<AccessLog> getLogsForKey(Long keyId) {
        return repo.findByDigitalKeyId(keyId);
    }

    @Override
    public List<AccessLog> getLogsForGuest(Long guestId) {
        return repo.findByGuestId(guestId);
    }
}
