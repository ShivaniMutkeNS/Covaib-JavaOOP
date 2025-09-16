public class PriorityScheduler implements JobScheduler {
    @Override
    public void scheduleJob(String jobId) {
        System.out.println("[PRIORITY] Added job " + jobId + " based on priority");
    }

    @Override
    public void cancelJob(String jobId) {
        System.out.println("[PRIORITY] Cancelled job " + jobId);
    }
}


