import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Comprehensive demonstration of the job scheduling system
 * Shows polymorphism and different scheduling strategies in action
 */
public class JobSchedulingDemo {
    
    public static void main(String[] args) {
        System.out.println("🚀 JOB SCHEDULING SYSTEM DEMONSTRATION");
        System.out.println("=" .repeat(60));
        
        // Create different types of schedulers
        PriorityScheduler priorityScheduler = new PriorityScheduler("Priority Scheduler", 3, 8.0, 16.0);
        RoundRobinScheduler roundRobinScheduler = new RoundRobinScheduler("Round Robin Scheduler", 2, 4.0, 8.0, 2000);
        CronScheduler cronScheduler = new CronScheduler("Cron Scheduler", 2, 4.0, 8.0);
        FairShareScheduler fairShareScheduler = new FairShareScheduler("Fair Share Scheduler", 3, 6.0, 12.0);
        
        // Demonstrate polymorphism with scheduler array
        JobScheduler[] schedulers = {priorityScheduler, roundRobinScheduler, cronScheduler, fairShareScheduler};
        
        System.out.println("\n📋 CREATED SCHEDULERS:");
        for (JobScheduler scheduler : schedulers) {
            System.out.println("• " + scheduler.toString());
        }
        
        // Demo 1: Priority Scheduling
        demonstratePriorityScheduling(priorityScheduler);
        
        // Demo 2: Round Robin Scheduling
        demonstrateRoundRobinScheduling(roundRobinScheduler);
        
        // Demo 3: Cron Scheduling
        demonstrateCronScheduling(cronScheduler);
        
        // Demo 4: Fair Share Scheduling
        demonstrateFairShareScheduling(fairShareScheduler);
        
        // Demo 5: Scheduler Comparison
        demonstrateSchedulerComparison(schedulers);
        
        System.out.println("\n🎉 JOB SCHEDULING DEMONSTRATION COMPLETED!");
    }
    
    private static void demonstratePriorityScheduling(PriorityScheduler scheduler) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🔥 DEMO 1: PRIORITY SCHEDULING");
        System.out.println("=".repeat(60));
        
        // Create jobs with different priorities
        Job criticalJob = new Job("Database Backup", "Critical system backup", JobPriority.CRITICAL, "admin");
        criticalJob.setEstimatedDuration(Duration.ofSeconds(3));
        criticalJob.setCpuRequirement(2.0);
        criticalJob.setMemoryRequirement(4.0);
        
        Job highJob = new Job("Security Scan", "System security scan", JobPriority.HIGH, "security");
        highJob.setEstimatedDuration(Duration.ofSeconds(2));
        highJob.setCpuRequirement(1.5);
        highJob.setMemoryRequirement(2.0);
        
        Job mediumJob = new Job("Log Analysis", "Analyze system logs", JobPriority.MEDIUM, "analyst");
        mediumJob.setEstimatedDuration(Duration.ofSeconds(2));
        mediumJob.setCpuRequirement(1.0);
        mediumJob.setMemoryRequirement(1.0);
        
        Job lowJob = new Job("Cleanup Task", "Clean temporary files", JobPriority.LOW, "maintenance");
        lowJob.setEstimatedDuration(Duration.ofSeconds(1));
        lowJob.setCpuRequirement(0.5);
        lowJob.setMemoryRequirement(0.5);
        
        // Submit jobs in random order
        scheduler.submitJob(lowJob);
        scheduler.submitJob(criticalJob);
        scheduler.submitJob(mediumJob);
        scheduler.submitJob(highJob);
        
        scheduler.printPriorityQueue();
        
        // Start scheduler
        scheduler.start();
        
        // Wait for jobs to complete
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        scheduler.stop();
        scheduler.printStatus();
        
