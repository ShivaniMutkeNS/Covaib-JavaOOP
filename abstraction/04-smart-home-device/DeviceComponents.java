package abstraction.smarthomedevice;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;

/**
 * Supporting components for smart home devices
 */

// Device Scheduler
class DeviceScheduler {
    private String deviceId;
    private Map<String, ScheduleRequest> schedules;
    private ScheduledExecutorService executor;
    private Map<String, ScheduledFuture<?>> scheduledTasks;
    
    public DeviceScheduler(String deviceId) {
        this.deviceId = deviceId;
        this.schedules = new ConcurrentHashMap<>();
        this.scheduledTasks = new ConcurrentHashMap<>();
        this.executor = Executors.newScheduledThreadPool(2, 
            r -> new Thread(r, "DeviceScheduler-" + deviceId));
    }
    
    public ScheduleResult addSchedule(ScheduleRequest request) {
        try {
            schedules.put(request.getScheduleId(), request);
            
            // Calculate delay until first execution
            long delaySeconds = java.time.Duration.between(LocalDateTime.now(), request.getStartTime()).getSeconds();
            if (delaySeconds < 0) {
                return ScheduleResult.failure(request.getScheduleId(), "Start time is in the past");
            }
            
            // Schedule the task
            ScheduledFuture<?> task = scheduleTask(request, delaySeconds);
            scheduledTasks.put(request.getScheduleId(), task);
            
            return ScheduleResult.success(request.getScheduleId(), request.getStartTime());
            
        } catch (Exception e) {
            return ScheduleResult.failure(request.getScheduleId(), "Failed to schedule: " + e.getMessage());
        }
    }
    
    private ScheduledFuture<?> scheduleTask(ScheduleRequest request, long initialDelaySeconds) {
        Runnable task = () -> {
            System.out.println("Executing scheduled operation: " + request.getOperation() + 
                             " for device: " + deviceId);
            // In real implementation, this would call the actual device operation
        };
        
        switch (request.getType()) {
            case ONE_TIME:
                return executor.schedule(task, initialDelaySeconds, TimeUnit.SECONDS);
            case DAILY:
                return executor.scheduleAtFixedRate(task, initialDelaySeconds, 24 * 60 * 60, TimeUnit.SECONDS);
            case WEEKLY:
                return executor.scheduleAtFixedRate(task, initialDelaySeconds, 7 * 24 * 60 * 60, TimeUnit.SECONDS);
            default:
                return executor.schedule(task, initialDelaySeconds, TimeUnit.SECONDS);
        }
    }
    
    public boolean cancelSchedule(String scheduleId) {
        ScheduledFuture<?> task = scheduledTasks.remove(scheduleId);
        if (task != null) {
            task.cancel(false);
            schedules.remove(scheduleId);
            return true;
        }
        return false;
    }
    
    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    
    public Map<String, ScheduleRequest> getActiveSchedules() {
        return new HashMap<>(schedules);
    }
}

// Security Manager
class SecurityManager {
    private String deviceId;
    private Set<String> authorizedUsers;
    private Map<String, LocalDateTime> lastAccessTimes;
    private boolean requiresAuthentication;
    
    public SecurityManager(String deviceId) {
        this.deviceId = deviceId;
        this.authorizedUsers = new HashSet<>();
        this.lastAccessTimes = new ConcurrentHashMap<>();
        this.requiresAuthentication = true;
        
        // Add default admin user
        authorizedUsers.add("admin");
    }
    
    public boolean validateOperation(DeviceOperation operation) {
        if (!requiresAuthentication) {
            return true;
        }
        
        String userId = operation.getUserId();
        if (userId == null || !authorizedUsers.contains(userId)) {
            System.err.println("Unauthorized access attempt by user: " + userId + " on device: " + deviceId);
            return false;
        }
        
        // Record access time
        lastAccessTimes.put(userId, LocalDateTime.now());
        return true;
    }
    
    public void addAuthorizedUser(String userId) {
        authorizedUsers.add(userId);
    }
    
    public void removeAuthorizedUser(String userId) {
        authorizedUsers.remove(userId);
        lastAccessTimes.remove(userId);
    }
    
    public Set<String> getAuthorizedUsers() {
        return new HashSet<>(authorizedUsers);
    }
    
    public LocalDateTime getLastAccessTime(String userId) {
        return lastAccessTimes.get(userId);
    }
}

// Energy Monitor
class EnergyMonitor {
    private String deviceId;
    private EnergyUsage currentUsage;
    private List<EnergyReading> readings;
    private double basePowerConsumption;
    private double operationalPowerConsumption;
    
    public EnergyMonitor(String deviceId) {
        this.deviceId = deviceId;
        this.readings = new ArrayList<>();
        this.basePowerConsumption = 2.0; // 2W standby power
        this.operationalPowerConsumption = 50.0; // 50W when active
        this.currentUsage = new EnergyUsage(basePowerConsumption, 0.0);
    }
    
    public void recordOperation(DeviceOperation operation, DeviceOperationResult result) {
        if (!result.isSuccess()) {
            return;
        }
        
        double powerUsage = basePowerConsumption;
        
        switch (operation.getType()) {
            case TURN_ON:
                powerUsage = operationalPowerConsumption;
                break;
            case TURN_OFF:
                powerUsage = basePowerConsumption;
                break;
            default:
                powerUsage = basePowerConsumption + 5.0; // Small increase for other operations
        }
        
        EnergyReading reading = new EnergyReading(
            LocalDateTime.now(),
            powerUsage,
            operation.getType().toString()
        );
        
        readings.add(reading);
        updateCurrentUsage(powerUsage);
        
        // Keep only last 1000 readings
        if (readings.size() > 1000) {
            readings.remove(0);
        }
    }
    
    private void updateCurrentUsage(double currentPower) {
        currentUsage.setCurrentPowerWatts(currentPower);
        
        // Calculate cumulative energy (simplified)
        double energyIncrement = currentPower / 1000.0 / 60.0; // Convert to kWh per minute
        currentUsage.setTotalEnergyKwh(currentUsage.getTotalEnergyKwh() + energyIncrement);
    }
    
    public EnergyUsage getCurrentUsage() {
        return currentUsage;
    }
    
    public List<EnergyReading> getRecentReadings(int count) {
        int size = readings.size();
        int fromIndex = Math.max(0, size - count);
        return new ArrayList<>(readings.subList(fromIndex, size));
    }
    
    public double getAveragePowerConsumption(int hours) {
        LocalDateTime cutoff = LocalDateTime.now().minusHours(hours);
        
        return readings.stream()
            .filter(reading -> reading.getTimestamp().isAfter(cutoff))
            .mapToDouble(EnergyReading::getPowerWatts)
            .average()
            .orElse(0.0);
    }
}

// Energy Reading
class EnergyReading {
    private LocalDateTime timestamp;
    private double powerWatts;
    private String operation;
    
    public EnergyReading(LocalDateTime timestamp, double powerWatts, String operation) {
        this.timestamp = timestamp;
        this.powerWatts = powerWatts;
        this.operation = operation;
    }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public double getPowerWatts() { return powerWatts; }
    public String getOperation() { return operation; }
}
