import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Comparator;

/**
 * Fair-share job scheduler implementation
 * Allocates resources fairly among users based on their share allocation
 */
public class FairShareScheduler extends JobScheduler {
    private Map<String, Double> userShares; // User -> allocated share percentage
    private Map<String, Double> userUsage; // User -> current usage
    private Map<String, Integer> userJobCounts; // User -> number of jobs
    private PriorityQueue<Job> fairShareQueue;
    private double totalAllocatedShares;
    
    public FairShareScheduler(String name, int maxConcurrentJobs, double cpuCapacity, double memoryCapacity) {
        super(name, maxConcurrentJobs, cpuCapacity, memoryCapacity);
        this.userShares = new HashMap<>();
        this.userUsage = new HashMap<>();
        this.userJobCounts = new HashMap<>();
        this.totalAllocatedShares = 0.0;
        
        // Priority queue ordered by fair share ratio (usage vs allocation)
        this.fairShareQueue = new PriorityQueue<>(Comparator
            .comparing((Job job) -> calculateFairShareRatio(job.getOwner()))
            .thenComparing((Job job) -> job.getPriority().getLevel()).reversed()
            .thenComparing(Job::getCreatedTime));
    }
    
    @Override
    public void scheduleJob(Job job) {
        String owner = job.getOwner();
        
        // Initialize user if not exists
        if (!userShares.containsKey(owner)) {
            allocateUserShare(owner, 10.0); // Default 10% share
        }
        
        fairShareQueue.offer(job);
        jobQueue.offer(job); // Keep for base class compatibility
        userJobCounts.put(owner, userJobCounts.getOrDefault(owner, 0) + 1);
        
        System.out.println("⚖️ Job scheduled for user " + owner + " (share: " + 
                          String.format("%.1f%%", userShares.get(owner)) + "): " + job.getName());
    }
    
    @Override
    public Job selectNextJob() {
        // Select job from user who is most under-allocated relative to their share
        for (Job job : fairShareQueue) {
            String owner = job.getOwner();
            if (job.isReadyToRun() && hasResourcesForJob(job) && canUserRunJob(owner)) {
                fairShareQueue.remove(job);
                return job;
            }
        }
        return null;
    }
    
    @Override
    public void prioritizeJobs() {
        // Rebuild priority queue based on current fair share ratios
        PriorityQueue<Job> newQueue = new PriorityQueue<>(Comparator
            .comparing((Job job) -> calculateFairShareRatio(job.getOwner()))
            .thenComparing((Job job) -> job.getPriority().getLevel()).reversed()
            .thenComparing(Job::getCreatedTime));
        
        newQueue.addAll(fairShareQueue);
        this.fairShareQueue = newQueue;
        
        System.out.println("⚖️ Jobs reprioritized based on fair share ratios");
    }
    
    @Override
    public String getSchedulingStrategy() {
        return "Fair-share scheduling with user resource allocation";
    }
    
    @Override
    protected void startJob(Job job) {
        super.startJob(job);
        
        // Update user usage
        String owner = job.getOwner();
        double currentUsage = userUsage.getOrDefault(owner, 0.0);
        double jobResourceUsage = (job.getCpuRequirement() + job.getMemoryRequirement()) / 2.0; // Simplified
        userUsage.put(owner, currentUsage + jobResourceUsage);
    }
    
