@Entity
public class DigitalKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private RoomBooking booking;

    @Column(unique = true)
    private String keyValue;

    private LocalDateTime issuedAt;
    private LocalDateTime expiresAt;

    private Boolean active = true;

    // getters/setters
}
