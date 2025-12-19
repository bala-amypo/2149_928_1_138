package com.example.demo.service;

import com.example.demo.model.DigitalKey;
import java.util.List;

public interface DigitalKeyService {

    DigitalKey generateKey(Long bookingId);

    List<DigitalKey> getKeysForGuest(Long guestId);
}
