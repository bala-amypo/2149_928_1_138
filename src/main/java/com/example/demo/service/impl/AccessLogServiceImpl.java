package com.example.demo.service.impl;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.AccessLogService;

import java.sql.Timestamp;
import java.util.List;
import org.springframework.stereotype.Service;
@Service 
public class AccessLogServiceImpl implements AccessLogService {

    private final AccessLogRepository repo;
    private final DigitalKeyRepository keyRepo;
    private final GuestRepository guestRepo;
    private final KeyShareRequestRepository shareRepo;

    public AccessLogServiceImpl(AccessLogRepository repo,
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
        if (log.getAccessTime().after(new Timestamp(System.currentTimeMillis()))) {
            throw new IllegalArgumentException("future access not allowed");
        }

        DigitalKey key = keyRepo.findById(log.getDigitalKey().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Key not found"));
        Guest guest = guestRepo.findById(log.getGuest().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Guest not found"));

        boolean allowed = key.getActive() &&
                (key.getBooking().getGuest().getId().equals(guest.getId()) ||
                        shareRepo.findBySharedWithId(guest.getId()).isEmpty());

        log.setResult(allowed ? "SUCCESS" : "DENIED");
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
