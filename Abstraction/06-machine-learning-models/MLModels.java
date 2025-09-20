package abstraction.machinelearning;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Data models and enums for the ML abstraction system
 */

// Model type enumeration
enum ModelType {
    CLASSIFICATION, REGRESSION, CLUSTERING, NEURAL_NETWORK, DEEP_LEARNING, 
    NATURAL_LANGUAGE_PROCESSING, COMPUTER_VISION, REINFORCEMENT_LEARNING
}

// Model state enumeration
enum ModelState {
    INITIALIZED, TRAINING, TRAINED, EVALUATING, DEPLOYED, STOPPED, ERROR
}

// Training request class
class TrainingRequest {
    private String requestId;
    private MLDataset trainingData;
    private MLDataset validationData;
    private Map<String, Object> parameters;
    private boolean hyperparameterTuningEnabled;
    private int maxEpochs;
    private double learningRate;
    private String requestedBy;
    
    public TrainingRequest(String requestId, MLDataset trainingData, Map<String, Object> parameters, String requestedBy) {
        this.requestId = requestId;
        this.trainingData = trainingData;
        this.parameters = parameters;
        this.requestedBy = requestedBy;
        this.hyperparameterTuningEnabled = false;
        this.maxEpochs = 100;
        this.learningRate = 0.001;
    }
    
    // Getters and setters
    public String getRequestId() { return requestId; }
    public MLDataset getTrainingData() { return trainingData; }
    public MLDataset getValidationData() { return validationData; }
    public Map<String, Object> getParameters() { return parameters; }
    public boolean isHyperparameterTuningEnabled() { return hyperparameterTuningEnabled; }
    public int getMaxEpochs() { return maxEpochs; }
    public double getLearningRate() { return learningRate; }
    public String getRequestedBy() { return requestedBy; }
    
    public void setValidationData(MLDataset validationData) { this.validationData = validationData; }
    public void setHyperparameterTuningEnabled(boolean enabled) { this.hyperparameterTuningEnabled = enabled; }
    public void setMaxEpochs(int maxEpochs) { this.maxEpochs = maxEpochs; }
    public void setLearningRate(double learningRate) { this.learningRate = learningRate; }
}

// Prediction request class
class PredictionRequest {
    private String requestId;
    private MLDataset inputData;
    private boolean includeProbabilities;
    private boolean includeExplanations;
    private String requestedBy;
    
    public PredictionRequest(String requestId, MLDataset inputData, String requestedBy) {
        this.requestId = requestId;
        this.inputData = inputData;
        this.requestedBy = requestedBy;
        this.includeProbabilities = false;
        this.includeExplanations = false;
    }
    
    // Getters and setters
    public String getRequestId() { return requestId; }
    public MLDataset getInputData() { return inputData; }
    public boolean isIncludeProbabilities() { return includeProbabilities; }
    public boolean isIncludeExplanations() { return includeExplanations; }
    public String getRequestedBy() { return requestedBy; }
    
    public void setIncludeProbabilities(boolean includeProbabilities) { this.includeProbabilities = includeProbabilities; }
    public void setIncludeExplanations(boolean includeExplanations) { this.includeExplanations = includeExplanations; }
}

// Evaluation request class
class EvaluationRequest {
    private String requestId;
    private MLDataset testData;
    private List<String> metricsToCompute;
    private boolean generateReport;
    private String requestedBy;
    
    public EvaluationRequest(String requestId, MLDataset testData, List<String> metricsToCompute, String requestedBy) {
        this.requestId = requestId;
        this.testData = testData;
        this.metricsToCompute = metricsToCompute;
        this.requestedBy = requestedBy;
        this.generateReport = true;
    }
    
    // Getters and setters
    public String getRequestId() { return requestId; }
    public MLDataset getTestData() { return testData; }
    public List<String> getMetricsToCompute() { return metricsToCompute; }
    public boolean isGenerateReport() { return generateReport; }
    public String getRequestedBy() { return requestedBy; }
    
    public void setGenerateReport(boolean generateReport) { this.generateReport = generateReport; }
}

// Deployment request class
class DeploymentRequest {
    private String requestId;
    private String deploymentEndpoint;
    private DeploymentType deploymentType;
    private Map<String, Object> deploymentConfig;
    private String requestedBy;
    
    public DeploymentRequest(String requestId, String deploymentEndpoint, DeploymentType deploymentType, String requestedBy) {
        this.requestId = requestId;
        this.deploymentEndpoint = deploymentEndpoint;
        this.deploymentType = deploymentType;
        this.requestedBy = requestedBy;
    }
    