        // Demonstrate priority boosting
        Job newJob = new Job("Emergency Fix", "Critical bug fix", JobPriority.LOW, "developer");
        scheduler.submitJob(newJob);
        scheduler.boostJobPriority(newJob.getJobId(), JobPriority.CRITICAL);
    }
    
    private static void demonstrateRoundRobinScheduling(RoundRobinScheduler scheduler) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🔄 DEMO 2: ROUND ROBIN SCHEDULING");
        System.out.println("=".repeat(60));
        
        // Create jobs with similar resource requirements
        Job[] jobs = {
            new Job("Task A", "Processing task A", JobPriority.MEDIUM, "user1"),
            new Job("Task B", "Processing task B", JobPriority.MEDIUM, "user2"),
            new Job("Task C", "Processing task C", JobPriority.MEDIUM, "user3"),
            new Job("Task D", "Processing task D", JobPriority.MEDIUM, "user4")
        };
        
        // Set similar durations and requirements
        for (Job job : jobs) {
            job.setEstimatedDuration(Duration.ofSeconds(5));
            job.setCpuRequirement(1.0);
            job.setMemoryRequirement(1.0);
            scheduler.submitJob(job);
        }
        
        System.out.println("Time slice: " + scheduler.getTimeSlice() + "ms");
        
        scheduler.start();
        
        // Monitor for a while
        try {
            for (int i = 0; i < 10; i++) {
                Thread.sleep(1000);
                if (scheduler.getCurrentJob() != null) {
                    System.out.println("Current job: " + scheduler.getCurrentJob().getName() + 
                                     " (remaining: " + scheduler.getRemainingTimeSlice() + "ms)");
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        scheduler.stop();
        scheduler.printStatus();
    }
    
    private static void demonstrateCronScheduling(CronScheduler scheduler) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("⏰ DEMO 3: CRON SCHEDULING");
        System.out.println("=".repeat(60));
        
        // Create scheduled jobs
        Job dailyReport = new Job("Daily Report", "Generate daily analytics report", JobPriority.MEDIUM, "reporter");
        dailyReport.setEstimatedDuration(Duration.ofSeconds(2));
        scheduler.scheduleJobWithCron(dailyReport, CronScheduler.daily());
        
        Job frequentBackup = new Job("Incremental Backup", "Backup changed files", JobPriority.HIGH, "backup");
        frequentBackup.setEstimatedDuration(Duration.ofSeconds(1));
        scheduler.scheduleJobWithCron(frequentBackup, CronScheduler.every5Minutes());
        
        Job weeklyMaintenance = new Job("Weekly Maintenance", "System maintenance tasks", JobPriority.LOW, "maintenance");
        weeklyMaintenance.setEstimatedDuration(Duration.ofSeconds(3));
        scheduler.scheduleJobWithCron(weeklyMaintenance, CronScheduler.weekly());
        
        // Create a job that should run soon
        Job immediateJob = new Job("Immediate Task", "Task scheduled to run soon", JobPriority.HIGH, "admin");
        immediateJob.setEstimatedDuration(Duration.ofSeconds(1));
        LocalDateTime soon = LocalDateTime.now().plusSeconds(2);
        String cronExpr = soon.getMinute() + " " + soon.getHour() + " " + soon.getDayOfMonth() + 
                         " " + soon.getMonthValue() + " *";
        scheduler.scheduleJobWithCron(immediateJob, cronExpr);
        
        scheduler.printCronSchedule();
        
        scheduler.start();
        
        // Wait to see some executions
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        scheduler.stop();
        scheduler.printStatus();
    }
    
    private static void demonstrateFairShareScheduling(FairShareScheduler scheduler) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("⚖️ DEMO 4: FAIR SHARE SCHEDULING");
        System.out.println("=".repeat(60));
        
        // Allocate shares to different users
        scheduler.allocateUserShare("alice", 40.0);
        scheduler.allocateUserShare("bob", 30.0);
        scheduler.allocateUserShare("charlie", 20.0);
        scheduler.allocateUserShare("diana", 10.0);
        
        // Create jobs for different users
        String[] users = {"alice", "bob", "charlie", "diana"};
        String[] taskTypes = {"Analysis", "Processing", "Computation", "Optimization"};
        
        for (int i = 0; i < 12; i++) {
            String user = users[i % users.length];
            String taskType = taskTypes[i % taskTypes.length];
            
            Job job = new Job(user + "'s " + taskType + " " + (i/users.length + 1), 
                            "Task for user " + user, JobPriority.MEDIUM, user);
            job.setEstimatedDuration(Duration.ofSeconds(2));
            job.setCpuRequirement(1.0);
            job.setMemoryRequirement(1.0);
            
            scheduler.submitJob(job);
        }
        
        scheduler.printFairShareStatus();
        
        scheduler.start();
        
        // Monitor fair share ratios
        try {
            for (int i = 0; i < 8; i++) {
                Thread.sleep(1000);
                if (i % 3 == 0) {
                    System.out.println("\n📊 Fair Share Update:");
                    for (String user : users) {
                        double ratio = scheduler.getUserFairShareRatio(user);
                        System.out.println("  " + user + ": " + String.format("%.2f", ratio) + 
                                         " (share: " + scheduler.getUserShare(user) + "%)");
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        scheduler.stop();
        scheduler.printFairShareStatus();
    }
    
    private static void demonstrateSchedulerComparison(JobScheduler[] schedulers) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📊 DEMO 5: SCHEDULER COMPARISON");
        System.out.println("=".repeat(60));
        
        // Create identical job sets for each scheduler
        for (JobScheduler scheduler : schedulers) {
            // Reset scheduler
            scheduler.stop();
            
            // Create test jobs
            for (int i = 1; i <= 5; i++) {
                Job job = new Job("Test Job " + i, "Comparison test job", 
                                JobPriority.values()[i % JobPriority.values().length], "testuser");
                job.setEstimatedDuration(Duration.ofSeconds(1));
                job.setCpuRequirement(0.5);
                job.setMemoryRequirement(0.5);
                scheduler.submitJob(job);
            }
        }
        
        System.out.println("📋 SCHEDULER STRATEGIES:");
        for (JobScheduler scheduler : schedulers) {
            System.out.println("• " + scheduler.getSchedulerName() + ": " + scheduler.getSchedulingStrategy());
        }
        
        // Start all schedulers
        System.out.println("\n🚀 Starting all schedulers...");
        for (JobScheduler scheduler : schedulers) {
            scheduler.start();
        }
        
        // Monitor progress
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Stop all schedulers and show results
        System.out.println("\n📊 FINAL COMPARISON RESULTS:");
        System.out.println("-".repeat(80));
        System.out.printf("%-20s %-15s %-10s %-10s %-15s%n", 
                         "Scheduler", "Strategy", "Completed", "Running", "Efficiency");
        System.out.println("-".repeat(80));
        
        for (JobScheduler scheduler : schedulers) {
            scheduler.stop();
            double efficiency = scheduler.getCompletedJobsCount() > 0 ? 
                              (scheduler.getCompletedJobsCount() * 100.0) / 
                              (scheduler.getCompletedJobsCount() + scheduler.getQueueSize()) : 0.0;
            
            System.out.printf("%-20s %-15s %-10d %-10d %-15.1f%%%n",
                             scheduler.getSchedulerName().length() > 18 ? 
                             scheduler.getSchedulerName().substring(0, 18) + ".." : scheduler.getSchedulerName(),
                             scheduler.getSchedulingStrategy().length() > 13 ?
                             scheduler.getSchedulingStrategy().substring(0, 13) + ".." : scheduler.getSchedulingStrategy(),
                             scheduler.getCompletedJobsCount(),
                             scheduler.getRunningJobsCount(),
                             efficiency);
        }
        
        System.out.println("\n🎯 SCHEDULING INSIGHTS:");
        System.out.println("• Priority Scheduler: Best for critical tasks and SLA requirements");
        System.out.println("• Round Robin: Fair time allocation, good for interactive workloads");
        System.out.println("• Cron Scheduler: Perfect for time-based and recurring tasks");
        System.out.println("• Fair Share: Optimal for multi-tenant environments with resource quotas");
    }
}
