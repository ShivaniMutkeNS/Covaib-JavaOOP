public class RealtimeScheduler implements JobScheduler {
    @Override
    public void scheduleJob(String jobId) {
        System.out.println("[REALTIME] Enqueued job " + jobId + " for immediate execution");
    }

    @Override
    public void cancelJob(String jobId) {
        System.out.println("[REALTIME] Cancelled job " + jobId);
    }
}


