import java.util.List;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * Abstract base class for all job scheduling implementations
 * Defines core scheduling operations that all schedulers must implement
 */
public abstract class JobScheduler {
    protected String schedulerName;
    protected Queue<Job> jobQueue;
    protected List<Job> runningJobs;
    protected List<Job> completedJobs;
    protected int maxConcurrentJobs;
    protected boolean isRunning;
    protected double totalCpuCapacity;
    protected double totalMemoryCapacity;
    protected double usedCpuCapacity;
    protected double usedMemoryCapacity;
    
    public JobScheduler(String schedulerName, int maxConcurrentJobs, double cpuCapacity, double memoryCapacity) {
        this.schedulerName = schedulerName;
        this.maxConcurrentJobs = maxConcurrentJobs;
        this.totalCpuCapacity = cpuCapacity;
        this.totalMemoryCapacity = memoryCapacity;
        this.jobQueue = new ConcurrentLinkedQueue<>();
        this.runningJobs = new ArrayList<>();
        this.completedJobs = new ArrayList<>();
        this.isRunning = false;
        this.usedCpuCapacity = 0.0;
        this.usedMemoryCapacity = 0.0;
    }
    
    // Abstract methods that must be implemented by concrete schedulers
    public abstract void scheduleJob(Job job);
    public abstract Job selectNextJob();
    public abstract void prioritizeJobs();
    public abstract String getSchedulingStrategy();
    
    // Concrete methods with default implementation
    public void start() {
        this.isRunning = true;
        System.out.println("🚀 Scheduler started: " + schedulerName);
        processJobs();
    }
    
    public void stop() {
        this.isRunning = false;
        System.out.println("🛑 Scheduler stopped: " + schedulerName);
    }
    
    public void pause() {
        this.isRunning = false;
        System.out.println("⏸️ Scheduler paused: " + schedulerName);
    }
    
    public void resume() {
        this.isRunning = true;
        System.out.println("▶️ Scheduler resumed: " + schedulerName);
        processJobs();
    }
    
    protected void processJobs() {
        while (isRunning && (!jobQueue.isEmpty() || !runningJobs.isEmpty())) {
            // Check for completed jobs
            checkCompletedJobs();
            
            // Try to start new jobs if resources are available
            if (canStartNewJob()) {
                Job nextJob = selectNextJob();
                if (nextJob != null) {
                    startJob(nextJob);
                }
            }
            
            // Simulate processing time
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
    
    protected boolean canStartNewJob() {
        return runningJobs.size() < maxConcurrentJobs && !jobQueue.isEmpty();
    }
    
    protected boolean hasResourcesForJob(Job job) {
        return (usedCpuCapacity + job.getCpuRequirement()) <= totalCpuCapacity &&
               (usedMemoryCapacity + job.getMemoryRequirement()) <= totalMemoryCapacity;
    }
    
    protected void startJob(Job job) {
        if (hasResourcesForJob(job)) {
            jobQueue.remove(job);
            runningJobs.add(job);
            usedCpuCapacity += job.getCpuRequirement();
            usedMemoryCapacity += job.getMemoryRequirement();
            job.start();
            
            // Simulate job execution in background
            simulateJobExecution(job);
        } else {
            System.out.println("⚠️ Insufficient resources for job: " + job.getName());
        }
    }
    
    protected void simulateJobExecution(Job job) {
        new Thread(() -> {
            try {
                // Simulate job execution time
                long executionTime = job.getEstimatedDuration().toMillis();
                Thread.sleep(executionTime);
                
                // Simulate success/failure (90% success rate)
                if (Math.random() > 0.1) {
                    job.complete();
                } else {
                    job.fail("Simulated execution error");
                }
            } catch (InterruptedException e) {
                job.cancel();
                Thread.currentThread().interrupt();
            }
        }).start();
    }
    
    protected void checkCompletedJobs() {
        runningJobs.removeIf(job -> {
            if (job.getStatus().isTerminal()) {
                usedCpuCapacity -= job.getCpuRequirement();
                usedMemoryCapacity -= job.getMemoryRequirement();
                completedJobs.add(job);
                
                // Handle failed jobs
                if (job.getStatus() == JobStatus.FAILED && job.getRetryCount() < job.getMaxRetries()) {
                    job.retry();
                    scheduleJob(job);
                }
                
                return true;
            }
            return false;
        });
    }
    
    public void submitJob(Job job) {
        scheduleJob(job);
        System.out.println("📝 Job submitted: " + job.getName() + " to " + schedulerName);
    }
    
    public void cancelJob(String jobId) {
        // Cancel from queue
        jobQueue.removeIf(job -> {
            if (job.getJobId().equals(jobId)) {
                job.cancel();
                return true;
            }
            return false;
        });
        
        // Cancel running job
        runningJobs.stream()
                  .filter(job -> job.getJobId().equals(jobId))
                  .forEach(Job::cancel);
    }
    
    public void printStatus() {
        System.out.println("\n📊 SCHEDULER STATUS: " + schedulerName);
        System.out.println(new String(new char[50]).replace('\0', '='));
        System.out.println("Strategy: " + getSchedulingStrategy());
        System.out.println("Status: " + (isRunning ? "Running" : "Stopped"));
        System.out.println("Jobs in Queue: " + jobQueue.size());
        System.out.println("Running Jobs: " + runningJobs.size() + "/" + maxConcurrentJobs);
        System.out.println("Completed Jobs: " + completedJobs.size());
        System.out.println("CPU Usage: " + String.format("%.1f/%.1f cores (%.1f%%)", 
            usedCpuCapacity, totalCpuCapacity, (usedCpuCapacity/totalCpuCapacity)*100));
        System.out.println("Memory Usage: " + String.format("%.1f/%.1f GB (%.1f%%)", 
            usedMemoryCapacity, totalMemoryCapacity, (usedMemoryCapacity/totalMemoryCapacity)*100));
        
        if (!runningJobs.isEmpty()) {
            System.out.println("\nRunning Jobs:");
            for (Job job : runningJobs) {
                System.out.println("  • " + job.getName() + " (" + String.format("%.1f%%", job.getProgressPercentage()) + ")");
            }
        }
        
        if (!jobQueue.isEmpty()) {
            System.out.println("\nQueued Jobs:");
            int count = 0;
            for (Job job : jobQueue) {
                if (count++ < 5) { // Show first 5
                    System.out.println("  • " + job.getName() + " (" + job.getPriority().getDisplayName() + ")");
                }
            }
            if (jobQueue.size() > 5) {
                System.out.println("  ... and " + (jobQueue.size() - 5) + " more");
            }
        }
        System.out.println();
    }
    
    // Getters
    public String getSchedulerName() { return schedulerName; }
    public boolean isRunning() { return isRunning; }
    public int getQueueSize() { return jobQueue.size(); }
    public int getRunningJobsCount() { return runningJobs.size(); }
    public int getCompletedJobsCount() { return completedJobs.size(); }
    public double getCpuUtilization() { return (usedCpuCapacity / totalCpuCapacity) * 100.0; }
    public double getMemoryUtilization() { return (usedMemoryCapacity / totalMemoryCapacity) * 100.0; }
    public List<Job> getRunningJobs() { return new java.util.ArrayList<>(runningJobs); }
    public List<Job> getCompletedJobs() { return new java.util.ArrayList<>(completedJobs); }
    
    @Override
    public String toString() {
        return String.format("%s (%s) - %d queued, %d running, %d completed", 
            schedulerName, getSchedulingStrategy(), jobQueue.size(), runningJobs.size(), completedJobs.size());
    }
}
