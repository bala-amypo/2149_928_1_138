@Service
public class GuestServiceImpl implements GuestService {

    private final GuestRepository guestRepository;
    private final PasswordEncoder passwordEncoder;

    public GuestServiceImpl(
            GuestRepository guestRepository,
            PasswordEncoder passwordEncoder) {
        this.guestRepository = guestRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Guest createGuest(Guest guest) {

        if (guest == null) {
            throw new IllegalArgumentException("guest required");
        }

        if (guest.getEmail() == null) {
            throw new IllegalArgumentException("email required");
        }

        if (guestRepository.existsByEmail(guest.getEmail())) {
            throw new IllegalArgumentException("email already exists");
        }

        // Encode only if raw password
        if (guest.getPassword() != null &&
                !guest.getPassword().startsWith("$2a$")) {
            guest.setPassword(passwordEncoder.encode(guest.getPassword()));
        }

        // AMYPO EXPECTED DEFAULTS
        if (guest.getActive() == null) guest.setActive(true);
        if (guest.getVerified() == null) guest.setVerified(false);
        if (guest.getRole() == null) guest.setRole("ROLE_USER");

        return guestRepository.save(guest);
    }

    @Override
    public Guest getGuestById(Long id) {
        return guestRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Guest not found"));
    }

    @Override
    public List<Guest> getAllGuests() {
        return guestRepository.findAll();
    }

    @Override
    public Guest updateGuest(Long id, Guest update) {

        Guest existing = guestRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Guest not found"));

        if (update.getFullName() != null)
            existing.setFullName(update.getFullName());

        if (update.getPhoneNumber() != null)
            existing.setPhoneNumber(update.getPhoneNumber());

        if (update.getVerified() != null)
            existing.setVerified(update.getVerified());

        if (update.getActive() != null)
            existing.setActive(update.getActive());

        // ROLE NORMALIZATION (CRITICAL)
        if (update.getRole() != null) {
            String role = update.getRole();
            if (!role.startsWith("ROLE_")) {
                role = "ROLE_" + role.toUpperCase();
            }
            existing.setRole(role);
        }

        return guestRepository.save(existing);
    }

    @Override
    public void deactivateGuest(Long id) {

        Guest guest = guestRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Guest not found"));

        guest.setActive(false);
        guestRepository.save(guest);
    }
}
