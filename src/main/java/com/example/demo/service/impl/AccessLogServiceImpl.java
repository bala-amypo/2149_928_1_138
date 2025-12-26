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

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

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

        Timestamp now = Timestamp.from(Instant.now());

        // ❌ Future access blocked
        if (log.getAccessTime().after(now)) {
            throw new IllegalArgumentException("future access");
        }

        // ✅ Fetch DigitalKey
        DigitalKey key = keyRepo.findById(log.getDigitalKey().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Key not found"));

        // ❌ Key inactive
        if (!Boolean.TRUE.equals(key.getActive())) {
            return deny(log, key, "key inactive");
        }

        // ❌ Key expired
        if (key.getExpiresAt() != null && now.after(key.getExpiresAt())) {
            return deny(log, key, "key expired");
        }

        // ✅ Fetch Guest
        Guest guest = guestRepo.findById(log.getGuest().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Guest not found"));

        // ❌ Guest inactive
        if (!Boolean.TRUE.equals(guest.getActive())) {
            return deny(log, key, guest, "guest inactive");
        }

        // =============================
        // 🔐 ACCESS RULES
        // =============================

        boolean isOwner =
                key.getBooking() != null &&
                key.getBooking().getGuest() != null &&
                key.getBooking().getGuest().getId().equals(guest.getId());

        boolean hasValidShare = false;

        if (!isOwner) {
            List<KeyShareRequest> shares =
                    shareRepo.findBySharedWithId(guest.getId());

            for (KeyShareRequest share : shares) {
                if (share.getDigitalKey().getId().equals(key.getId()) &&
                    "APPROVED".equals(share.getStatus()) &&
                    !log.getAccessTime().before(share.getShareStart()) &&
                    !log.getAccessTime().after(share.getShareEnd())) {
                    hasValidShare = true;
                    break;
                }
            }
        }

        boolean allowed = isOwner || hasValidShare;

        // =============================
        // ✅ ATTACH FULL OBJECT GRAPH
        // =============================
        log.setDigitalKey(key);
        log.setGuest(guest);

        if (allowed) {
            log.setResult("SUCCESS");
            log.setReason("Access granted");
        } else {
            log.setResult("DENIED");
            log.setReason("unauthorized access");
        }

        return repo.save(log);
    }

    // =============================
    // 🔁 Helper methods (NO NULLS)
    // =============================

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
