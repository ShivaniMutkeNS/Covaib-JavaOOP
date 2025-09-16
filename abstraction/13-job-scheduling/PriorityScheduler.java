import java.util.PriorityQueue;
import java.util.Comparator;

/**
 * Priority-based job scheduler implementation
 * Schedules jobs based on priority levels with FIFO for same priority
 */
public class PriorityScheduler extends JobScheduler {
    private PriorityQueue<Job> priorityQueue;
    
    public PriorityScheduler(String name, int maxConcurrentJobs, double cpuCapacity, double memoryCapacity) {
        super(name, maxConcurrentJobs, cpuCapacity, memoryCapacity);
        
        // Priority queue with custom comparator
        this.priorityQueue = new PriorityQueue<>(Comparator
            .comparing((Job job) -> job.getPriority().getLevel()).reversed() // Higher priority first
            .thenComparing(Job::getCreatedTime)); // FIFO for same priority
    }
    
    @Override
    public void scheduleJob(Job job) {
        priorityQueue.offer(job);
        jobQueue.offer(job); // Keep for base class compatibility
        System.out.println("📋 Job scheduled with priority " + job.getPriority().getDisplayName() + ": " + job.getName());
    }
    
    @Override
    public Job selectNextJob() {
        // Select highest priority job that has required resources
        for (Job job : priorityQueue) {
            if (job.isReadyToRun() && hasResourcesForJob(job)) {
                priorityQueue.remove(job);
                return job;
            }
        }
        return null;
    }
    
    @Override
    public void prioritizeJobs() {
        // Priority queue automatically maintains priority order
        System.out.println("🔄 Jobs are automatically prioritized by priority level");
    }
    
    @Override
    public String getSchedulingStrategy() {
        return "Priority-based scheduling with FIFO for same priority";
    }
    
    public void boostJobPriority(String jobId, JobPriority newPriority) {
        Job jobToBoost = null;
        
        // Find job in priority queue
        for (Job job : priorityQueue) {
            if (job.getJobId().equals(jobId)) {
                jobToBoost = job;
                break;
            }
        }
        
        if (jobToBoost != null) {
            priorityQueue.remove(jobToBoost);
            jobToBoost.setPriority(newPriority);
            priorityQueue.offer(jobToBoost);
            System.out.println("⬆️ Boosted job priority: " + jobToBoost.getName() + " to " + newPriority.getDisplayName());
        }
    }
    
    public void printPriorityQueue() {
        System.out.println("\n📊 PRIORITY QUEUE STATUS");
        System.out.println("========================================");
        
        if (priorityQueue.isEmpty()) {
            System.out.println("No jobs in priority queue");
            return;
        }
        
        // Group by priority
        for (JobPriority priority : JobPriority.values()) {
            long count = priorityQueue.stream()
                .filter(job -> job.getPriority() == priority)
                .count();
            
            if (count > 0) {
                System.out.println(priority.getDisplayName() + " Priority: " + count + " jobs");
                priorityQueue.stream()
                    .filter(job -> job.getPriority() == priority)
                    .limit(3) // Show first 3 of each priority
                    .forEach(job -> System.out.println("  • " + job.getName()));
                
                if (count > 3) {
                    System.out.println("  ... and " + (count - 3) + " more");
                }
            }
        }
        System.out.println();
    }
}
