public class CronScheduler implements JobScheduler {
    @Override
    public void scheduleJob(String jobId) {
        System.out.println("[CRON] Scheduled job " + jobId + " using cron expression");
    }

    @Override
    public void cancelJob(String jobId) {
        System.out.println("[CRON] Cancelled job " + jobId);
    }
}


