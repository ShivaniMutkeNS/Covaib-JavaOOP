import java.util.LinkedList;
import java.util.Queue;

/**
 * Round-robin job scheduler implementation
 * Gives each job equal time slices and cycles through jobs fairly
 */
public class RoundRobinScheduler extends JobScheduler {
    private Queue<Job> roundRobinQueue;
    private int timeSliceMs;
    private Job currentJob;
    private long currentJobStartTime;
    
    public RoundRobinScheduler(String name, int maxConcurrentJobs, double cpuCapacity, double memoryCapacity, int timeSliceMs) {
        super(name, maxConcurrentJobs, cpuCapacity, memoryCapacity);
        this.roundRobinQueue = new LinkedList<>();
        this.timeSliceMs = timeSliceMs;
        this.currentJob = null;
    }
    
    @Override
    public void scheduleJob(Job job) {
        roundRobinQueue.offer(job);
        jobQueue.offer(job); // Keep for base class compatibility
        System.out.println("🔄 Job added to round-robin queue: " + job.getName());
    }
    
    @Override
    public Job selectNextJob() {
        // Check if current job's time slice is up
        if (currentJob != null && isTimeSliceExpired()) {
            preemptCurrentJob();
        }
        
        // Select next job from round-robin queue
        Job nextJob = null;
        while (!roundRobinQueue.isEmpty()) {
            Job candidate = roundRobinQueue.poll();
            if (candidate.isReadyToRun() && hasResourcesForJob(candidate)) {
                nextJob = candidate;
                break;
            } else if (!candidate.getStatus().isTerminal()) {
                // Put back in queue if not ready but not terminal
                roundRobinQueue.offer(candidate);
            }
        }
        
        if (nextJob != null) {
            currentJob = nextJob;
            currentJobStartTime = System.currentTimeMillis();
        }
        
        return nextJob;
    }
    
    private boolean isTimeSliceExpired() {
        return (System.currentTimeMillis() - currentJobStartTime) >= timeSliceMs;
    }
    
    private void preemptCurrentJob() {
        if (currentJob != null && currentJob.getStatus() == JobStatus.RUNNING) {
            currentJob.pause();
            roundRobinQueue.offer(currentJob); // Put back in queue
            System.out.println("⏰ Time slice expired for job: " + currentJob.getName());
            currentJob = null;
        }
    }
    
    @Override
    public void prioritizeJobs() {
        // Round-robin doesn't prioritize - all jobs get equal treatment
        System.out.println("🔄 Round-robin scheduler treats all jobs equally");
    }
    
    @Override
    public String getSchedulingStrategy() {
        return "Round-robin with " + timeSliceMs + "ms time slices";
    }
    
    @Override
    protected void processJobs() {
        while (isRunning && (!roundRobinQueue.isEmpty() || !runningJobs.isEmpty())) {
            // Check for completed jobs
            checkCompletedJobs();
            
            // Check if current job needs preemption
            if (currentJob != null && isTimeSliceExpired()) {
                preemptCurrentJob();
            }
            
            // Try to start new jobs if resources are available
            if (canStartNewJob()) {
                Job nextJob = selectNextJob();
                if (nextJob != null) {
                    startJob(nextJob);
                }
            }
            
            // Simulate processing time
            try {
                Thread.sleep(Math.min(100, timeSliceMs / 10)); // Check more frequently than time slice
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
    
    public void setTimeSlice(int timeSliceMs) {
        this.timeSliceMs = timeSliceMs;
        System.out.println("⏱️ Time slice updated to: " + timeSliceMs + "ms");
    }
    
    public int getTimeSlice() {
        return timeSliceMs;
    }
    
    public Job getCurrentJob() {
        return currentJob;
    }
    
    public long getRemainingTimeSlice() {
        if (currentJob == null) return 0;
        long elapsed = System.currentTimeMillis() - currentJobStartTime;
        return Math.max(0, timeSliceMs - elapsed);
    }
    
    @Override
    public void printStatus() {
        super.printStatus();
        
        if (currentJob != null) {
            System.out.println("Current Job: " + currentJob.getName());
            System.out.println("Remaining Time Slice: " + getRemainingTimeSlice() + "ms");
        }
        
        System.out.println("Time Slice: " + timeSliceMs + "ms");
        System.out.println("Round-Robin Queue Size: " + roundRobinQueue.size());
    }
}
