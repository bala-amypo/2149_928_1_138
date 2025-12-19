@Entity
public class KeyShareRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private DigitalKey digitalKey;

    @ManyToOne
    private Guest sharedBy;

    @ManyToOne
    private Guest sharedWith;

    private LocalDateTime shareStart;
    private LocalDateTime shareEnd;
    private String status;

    @PrePersist
    public void init() {
        status = "PENDING";
    }
}
