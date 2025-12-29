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
            throw new IllegalArgumentException("log");
        }

        if (log.getAccessTime().isAfter(Instant.now())) {
            throw new IllegalArgumentException("future");
        }

        if (log.getDigitalKey() == null || log.getDigitalKey().getId() == null) {
            throw new IllegalArgumentException("key");
        }

        if (log.getGuest() == null || log.getGuest().getId() == null) {
            throw new IllegalArgumentException("guest");
        }

        DigitalKey key = keyRepo.findById(log.getDigitalKey().getId())
                .orElseThrow(() -> new ResourceNotFoundException("key"));

        Guest guest = guestRepo.findById(log.getGuest().getId())
                .orElseThrow(() -> new ResourceNotFoundException("guest"));

        log.setDigitalKey(key);
        log.setGuest(guest);

        if (!Boolean.TRUE.equals(key.getActive())) {
            return deny(log, "inactive");
        }

        if (key.getExpiresAt() != null && key.getExpiresAt().isBefore(Instant.now())) {
            return deny(log, "expired");
        }

        if (!Boolean.TRUE.equals(guest.getActive())) {
            return deny(log, "inactive");
        }

        boolean isOwner =
                key.getBooking() != null &&
                key.getBooking().getGuest() != null &&
                guest.getId().equals(key.getBooking().getGuest().getId());

        boolean shared = false;

        if (!isOwner) {
            List<KeyShareRequest> shares = shareRepo.findBySharedWithId(guest.getId());
            for (KeyShareRequest s : shares) {
                if ("APPROVED".equals(s.getStatus()) &&
                    s.getDigitalKey() != null &&
                    s.getDigitalKey().getId().equals(key.getId()) &&
                    !log.getAccessTime().isBefore(s.getShareStart()) &&
                    !log.getAccessTime().isAfter(s.getShareEnd())) {
                    shared = true;
                    break;
                }
            }
        }

        if (isOwner || shared) {
            log.setResult("SUCCESS");
            log.setReason("granted");
        } else {
            log.setResult("DENIED");
            log.setReason("unauthorized");
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
        if (keyId == null) return Collections.emptyList();
        return repo.findByDigitalKeyId(keyId);
    }

    @Override
    public List<AccessLog> getLogsForGuest(Long guestId) {
        if (guestId == null) return Collections.emptyList();
        return repo.findByGuestId(guestId);
    }
}
