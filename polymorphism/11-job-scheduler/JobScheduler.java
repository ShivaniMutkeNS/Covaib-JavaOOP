public interface JobScheduler {
    void scheduleJob(String jobId);
    void cancelJob(String jobId);
}