    // Getters and setters
    public String getRequestId() { return requestId; }
    public String getDeploymentEndpoint() { return deploymentEndpoint; }
    public DeploymentType getDeploymentType() { return deploymentType; }
    public Map<String, Object> getDeploymentConfig() { return deploymentConfig; }
    public String getRequestedBy() { return requestedBy; }
    
    public void setDeploymentConfig(Map<String, Object> deploymentConfig) { this.deploymentConfig = deploymentConfig; }
}

// Deployment type enumeration
enum DeploymentType {
    REST_API, BATCH_PROCESSING, STREAMING, EDGE_DEVICE, CLOUD_FUNCTION
}

// ML Dataset class
class MLDataset {
    private String datasetId;
    private List<DataPoint> dataPoints;
    private List<String> features;
    private String targetColumn;
    private DatasetType datasetType;
    private Map<String, Object> metadata;
    
    public MLDataset(String datasetId, List<DataPoint> dataPoints, List<String> features, DatasetType datasetType) {
        this.datasetId = datasetId;
        this.dataPoints = dataPoints;
        this.features = features;
        this.datasetType = datasetType;
    }
    
    public boolean isEmpty() {
        return dataPoints == null || dataPoints.isEmpty();
    }
    
    public int size() {
        return dataPoints != null ? dataPoints.size() : 0;
    }
    
    // Getters and setters
    public String getDatasetId() { return datasetId; }
    public List<DataPoint> getDataPoints() { return dataPoints; }
    public List<String> getFeatures() { return features; }
    public String getTargetColumn() { return targetColumn; }
    public DatasetType getDatasetType() { return datasetType; }
    public Map<String, Object> getMetadata() { return metadata; }
    
    public void setTargetColumn(String targetColumn) { this.targetColumn = targetColumn; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
}

// Dataset type enumeration
enum DatasetType {
    TABULAR, IMAGE, TEXT, TIME_SERIES, AUDIO, VIDEO
}

// Data point class
class DataPoint {
    private Map<String, Object> features;
    private Object target;
    private String dataPointId;
    
    public DataPoint(String dataPointId, Map<String, Object> features) {
        this.dataPointId = dataPointId;
        this.features = features;
    }
    
    public DataPoint(String dataPointId, Map<String, Object> features, Object target) {
        this.dataPointId = dataPointId;
        this.features = features;
        this.target = target;
    }
    
    // Getters and setters
    public String getDataPointId() { return dataPointId; }
    public Map<String, Object> getFeatures() { return features; }
    public Object getTarget() { return target; }
    
    public void setTarget(Object target) { this.target = target; }
}

// Processed data class
class ProcessedData {
    private MLDataset originalDataset;
    private MLDataset processedDataset;
    private Map<String, Object> transformations;
    private DataProcessingStatistics statistics;
    
    public ProcessedData(MLDataset originalDataset, MLDataset processedDataset, Map<String, Object> transformations) {
        this.originalDataset = originalDataset;
        this.processedDataset = processedDataset;
        this.transformations = transformations;
    }
    
    // Getters and setters
    public MLDataset getOriginalDataset() { return originalDataset; }
    public MLDataset getProcessedDataset() { return processedDataset; }
    public Map<String, Object> getTransformations() { return transformations; }
    public DataProcessingStatistics getStatistics() { return statistics; }
    
    public void setStatistics(DataProcessingStatistics statistics) { this.statistics = statistics; }
}

// Data processing statistics class
class DataProcessingStatistics {
    private int originalSize;
    private int processedSize;
    private int missingValuesHandled;
    private int outliersTreated;
    private Map<String, Object> featureStatistics;
    
    public DataProcessingStatistics(int originalSize, int processedSize) {
        this.originalSize = originalSize;
        this.processedSize = processedSize;
    }
    
    // Getters and setters
    public int getOriginalSize() { return originalSize; }
    public int getProcessedSize() { return processedSize; }
    public int getMissingValuesHandled() { return missingValuesHandled; }
    public int getOutliersTreated() { return outliersTreated; }
    public Map<String, Object> getFeatureStatistics() { return featureStatistics; }
    
    public void setMissingValuesHandled(int missingValuesHandled) { this.missingValuesHandled = missingValuesHandled; }
    public void setOutliersTreated(int outliersTreated) { this.outliersTreated = outliersTreated; }
    public void setFeatureStatistics(Map<String, Object> featureStatistics) { this.featureStatistics = featureStatistics; }
}

// Model metrics class
class ModelMetrics {
    private double accuracy;
    private double precision;
    private double recall;
    private double f1Score;
    private double loss;
    private Map<String, Double> customMetrics;
    private LocalDateTime computedAt;
    
