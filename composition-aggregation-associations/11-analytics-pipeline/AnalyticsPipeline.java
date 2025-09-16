package composition.analytics;

import java.util.*;
import java.util.concurrent.*;

/**
 * MAANG-Level Analytics Pipeline System using Composition
 * Demonstrates: Strategy Pattern, Chain of Responsibility, Observer Pattern, Command Pattern
 */
public class AnalyticsPipeline {
    private final String pipelineId;
    private final List<DataProcessor> processors;
    private final List<DataSink> sinks;
    private final List<PipelineEventListener> listeners;
    private DataSource dataSource;
    private ErrorHandler errorHandler;
    private MetricsCollector metricsCollector;
    private final ExecutorService processingExecutor;
    private final ScheduledExecutorService schedulerExecutor;
    private PipelineState state;
    private final Map<String, Object> configuration;
    private final long createdAt;
    
    public AnalyticsPipeline(String pipelineId) {
        this.pipelineId = pipelineId;
        this.processors = new ArrayList<>();
        this.sinks = new ArrayList<>();
        this.listeners = new ArrayList<>();
        this.errorHandler = new DefaultErrorHandler();
        this.metricsCollector = new DefaultMetricsCollector();
        this.processingExecutor = Executors.newFixedThreadPool(10);
        this.schedulerExecutor = Executors.newScheduledThreadPool(2);
        this.state = PipelineState.STOPPED;
        this.configuration = new ConcurrentHashMap<>();
        this.createdAt = System.currentTimeMillis();
    }
    
