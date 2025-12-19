@RestController
@RequestMapping("/api/bookings")
@Tag(name = "Bookings")
public class RoomBookingController {

    private final RoomBookingService service;

    public RoomBookingController(RoomBookingService service) {
        this.service = service;
    }

    @PostMapping
    public RoomBooking create(@RequestBody RoomBooking booking) {
        return service.createBooking(booking);
    }

    @PutMapping("/{id}")
    public RoomBooking update(@PathVariable Long id, @RequestBody RoomBooking booking) {
        return service.updateBooking(id, booking);
    }

    @GetMapping("/{id}")
    public RoomBooking get(@PathVariable Long id) {
        return service.getBookingById(id);
    }

    @GetMapping("/guest/{guestId}")
    public List<RoomBooking> list(@PathVariable Long guestId) {
        return service.getBookingsForGuest(guestId);
    }

    @PutMapping("/{id}/deactivate")
    public void deactivate(@PathVariable Long id) {
        service.deactivateBooking(id);
    }
}