    @Override
    protected void checkCompletedJobs() {
        runningJobs.removeIf(job -> {
            if (job.getStatus().isTerminal()) {
                String owner = job.getOwner();
                
                // Update resource usage
                usedCpuCapacity -= job.getCpuRequirement();
                usedMemoryCapacity -= job.getMemoryRequirement();
                
                // Update user usage
                double jobResourceUsage = (job.getCpuRequirement() + job.getMemoryRequirement()) / 2.0;
                double currentUsage = userUsage.getOrDefault(owner, 0.0);
                userUsage.put(owner, Math.max(0.0, currentUsage - jobResourceUsage));
                
                // Update job count
                userJobCounts.put(owner, Math.max(0, userJobCounts.getOrDefault(owner, 0) - 1));
                
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
    
    private boolean canUserRunJob(String owner) {
        double currentRatio = calculateFairShareRatio(owner);
        double allocatedShare = userShares.getOrDefault(owner, 0.0);
        
        // Allow if user is under their allocated share or if no other users are waiting
        return currentRatio <= 1.0 || allocatedShare > 0.0;
    }
    
    private double calculateFairShareRatio(String owner) {
        double allocatedShare = userShares.getOrDefault(owner, 0.0);
        double currentUsage = userUsage.getOrDefault(owner, 0.0);
        
        if (allocatedShare == 0.0) return Double.MAX_VALUE;
        
        double totalCapacity = (totalCpuCapacity + totalMemoryCapacity) / 2.0; // Simplified
        double expectedUsage = (allocatedShare / 100.0) * totalCapacity;
        
        return expectedUsage > 0 ? currentUsage / expectedUsage : 0.0;
    }
    
    public void allocateUserShare(String owner, double sharePercentage) {
        if (sharePercentage < 0 || sharePercentage > 100) {
            System.out.println("❌ Invalid share percentage: " + sharePercentage);
            return;
        }
        
        double oldShare = userShares.getOrDefault(owner, 0.0);
        totalAllocatedShares = totalAllocatedShares - oldShare + sharePercentage;
        
        if (totalAllocatedShares > 100.0) {
            totalAllocatedShares = totalAllocatedShares - sharePercentage + oldShare;
            System.out.println("❌ Cannot allocate " + sharePercentage + "% to " + owner + 
                             ": would exceed 100% total allocation");
            return;
        }
        
        userShares.put(owner, sharePercentage);
        userUsage.putIfAbsent(owner, 0.0);
        userJobCounts.putIfAbsent(owner, 0);
        
        System.out.println("⚖️ Allocated " + sharePercentage + "% share to user: " + owner);
        prioritizeJobs(); // Reprioritize based on new allocation
    }
    
    public void adjustUserShare(String owner, double newSharePercentage) {
        if (userShares.containsKey(owner)) {
            allocateUserShare(owner, newSharePercentage);
        } else {
            System.out.println("❌ User not found: " + owner);
        }
    }
    
    public double getUserShare(String owner) {
        return userShares.getOrDefault(owner, 0.0);
    }
    
    public double getUserUsage(String owner) {
        return userUsage.getOrDefault(owner, 0.0);
    }
    
    public double getUserFairShareRatio(String owner) {
        return calculateFairShareRatio(owner);
    }
    
    public void printFairShareStatus() {
        System.out.println("\n⚖️ FAIR SHARE STATUS");
        System.out.println(new String(new char[70]).replace('\0', '='));
        System.out.printf("%-15s %-10s %-10s %-10s %-10s %-10s%n", 
                         "User", "Share %", "Usage", "Ratio", "Jobs", "Status");
        System.out.println(new String(new char[70]).replace('\0', '-'));
        
        for (String owner : userShares.keySet()) {
            double share = userShares.get(owner);
            double usage = userUsage.getOrDefault(owner, 0.0);
            double ratio = calculateFairShareRatio(owner);
            int jobCount = userJobCounts.getOrDefault(owner, 0);
            String status = ratio <= 1.0 ? "Under" : "Over";
            
            System.out.printf("%-15s %-10.1f %-10.2f %-10.2f %-10d %-10s%n",
                             owner.length() > 13 ? owner.substring(0, 13) + ".." : owner,
                             share, usage, ratio, jobCount, status);
        }
        
        System.out.println();
        System.out.println("Total Allocated Shares: " + String.format("%.1f%%", totalAllocatedShares));
        System.out.println("Available Shares: " + String.format("%.1f%%", 100.0 - totalAllocatedShares));
    }
    
    @Override
    public void printStatus() {
        super.printStatus();
        System.out.println("Total Users: " + userShares.size());
        System.out.println("Allocated Shares: " + String.format("%.1f%%", totalAllocatedShares));
        
        // Show top users by job count
        System.out.println("\nTop Users by Job Count:");
        userJobCounts.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(5)
            .forEach(entry -> 
                System.out.println("  • " + entry.getKey() + ": " + entry.getValue() + " jobs")
            );
    }
}