    // Runtime component swapping - core composition flexibility
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        notifyListeners("Data source updated: " + dataSource.getSourceName());
    }
    
    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
        notifyListeners("Error handler updated");
    }
    
    public void setMetricsCollector(MetricsCollector metricsCollector) {
        this.metricsCollector = metricsCollector;
        notifyListeners("Metrics collector updated");
    }
    
    // Processor chain management
    public void addProcessor(DataProcessor processor) {
        processors.add(processor);
        processor.initialize(configuration);
        notifyListeners("Processor added: " + processor.getProcessorName());
    }
    
    public void removeProcessor(DataProcessor processor) {
        if (processors.remove(processor)) {
            processor.cleanup();
            notifyListeners("Processor removed: " + processor.getProcessorName());
        }
    }
    
    public void insertProcessor(int index, DataProcessor processor) {
        processors.add(index, processor);
        processor.initialize(configuration);
        notifyListeners("Processor inserted at position " + index + ": " + processor.getProcessorName());
    }
    
    // Data sink management
    public void addSink(DataSink sink) {
        sinks.add(sink);
        sink.initialize(configuration);
        notifyListeners("Data sink added: " + sink.getSinkName());
    }
    
    public void removeSink(DataSink sink) {
        if (sinks.remove(sink)) {
            sink.cleanup();
            notifyListeners("Data sink removed: " + sink.getSinkName());
        }
    }
    
    // Pipeline lifecycle management
    public PipelineResult start() {
        if (state == PipelineState.RUNNING) {
            return new PipelineResult(false, "Pipeline is already running", null);
        }
        
        if (dataSource == null) {
            return new PipelineResult(false, "No data source configured", null);
        }
        
        if (sinks.isEmpty()) {
            return new PipelineResult(false, "No data sinks configured", null);
        }
        
        try {
            state = PipelineState.STARTING;
            notifyListeners("Pipeline starting...");
            
            // Initialize components
            dataSource.initialize(configuration);
            for (DataSink sink : sinks) {
                sink.initialize(configuration);
            }
            
            // Start data processing
            startDataProcessing();
            
            state = PipelineState.RUNNING;
            metricsCollector.recordPipelineStart();
            notifyListeners("Pipeline started successfully");
            
            return new PipelineResult(true, "Pipeline started", getPipelineStatus());
            
        } catch (Exception e) {
            state = PipelineState.ERROR;
            String errorMsg = "Failed to start pipeline: " + e.getMessage();
            errorHandler.handleError(new PipelineError(pipelineId, "STARTUP_ERROR", errorMsg, e));
            return new PipelineResult(false, errorMsg, null);
        }
    }
    
    public PipelineResult stop() {
        if (state == PipelineState.STOPPED) {
            return new PipelineResult(false, "Pipeline is already stopped", null);
        }
        
        try {
            state = PipelineState.STOPPING;
            notifyListeners("Pipeline stopping...");
            
            // Stop data source
            if (dataSource != null) {
                dataSource.stop();
            }
            
            // Cleanup processors
            for (DataProcessor processor : processors) {
                processor.cleanup();
            }
            
            // Cleanup sinks
            for (DataSink sink : sinks) {
                sink.cleanup();
            }
            
            state = PipelineState.STOPPED;
            metricsCollector.recordPipelineStop();
            notifyListeners("Pipeline stopped successfully");
            
            return new PipelineResult(true, "Pipeline stopped", getPipelineStatus());
            
        } catch (Exception e) {
            state = PipelineState.ERROR;
            String errorMsg = "Failed to stop pipeline: " + e.getMessage();
            errorHandler.handleError(new PipelineError(pipelineId, "SHUTDOWN_ERROR", errorMsg, e));
            return new PipelineResult(false, errorMsg, null);
        }
    }
    
    public PipelineResult pause() {
        if (state != PipelineState.RUNNING) {
            return new PipelineResult(false, "Pipeline is not running", null);
        }
        
        state = PipelineState.PAUSED;
        notifyListeners("Pipeline paused");
        return new PipelineResult(true, "Pipeline paused", getPipelineStatus());
    }
    
    public PipelineResult resume() {
        if (state != PipelineState.PAUSED) {
            return new PipelineResult(false, "Pipeline is not paused", null);
        }
        
        state = PipelineState.RUNNING;
        notifyListeners("Pipeline resumed");
        return new PipelineResult(true, "Pipeline resumed", getPipelineStatus());
    }
    
    private void startDataProcessing() {
        // Start continuous data processing
        schedulerExecutor.scheduleWithFixedDelay(() -> {
            if (state == PipelineState.RUNNING) {
                processDataBatch();
            }
        }, 0, 1, TimeUnit.SECONDS);
    }
    
    private void processDataBatch() {
        try {
            // Fetch data from source
            DataBatch batch = dataSource.fetchData();
            if (batch == null || batch.isEmpty()) {
                return;
            }
            
            metricsCollector.recordBatchReceived(batch.size());
            
            // Process data through pipeline
            CompletableFuture<DataBatch> processingFuture = CompletableFuture.supplyAsync(() -> {
                DataBatch currentBatch = batch;
                
                // Apply processors in sequence
                for (DataProcessor processor : processors) {
                    try {
                        ProcessingResult result = processor.process(currentBatch);
                        if (!result.isSuccess()) {
                            throw new ProcessingException("Processor failed: " + result.getErrorMessage());
                        }
                        currentBatch = result.getProcessedData();
                        metricsCollector.recordProcessorExecution(processor.getProcessorName(), result.getProcessingTime());
                    } catch (Exception e) {
                        errorHandler.handleError(new PipelineError(pipelineId, "PROCESSING_ERROR", 
                                                                 "Processor " + processor.getProcessorName() + " failed", e));
                        throw new ProcessingException("Processing failed", e);
                    }
                }
                
                return currentBatch;
                
            }, processingExecutor);
            
            // Send processed data to sinks
            processingFuture.thenAccept(processedBatch -> {
                for (DataSink sink : sinks) {
                    try {
                        SinkResult result = sink.write(processedBatch);
                        metricsCollector.recordSinkWrite(sink.getSinkName(), result.getRecordsWritten());
                        
                        if (!result.isSuccess()) {
                            errorHandler.handleError(new PipelineError(pipelineId, "SINK_ERROR", 
                                                                     "Sink " + sink.getSinkName() + " failed: " + result.getErrorMessage(), null));
                        }
                    } catch (Exception e) {
                        errorHandler.handleError(new PipelineError(pipelineId, "SINK_ERROR", 
                                                                 "Sink " + sink.getSinkName() + " failed", e));
                    }
                }
            }).exceptionally(throwable -> {
                errorHandler.handleError(new PipelineError(pipelineId, "PIPELINE_ERROR", 
                                                         "Pipeline processing failed", throwable));
                return null;
            });
            
        } catch (Exception e) {
            errorHandler.handleError(new PipelineError(pipelineId, "BATCH_ERROR", 
                                                     "Failed to process data batch", e));
        }
    }
    
    // Manual data processing for testing
    public ProcessingResult processData(DataRecord record) {
        if (state != PipelineState.RUNNING) {
            return new ProcessingResult(false, null, "Pipeline is not running", 0);
        }
        
        try {
            DataBatch batch = new DataBatch(Arrays.asList(record));
            
            // Process through pipeline
            for (DataProcessor processor : processors) {
                ProcessingResult result = processor.process(batch);
                if (!result.isSuccess()) {
                    return result;
                }
                batch = result.getProcessedData();
            }
            
            // Write to sinks
            for (DataSink sink : sinks) {
                SinkResult result = sink.write(batch);
                if (!result.isSuccess()) {
                    return new ProcessingResult(false, null, "Sink error: " + result.getErrorMessage(), 0);
                }
            }
            
            return new ProcessingResult(true, batch, "Processing successful", System.currentTimeMillis());
            
        } catch (Exception e) {
            return new ProcessingResult(false, null, "Processing failed: " + e.getMessage(), 0);
        }
    }
    
    // Configuration management
    public void setConfiguration(String key, Object value) {
        configuration.put(key, value);
        notifyListeners("Configuration updated: " + key);
    }
    
    public Object getConfiguration(String key) {
        return configuration.get(key);
    }
    
    // Pipeline monitoring and metrics
    public PipelineStatus getPipelineStatus() {
        PipelineMetrics metrics = metricsCollector.getMetrics();
        return new PipelineStatus(pipelineId, state, processors.size(), sinks.size(), 
                                metrics, System.currentTimeMillis() - createdAt);
    }
    
    public List<DataProcessor> getProcessors() {
        return new ArrayList<>(processors);
    }
    
    public List<DataSink> getSinks() {
        return new ArrayList<>(sinks);
    }
    
    // Health check
    public HealthCheckResult performHealthCheck() {
        List<String> issues = new ArrayList<>();
        
        // Check data source
        if (dataSource == null) {
            issues.add("No data source configured");
        } else if (!dataSource.isHealthy()) {
            issues.add("Data source is unhealthy");
        }
        
        // Check processors
        for (DataProcessor processor : processors) {
            if (!processor.isHealthy()) {
                issues.add("Processor unhealthy: " + processor.getProcessorName());
            }
        }
        
        // Check sinks
        for (DataSink sink : sinks) {
            if (!sink.isHealthy()) {
                issues.add("Sink unhealthy: " + sink.getSinkName());
            }
        }
        
        boolean healthy = issues.isEmpty();
        return new HealthCheckResult(healthy, healthy ? "Pipeline is healthy" : "Pipeline has issues", issues);
    }
    
    // Event handling
    public void addEventListener(PipelineEventListener listener) {
        listeners.add(listener);
    }
    
    public void removeEventListener(PipelineEventListener listener) {
        listeners.remove(listener);
    }
    
    private void notifyListeners(String message) {
        for (PipelineEventListener listener : listeners) {
            listener.onPipelineEvent(pipelineId, message, getPipelineStatus());
        }
    }
    
    public void displayPipelineStatus() {
        System.out.println("\n📊 Analytics Pipeline Status: " + pipelineId);
        System.out.println("State: " + state);
        System.out.println("Data Source: " + (dataSource != null ? dataSource.getSourceName() : "None"));
        System.out.println("Processors: " + processors.size());
        
        for (int i = 0; i < processors.size(); i++) {
            System.out.printf("   %d. %s\n", i + 1, processors.get(i).getProcessorName());
        }
        
        System.out.println("Data Sinks: " + sinks.size());
        for (DataSink sink : sinks) {
            System.out.printf("   • %s\n", sink.getSinkName());
        }
        
        PipelineMetrics metrics = metricsCollector.getMetrics();
        System.out.println("\nMetrics:");
        System.out.println("   Batches Processed: " + metrics.getBatchesProcessed());
        System.out.println("   Records Processed: " + metrics.getRecordsProcessed());
        System.out.println("   Errors: " + metrics.getErrorCount());
        System.out.printf("   Avg Processing Time: %.2fms\n", metrics.getAverageProcessingTime());
    }
    
    public void shutdown() {
        stop();
        
        processingExecutor.shutdown();
        schedulerExecutor.shutdown();
        
        try {
            if (!processingExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                processingExecutor.shutdownNow();
            }
            if (!schedulerExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                schedulerExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            processingExecutor.shutdownNow();
            schedulerExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        notifyListeners("Pipeline shutdown completed");
    }
    
    // Getters
    public String getPipelineId() { return pipelineId; }
    public PipelineState getState() { return state; }
    public DataSource getDataSource() { return dataSource; }
    public long getCreatedAt() { return createdAt; }
}
