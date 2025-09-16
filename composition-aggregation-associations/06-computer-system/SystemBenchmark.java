package composition.computer;

/**
 * System Benchmark results
 */
public class SystemBenchmark {
    private final double cpuScore;
    private final double gpuScore;
    private final double storageScore;
    private final double memoryScore;
    
    public SystemBenchmark(double cpuScore, double gpuScore, double storageScore, double memoryScore) {
        this.cpuScore = cpuScore;
        this.gpuScore = gpuScore;
        this.storageScore = storageScore;
        this.memoryScore = memoryScore;
    }
    
    public double getOverallScore() {
        return (cpuScore + gpuScore + storageScore + memoryScore) / 4.0;
    }
    
    public void displayResults() {
        System.out.println("\n=== Benchmark Results ===");
        System.out.printf("CPU Score: %.2f\n", cpuScore);
        System.out.printf("GPU Score: %.2f\n", gpuScore);
        System.out.printf("Storage Score: %.2f\n", storageScore);
        System.out.printf("Memory Score: %.2f\n", memoryScore);
        System.out.printf("Overall Score: %.2f\n", getOverallScore());
        
        String rating = getPerformanceRating();
        System.out.println("Performance Rating: " + rating);
    }
    
    private String getPerformanceRating() {
        double overall = getOverallScore();
        if (overall >= 90) return "Excellent";
        if (overall >= 80) return "Very Good";
        if (overall >= 70) return "Good";
        if (overall >= 60) return "Fair";
        return "Poor";
    }
    
    public double getCpuScore() { return cpuScore; }
    public double getGpuScore() { return gpuScore; }
    public double getStorageScore() { return storageScore; }
    public double getMemoryScore() { return memoryScore; }
}
