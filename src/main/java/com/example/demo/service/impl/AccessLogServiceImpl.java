package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.AccessLog;
import com.example.demo.model.DigitalKey;
import com.example.demo.model.Guest;
import com.example.demo.repository.AccessLogRepository;
import com.example.demo.repository.DigitalKeyRepository;
import com.example.demo.repository.GuestRepository;
import com.example.demo.repository.KeyShareRequestRepository;
import com.example.demo.service.AccessLogService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
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
            KeyShareRequestRepository shareRepo
    ) {
        this.repo = repo;
        this.keyRepo = keyRepo;
        this.guestRepo = guestRepo;
        this.shareRepo = shareRepo;
    }

    @Override
    public AccessLog createLog(AccessLog log) {

        // ❌ Block future timestamps
        if (log.getAccessTime().after(new Timestamp(System.currentTimeMillis()))) {
            throw new IllegalArgumentException("future access not allowed");
        }

        // ✅ Fetch full DigitalKey
        DigitalKey key = keyRepo.findById(log.getDigitalKey().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Key not found"));

        // ❌ Key must be active
        if (!Boolean.TRUE.equals(key.getActive())) {
            log.setDigitalKey(key);
            log.setResult("DENIED");
            log.setReason("Key inactive");
            return repo.save(log);
        }

        // ✅ Fetch full Guest
        Guest guest = guestRepo.findById(log.getGuest().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Guest not found"));

        // =============================
        // 🔐 ACCESS VALIDATION
        // =============================

        boolean isOwner =
                key.getBooking() != null &&
                key.getBooking().getGuest() != null &&
                key.getBooking().getGuest().getId().equals(guest.getId());

        boolean isSharedUser =
                shareRepo.existsByDigitalKeyIdAndSharedWithIdAndStatus(
                        key.getId(),
                        guest.getId(),
                        "APPROVED"
                );

        boolean allowed = isOwner || isSharedUser;

        // =============================
        // ✅ ATTACH FULL OBJECT GRAPH
        // =============================
        log.setDigitalKey(key);
        log.setGuest(guest);

        // =============================
        // ✅ RESULT + REASON (NO NULLS)
        // =============================
        if (allowed) {
            log.setResult("SUCCESS");
            log.setReason("Access granted");
        } else {
            log.setResult("DENIED");
            log.setReason("No valid key ownership or approved share");
        }

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
