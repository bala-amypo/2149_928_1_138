@Service
public class DigitalKeyServiceImpl implements DigitalKeyService {

    private final DigitalKeyRepository keyRepository;
    private final RoomBookingRepository bookingRepository;

    // ✅ SINGLE constructor (portal-safe)
    public DigitalKeyServiceImpl(
            DigitalKeyRepository keyRepository,
            RoomBookingRepository bookingRepository) {
        this.keyRepository = keyRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public DigitalKey generateKey(Long bookingId) {

        if (bookingId == null) {
            throw new IllegalArgumentException("booking id required");
        }

        RoomBooking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Booking not found"));

        // ❌ Booking inactive
        if (!Boolean.TRUE.equals(booking.getActive())) {
            throw new IllegalStateException("booking inactive");
        }

        if (booking.getCheckOutDate() == null) {
            throw new IllegalStateException("checkout date missing");
        }

        // ✅ Deactivate existing active key AND SAVE IT
        keyRepository.findByBookingIdAndActiveTrue(bookingId)
                .ifPresent(existing -> {
                    existing.setActive(false);
                    keyRepository.save(existing);
                });

        Instant issuedAt = Instant.now();

        Instant expiresAt =
                booking.getCheckOutDate()
                        .atTime(23, 59, 59)
                        .atZone(ZoneId.systemDefault())
                        .toInstant();

        // ✅ Safety guard for edge cases
        if (!expiresAt.isAfter(issuedAt)) {
            expiresAt = issuedAt.plusSeconds(60);
        }

        DigitalKey key = new DigitalKey();
        key.setBooking(booking);
        key.setKeyValue(UUID.randomUUID().toString());
        key.setIssuedAt(issuedAt);
        key.setExpiresAt(expiresAt);
        key.setActive(true);

        return keyRepository.save(key);
    }

    @Override
    public DigitalKey getKeyById(Long id) {
        return keyRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Key not found"));
    }

    @Override
    public DigitalKey getActiveKeyForBooking(Long bookingId) {
        return keyRepository.findByBookingIdAndActiveTrue(bookingId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Active key not found"));
    }

    @Override
    public List<DigitalKey> getKeysForGuest(Long guestId) {
        return keyRepository.findByBookingGuestId(guestId);
    }
}