    public ModelMetrics() {
        this.computedAt = LocalDateTime.now();
    }
    
    // Getters and setters
    public double getAccuracy() { return accuracy; }
    public double getPrecision() { return precision; }
    public double getRecall() { return recall; }
    public double getF1Score() { return f1Score; }
    public double getLoss() { return loss; }
    public Map<String, Double> getCustomMetrics() { return customMetrics; }
    public LocalDateTime getComputedAt() { return computedAt; }
    
    public void setAccuracy(double accuracy) { this.accuracy = accuracy; }
    public void setPrecision(double precision) { this.precision = precision; }
    public void setRecall(double recall) { this.recall = recall; }
    public void setF1Score(double f1Score) { this.f1Score = f1Score; }
    public void setLoss(double loss) { this.loss = loss; }
    public void setCustomMetrics(Map<String, Double> customMetrics) { this.customMetrics = customMetrics; }
}

// Training result class
class TrainingResult {
    private boolean success;
    private String message;
    private ModelMetrics metrics;
    private int epochsCompleted;
    private boolean converged;
    private double finalLoss;
    private LocalDateTime trainingTime;
    private Map<String, Object> trainingData;
    
    private TrainingResult(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.trainingTime = LocalDateTime.now();
    }
    
    public static TrainingResult success(String message, ModelMetrics metrics) {
        TrainingResult result = new TrainingResult(true, message);
        result.metrics = metrics;
        result.converged = true;
        return result;
    }
    
    public static TrainingResult failure(String message) {
        return new TrainingResult(false, message);
    }
    
    // Getters and setters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public ModelMetrics getMetrics() { return metrics; }
    public int getEpochsCompleted() { return epochsCompleted; }
    public boolean hasConverged() { return converged; }
    public double getFinalLoss() { return finalLoss; }
    public LocalDateTime getTrainingTime() { return trainingTime; }
    public Map<String, Object> getTrainingData() { return trainingData; }
    
    public void setEpochsCompleted(int epochsCompleted) { this.epochsCompleted = epochsCompleted; }
    public void setConverged(boolean converged) { this.converged = converged; }
    public void setFinalLoss(double finalLoss) { this.finalLoss = finalLoss; }
    public void setTrainingData(Map<String, Object> trainingData) { this.trainingData = trainingData; }
}

// Prediction result class
class PredictionResult {
    private boolean success;
    private String message;
    private List<Prediction> predictions;
    private LocalDateTime predictionTime;
    
    private PredictionResult(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.predictionTime = LocalDateTime.now();
    }
    
    public static PredictionResult success(String message, List<Prediction> predictions) {
        PredictionResult result = new PredictionResult(true, message);
        result.predictions = predictions;
        return result;
    }
    
    public static PredictionResult failure(String message) {
        return new PredictionResult(false, message);
    }
    
    // Getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public List<Prediction> getPredictions() { return predictions; }
    public LocalDateTime getPredictionTime() { return predictionTime; }
}

// Prediction class
class Prediction {
    private Object predictedValue;
    private double confidence;
    private Map<String, Double> probabilities;
    private String explanation;
    
    public Prediction(Object predictedValue, double confidence) {
        this.predictedValue = predictedValue;
        this.confidence = confidence;
    }
    
    // Getters and setters
    public Object getPredictedValue() { return predictedValue; }
    public double getConfidence() { return confidence; }
    public Map<String, Double> getProbabilities() { return probabilities; }
    public String getExplanation() { return explanation; }
    
    public void setProbabilities(Map<String, Double> probabilities) { this.probabilities = probabilities; }
    public void setExplanation(String explanation) { this.explanation = explanation; }
}

// Evaluation result class
class EvaluationResult {
    private boolean success;
    private String message;
    private ModelMetrics evaluationMetrics;
    private String evaluationReport;
    private LocalDateTime evaluationTime;
    
    private EvaluationResult(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.evaluationTime = LocalDateTime.now();
    }
    
    public static EvaluationResult success(String message, ModelMetrics metrics) {
        EvaluationResult result = new EvaluationResult(true, message);
        result.evaluationMetrics = metrics;
        return result;
    }
    
    public static EvaluationResult failure(String message) {
        return new EvaluationResult(false, message);
    }
    
    // Getters and setters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public ModelMetrics getEvaluationMetrics() { return evaluationMetrics; }
    public String getEvaluationReport() { return evaluationReport; }
    public LocalDateTime getEvaluationTime() { return evaluationTime; }
    
