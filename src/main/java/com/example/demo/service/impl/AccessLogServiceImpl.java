package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.AccessLog;
import com.example.demo.model.DigitalKey;
import com.example.demo.model.Guest;
import com.example.demo.model.KeyShareRequest;
import com.example.demo.repository.AccessLogRepository;
import com.example.demo.repository.DigitalKeyRepository;
import com.example.demo.repository.GuestRepository;
import com.example.demo.repository.KeyShareRequestRepository;
import com.example.demo.service.AccessLogService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

@Service
public class AccessLogServiceImpl implements AccessLogService {

    private final AccessLogRepository repo;
    private final DigitalKeyRepository keyRepo;
    private final GuestRepository guestRepo;
    private final KeyShareRequestRepository shareRepo;

    // ✅ SINGLE PUBLIC CONSTRUCTOR (IMPORTANT)
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

        if (log == null || log.getAccessTime() == null) {
            throw new IllegalArgumentException();
        }

        Instant now = Instant.now();

        if (log.getAccessTime().isAfter(now)) {
            throw new IllegalArgumentException();
        }

        if (log.getDigitalKey() == null || log.getDigitalKey().getId() == null) {
            throw new IllegalArgumentException();
        }

        if (log.getGuest() == null || log.getGuest().getId() == null) {
            throw new IllegalArgumentException();
        }

        DigitalKey key = keyRepo.findById(log.getDigitalKey().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Key not found"));

        Guest guest = guestRepo.findById(log.getGuest().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Guest not found"));

        log.setDigitalKey(key);
        log.setGuest(guest);

        // ❌ INACTIVE KEY
        if (!Boolean.TRUE.equals(key.getActive())) {
            return deny(log, "key inactive");
        }

        // ❌ EXPIRED KEY
        if (key.getExpiresAt() != null && key.getExpiresAt().isBefore(now)) {
            return deny(log, "key expired");
        }

        // ❌ INACTIVE GUEST
        if (!Boolean.TRUE.equals(guest.getActive())) {
            return deny(log, "guest inactive");
        }

        boolean isOwner =
                key.getBooking() != null &&
                key.getBooking().getGuest() != null &&
                key.getBooking().getGuest().getId() != null &&
                key.getBooking().getGuest().getId().equals(guest.getId());

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

        if (isOwner || hasValidShare) {
            log.setResult("SUCCESS");
            log.setReason("access granted");
        } else {
            log.setResult("DENIED");
            log.setReason("unauthorized access");
        }

        return repo.save(log);
    }

    private AccessLog deny(AccessLog log, String reason) {
        log.setResult("DENIED");
        log.setReason(reason);
        return repo.save(log);
    }

    @Override
    public List<AccessLog> getLogsForKey(Long keyId) {
        if (keyId == null) {
            return Collections.emptyList();
        }
        return repo.findByDigitalKeyId(keyId);
    }

    @Override
    public List<AccessLog> getLogsForGuest(Long guestId) {
        if (guestId == null) {
            return Collections.emptyList();
        }
        return repo.findByGuestId(guestId);
    }
}
