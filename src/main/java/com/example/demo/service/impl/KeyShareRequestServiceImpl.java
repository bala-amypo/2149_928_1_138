@Service
public class KeyShareRequestServiceImpl implements KeyShareRequestService {

    private final KeyShareRequestRepository repo;

    public KeyShareRequestServiceImpl(KeyShareRequestRepository repo) {
        this.repo = repo;
    }

    @Override
    public KeyShareRequest createShareRequest(KeyShareRequest req) {
        if (req.getSharedBy().getId().equals(req.getSharedWith().getId())) {
            throw new IllegalArgumentException("sharedBy and sharedWith");
        }
        if (!req.getShareEnd().isAfter(req.getShareStart())) {
            throw new IllegalArgumentException("Share end must be after start");
        }
        return repo.save(req);
    }

    @Override
    public KeyShareRequest updateStatus(Long id, String status) {
        KeyShareRequest req = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found"));
        req.setStatus(status);
        return repo.save(req);
    }
}