    public void setEvaluationReport(String evaluationReport) { this.evaluationReport = evaluationReport; }
}

// Deployment result class
class DeploymentResult {
    private boolean success;
    private String message;
    private String deploymentEndpoint;
    private LocalDateTime deploymentTime;
    
    private DeploymentResult(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.deploymentTime = LocalDateTime.now();
    }
    
    public static DeploymentResult success(String message, String endpoint) {
        DeploymentResult result = new DeploymentResult(true, message);
        result.deploymentEndpoint = endpoint;
        return result;
    }
    
    public static DeploymentResult failure(String message) {
        return new DeploymentResult(false, message);
    }
    
    // Getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public String getDeploymentEndpoint() { return deploymentEndpoint; }
    public LocalDateTime getDeploymentTime() { return deploymentTime; }
}

// Model status class
class ModelStatus {
    private String modelId;
    private String modelName;
    private ModelType modelType;
    private ModelState currentState;
    private ModelMetrics currentMetrics;
    private PerformanceData recentPerformance;
    private LocalDateTime statusTime;
    
    public ModelStatus(String modelId, String modelName, ModelType modelType, ModelState currentState,
                      ModelMetrics currentMetrics, PerformanceData recentPerformance, LocalDateTime statusTime) {
        this.modelId = modelId;
        this.modelName = modelName;
        this.modelType = modelType;
        this.currentState = currentState;
        this.currentMetrics = currentMetrics;
        this.recentPerformance = recentPerformance;
        this.statusTime = statusTime;
    }
    
    // Getters
    public String getModelId() { return modelId; }
    public String getModelName() { return modelName; }
    public ModelType getModelType() { return modelType; }
    public ModelState getCurrentState() { return currentState; }
    public ModelMetrics getCurrentMetrics() { return currentMetrics; }
    public PerformanceData getRecentPerformance() { return recentPerformance; }
    public LocalDateTime getStatusTime() { return statusTime; }
}

// Performance data class
class PerformanceData {
    private double averageResponseTime;
    private int totalPredictions;
    private double throughput;
    private LocalDateTime lastUpdated;
    
    public PerformanceData(double averageResponseTime, int totalPredictions, double throughput) {
        this.averageResponseTime = averageResponseTime;
        this.totalPredictions = totalPredictions;
        this.throughput = throughput;
        this.lastUpdated = LocalDateTime.now();
    }
    
    // Getters
    public double getAverageResponseTime() { return averageResponseTime; }
    public int getTotalPredictions() { return totalPredictions; }
    public double getThroughput() { return throughput; }
    public LocalDateTime getLastUpdated() { return lastUpdated; }
}

// Result classes for various operations
class ValidationResult {
    private boolean success;
    private String message;
    
    private ValidationResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public static ValidationResult success(String message) {
        return new ValidationResult(true, message);
    }
    
    public static ValidationResult failure(String message) {
        return new ValidationResult(false, message);
    }
    
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
}

class DataProcessingResult {
    private boolean success;
    private String message;
    private ProcessedData processedData;
    
    private DataProcessingResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public static DataProcessingResult success(String message, ProcessedData data) {
        DataProcessingResult result = new DataProcessingResult(true, message);
        result.processedData = data;
        return result;
    }
    
    public static DataProcessingResult failure(String message) {
        return new DataProcessingResult(false, message);
    }
    
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public ProcessedData getProcessedData() { return processedData; }
}

class DataQualityResult {
    private boolean acceptable;
    private List<String> issues;
    private double qualityScore;
    
    public DataQualityResult(boolean acceptable, List<String> issues, double qualityScore) {
        this.acceptable = acceptable;
        this.issues = issues;
        this.qualityScore = qualityScore;
    }
    
    public boolean isAcceptable() { return acceptable; }
    public List<String> getIssues() { return issues; }
    public double getQualityScore() { return qualityScore; }
}

class TuningResult {
    private boolean success;
    private String message;
    private Map<String, Object> bestParameters;
    private double bestScore;
    
    private TuningResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public static TuningResult success(String message, Map<String, Object> bestParams, double bestScore) {
        TuningResult result = new TuningResult(true, message);
        result.bestParameters = bestParams;
        result.bestScore = bestScore;
        return result;
    }
    
    public static TuningResult failure(String message) {
        return new TuningResult(false, message);
    }
    
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public Map<String, Object> getBestParameters() { return bestParameters; }
    public double getBestScore() { return bestScore; }
}

// Custom exception for ML operations
class MLException extends RuntimeException {
    public MLException(String message) {
        super(message);
    }
    
    public MLException(String message, Throwable cause) {
        super(message, cause);
    }
}
