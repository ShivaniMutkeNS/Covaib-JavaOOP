import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Cron-based job scheduler implementation
 * Schedules jobs based on cron expressions for time-based execution
 */
public class CronScheduler extends JobScheduler {
    private Map<Job, String> cronExpressions;
    private Map<Job, LocalDateTime> nextExecutionTimes;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public CronScheduler(String name, int maxConcurrentJobs, double cpuCapacity, double memoryCapacity) {
        super(name, maxConcurrentJobs, cpuCapacity, memoryCapacity);
        this.cronExpressions = new HashMap<>();
        this.nextExecutionTimes = new HashMap<>();
    }
    
    @Override
    public void scheduleJob(Job job) {
        // Default cron expression: every 5 minutes
        scheduleJobWithCron(job, "*/5 * * * *");
    }
    
    public void scheduleJobWithCron(Job job, String cronExpression) {
        if (!isValidCronExpression(cronExpression)) {
            System.out.println("❌ Invalid cron expression: " + cronExpression);
            return;
        }
        
        cronExpressions.put(job, cronExpression);
        LocalDateTime nextExecution = calculateNextExecution(cronExpression);
        nextExecutionTimes.put(job, nextExecution);
        job.schedule(nextExecution);
        jobQueue.offer(job);
        
        System.out.println("⏰ Job scheduled with cron '" + cronExpression + "': " + job.getName() + 
                          " (next: " + nextExecution.format(formatter) + ")");
    }
    
    @Override
    public Job selectNextJob() {
        LocalDateTime now = LocalDateTime.now();
        
        for (Job job : jobQueue) {
            LocalDateTime nextExecution = nextExecutionTimes.get(job);
            if (nextExecution != null && now.isAfter(nextExecution) && hasResourcesForJob(job)) {
                // Reschedule for next execution
                String cronExpr = cronExpressions.get(job);
                LocalDateTime newNextExecution = calculateNextExecution(cronExpr);
                nextExecutionTimes.put(job, newNextExecution);
                
                return job;
            }
        }
        return null;
    }
    
    @Override
    public void prioritizeJobs() {
        // Cron jobs are prioritized by execution time
        System.out.println("⏰ Jobs prioritized by scheduled execution time");
    }
    
    @Override
    public String getSchedulingStrategy() {
        return "Cron-based time scheduling";
    }
    
    private boolean isValidCronExpression(String cronExpression) {
        // Simplified cron validation (minute hour day month dayOfWeek)
        String[] parts = cronExpression.split("\\s+");
        if (parts.length != 5) return false;
        
        // Basic pattern matching for cron fields
        String cronFieldPattern = "^(\\*|\\*/\\d+|\\d+(-\\d+)?(,\\d+(-\\d+)?)*)$";
        Pattern pattern = Pattern.compile(cronFieldPattern);
        
        for (String part : parts) {
            if (!pattern.matcher(part).matches()) {
                return false;
            }
        }
        return true;
    }
    
    private LocalDateTime calculateNextExecution(String cronExpression) {
        // Simplified cron calculation - in real implementation would use proper cron library
        LocalDateTime now = LocalDateTime.now();
        String[] parts = cronExpression.split("\\s+");
        
        try {
            // Parse minute field
            int minute = parseCronField(parts[0], now.getMinute(), 0, 59);
            int hour = parseCronField(parts[1], now.getHour(), 0, 23);
            int day = parseCronField(parts[2], now.getDayOfMonth(), 1, 31);
            int month = parseCronField(parts[3], now.getMonthValue(), 1, 12);
            
            LocalDateTime next = LocalDateTime.of(now.getYear(), month, day, hour, minute, 0);
            
            // If the calculated time is in the past, add appropriate time
            if (next.isBefore(now) || next.equals(now)) {
                if (parts[0].startsWith("*/")) {
                    // Handle interval expressions like */5
                    int interval = Integer.parseInt(parts[0].substring(2));
                    next = now.plusMinutes(interval);
                } else {
                    next = next.plusDays(1);
                }
            }
            
            return next;
        } catch (Exception e) {
            // Fallback: schedule for 5 minutes from now
            return now.plusMinutes(5);
        }
    }
    
    private int parseCronField(String field, int current, int min, int max) {
        if (field.equals("*")) {
            return current;
        } else if (field.startsWith("*/")) {
            int interval = Integer.parseInt(field.substring(2));
            return Math.min(current + interval, max);
        } else if (field.contains(",")) {
            // Take first value from comma-separated list
            return Integer.parseInt(field.split(",")[0]);
        } else {
            int value = Integer.parseInt(field);
            return Math.max(min, Math.min(max, value));
        }
    }
    
    public void updateCronExpression(String jobId, String newCronExpression) {
        Job job = findJobById(jobId);
        if (job != null && isValidCronExpression(newCronExpression)) {
            cronExpressions.put(job, newCronExpression);
            LocalDateTime nextExecution = calculateNextExecution(newCronExpression);
            nextExecutionTimes.put(job, nextExecution);
            job.schedule(nextExecution);
            System.out.println("⏰ Updated cron expression for " + job.getName() + ": " + newCronExpression);
        }
    }
    
    private Job findJobById(String jobId) {
        return jobQueue.stream()
                      .filter(job -> job.getJobId().equals(jobId))
                      .findFirst()
                      .orElse(null);
    }
    
    public void printCronSchedule() {
        System.out.println("\n⏰ CRON SCHEDULE");
        System.out.println(new String(new char[60]).replace('\0', '='));
        
        if (cronExpressions.isEmpty()) {
            System.out.println("No cron jobs scheduled");
            return;
        }
        
        System.out.printf("%-20s %-15s %-20s%n", "Job Name", "Cron Expression", "Next Execution");
        System.out.println(new String(new char[60]).replace('\0', '-'));
        
        for (Map.Entry<Job, String> entry : cronExpressions.entrySet()) {
            Job job = entry.getKey();
            String cronExpr = entry.getValue();
            LocalDateTime nextExec = nextExecutionTimes.get(job);
            
            System.out.printf("%-20s %-15s %-20s%n", 
                job.getName().length() > 18 ? job.getName().substring(0, 18) + ".." : job.getName(),
                cronExpr,
                nextExec != null ? nextExec.format(formatter) : "Not scheduled"
            );
        }
        System.out.println();
    }
    
    // Common cron expression helpers
    public static String everyMinute() { return "* * * * *"; }
    public static String every5Minutes() { return "*/5 * * * *"; }
    public static String every15Minutes() { return "*/15 * * * *"; }
    public static String every30Minutes() { return "*/30 * * * *"; }
    public static String hourly() { return "0 * * * *"; }
    public static String daily() { return "0 0 * * *"; }
    public static String weekly() { return "0 0 * * 0"; }
    public static String monthly() { return "0 0 1 * *"; }
    
    @Override
    public void printStatus() {
        super.printStatus();
        System.out.println("Cron Jobs: " + cronExpressions.size());
        
        // Show next few executions
        LocalDateTime now = LocalDateTime.now();
        System.out.println("\nUpcoming Executions:");
        nextExecutionTimes.entrySet().stream()
            .filter(entry -> entry.getValue().isAfter(now))
            .sorted(Map.Entry.comparingByValue())
            .limit(5)
            .forEach(entry -> 
                System.out.println("  • " + entry.getKey().getName() + 
                                 " at " + entry.getValue().format(formatter))
            );
    }
}
