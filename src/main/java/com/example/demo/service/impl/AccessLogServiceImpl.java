@Service
public class AccessLogServiceImpl implements AccessLogService {

    private final AccessLogRepository repo;

    public AccessLogServiceImpl(AccessLogRepository repo) {
        this.repo = repo;
    }

    @Override
    public AccessLog createLog(AccessLog log) {
        if (log.getAccessTime().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("future");
        }
        log.setResult("SUCCESS");
        return repo.save(log);
    }
}
