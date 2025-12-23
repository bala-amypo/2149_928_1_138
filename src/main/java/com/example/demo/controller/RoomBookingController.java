@RestController
@RequestMapping("/api/bookings")
public class RoomBookingController {

    private final RoomBookingRepository repository;

    public RoomBookingController(RoomBookingRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public RoomBooking createBooking(@RequestBody RoomBooking booking) {
        return repository.save(booking);
    }
}
