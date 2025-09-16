public class JobSchedulerDemo {
    public static void main(String[] args) {
        JobScheduler[] schedulers = new JobScheduler[] {
            new CronScheduler(), new RealtimeScheduler(), new PriorityScheduler()
        };
        for (int i = 0; i < schedulers.length; i++) {
            String jobId = "job-" + (i + 1);
            schedulers[i].scheduleJob(jobId);
            schedulers[i].cancelJob(jobId);
        }
    }
}


