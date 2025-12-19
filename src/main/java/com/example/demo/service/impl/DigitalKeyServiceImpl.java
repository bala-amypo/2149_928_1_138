@Service
public class DigitalKeyServiceImpl implements DigitalKeyService {

    private final DigitalKeyRepository repo;
    private final RoomBookingRepository bookingRepo;

    public DigitalKeyServiceImpl(DigitalKeyRepository repo, RoomBookingRepository bookingRepo) {
        this.repo = repo;
        this.bookingRepo = bookingRepo;
    }

    @Override
    public DigitalKey generateKey(Long bookingId) {
        RoomBooking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (!booking.getActive()) {
            throw new IllegalStateException("inactive booking");
        }

        DigitalKey key = new DigitalKey();
        key.setBooking(booking);
        key.setKeyValue(UUID.randomUUID().toString());
        key.setIssuedAt(LocalDateTime.now());
        key.setExpiresAt(LocalDateTime.now().plusDays(1));

        return repo.save(key);
    }
}
