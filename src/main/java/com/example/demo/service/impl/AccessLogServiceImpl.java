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

        Instant now = Instant.now();

        if (log.getAccessTime().isAfter(now)) {
            throw new IllegalArgumentException("future");
        }

        if (log.getDigitalKey() == null || log.getDigitalKey().getId() == null) {
            throw new IllegalArgumentException("key");
        }

        if (log.getGuest() == null || log.getGuest().getId() == null) {
            throw new IllegalArgumentException("guest");
        }

        DigitalKey key = keyRepo.findById(log.getDigitalKey().getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("not found"));

        Guest guest = guestRepo.findById(log.getGuest().getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("not found"));

        log.setDigitalKey(key);
        log.setGuest(guest);

        if (!Boolean.TRUE.equals(key.getActive())) {
            return deny(log, "inactive");
        }

        if (key.getExpiresAt() != null && key.getExpiresAt().isBefore(now)) {
            return deny(log, "expired");
        }

        if (!Boolean.TRUE.equals(guest.getActive())) {
            return deny(log, "inactive");
        }

        boolean isOwner =
                key.getBooking() != null &&
                key.getBooking().getGuest() != null &&
                guest.getId().equals(key.getBooking().getGuest().getId());

        boolean hasShare = false;

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

                boolean inWindow =
                        !log.getAccessTime().isBefore(share.getShareStart()) &&
                        !log.getAccessTime().isAfter(share.getShareEnd());

                if (sameKey && approved && inWindow) {
                    hasShare = true;
                    break;
                }
            }
        }

        if (isOwner || hasShare) {
            log.setResult("SUCCESS");
            log.setReason("access granted");
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
