package abstraction.machinelearning;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Abstract ML Model class defining the template for all machine learning models
 * Uses Template Method pattern to enforce common ML workflow while allowing customization
 */
public abstract class MLModel {
    
    protected String modelId;
    protected String modelName;
    protected ModelType modelType;
    protected ModelState currentState;
    protected DataProcessor dataProcessor;
    protected ModelValidator validator;
    protected PerformanceMonitor performanceMonitor;
    protected HyperparameterTuner tuner;
    protected Map<String, Object> configuration;
    protected ModelMetrics currentMetrics;
    
    public MLModel(String modelId, String modelName, ModelType type, Map<String, Object> configuration) {
        this.modelId = modelId;
        this.modelName = modelName;
        this.modelType = type;
        this.configuration = configuration;
        this.currentState = ModelState.INITIALIZED;
        
        this.dataProcessor = createDataProcessor();
        this.validator = new ModelValidator(type);
        this.performanceMonitor = new PerformanceMonitor();
        this.tuner = createHyperparameterTuner();
        
        initialize();
    }
    
    // Template method for model training
    public final CompletableFuture<TrainingResult> train(TrainingRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Pre-training validation
                ValidationResult preValidation = performPreTrainingValidation(request);
                if (!preValidation.isSuccess()) {
                    return TrainingResult.failure("Pre-training validation failed: " + preValidation.getMessage());
                }
                
                currentState = ModelState.TRAINING;
                
                // Data preprocessing
                DataProcessingResult dataResult = preprocessData(request.getTrainingData());
                if (!dataResult.isSuccess()) {
                    currentState = ModelState.ERROR;
                    return TrainingResult.failure("Data preprocessing failed: " + dataResult.getMessage());
                }
                
                // Hyperparameter tuning (if enabled)
                if (request.isHyperparameterTuningEnabled()) {
                    TuningResult tuningResult = performHyperparameterTuning(request);
                    if (!tuningResult.isSuccess()) {
                        currentState = ModelState.ERROR;
                        return TrainingResult.failure("Hyperparameter tuning failed: " + tuningResult.getMessage());
                    }
                }
                
                // Core training (abstract method)
                TrainingResult coreResult = performCoreTraining(request, dataResult.getProcessedData());
                if (!coreResult.isSuccess()) {
                    currentState = ModelState.ERROR;
                    return coreResult;
                }
                
                // Post-training validation
                ValidationResult postValidation = performPostTrainingValidation(coreResult);
                if (!postValidation.isSuccess()) {
                    currentState = ModelState.ERROR;
                    return TrainingResult.failure("Post-training validation failed: " + postValidation.getMessage());
                }
                
                // Update model metrics
                updateModelMetrics(coreResult);
                
                currentState = ModelState.TRAINED;
                return coreResult;
                
            } catch (Exception e) {
                currentState = ModelState.ERROR;
                return TrainingResult.failure("Training failed: " + e.getMessage());
            }
        });
    }
    
    // Template method for model prediction
    public final CompletableFuture<PredictionResult> predict(PredictionRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (currentState != ModelState.TRAINED && currentState != ModelState.DEPLOYED) {
                    return PredictionResult.failure("Model not trained. Current state: " + currentState);
                }
                
                // Input validation
                ValidationResult inputValidation = validateInput(request.getInputData());
                if (!inputValidation.isSuccess()) {
                    return PredictionResult.failure("Input validation failed: " + inputValidation.getMessage());
                }
                
                // Preprocess input data
                DataProcessingResult inputProcessing = preprocessInput(request.getInputData());
                if (!inputProcessing.isSuccess()) {
                    return PredictionResult.failure("Input preprocessing failed: " + inputProcessing.getMessage());
                }
                
                // Core prediction (abstract method)
                PredictionResult predictionResult = performCorePrediction(request, inputProcessing.getProcessedData());
                
                // Post-process predictions
                if (predictionResult.isSuccess()) {
                    PredictionResult postProcessedResult = postprocessPredictions(predictionResult);
                    
                    // Update performance metrics
                    performanceMonitor.recordPrediction(request, postProcessedResult);
                    
                    return postProcessedResult;
                }
                
                return predictionResult;
                
            } catch (Exception e) {
                return PredictionResult.failure("Prediction failed: " + e.getMessage());
            }
        });
    }
    
    // Template method for model evaluation
    public final CompletableFuture<EvaluationResult> evaluate(EvaluationRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (currentState != ModelState.TRAINED && currentState != ModelState.DEPLOYED) {
                    return EvaluationResult.failure("Model not trained for evaluation");
                }
                
                // Prepare test data
                DataProcessingResult testDataResult = preprocessData(request.getTestData());
                if (!testDataResult.isSuccess()) {
                    return EvaluationResult.failure("Test data preprocessing failed: " + testDataResult.getMessage());
                }
                
                // Perform model-specific evaluation
                EvaluationResult evaluationResult = performModelEvaluation(request, testDataResult.getProcessedData());
                
                // Update model metrics with evaluation results
                if (evaluationResult.isSuccess()) {
                    updateModelMetricsFromEvaluation(evaluationResult);
                }
                
                return evaluationResult;
                
            } catch (Exception e) {
                return EvaluationResult.failure("Evaluation failed: " + e.getMessage());
            }
        });
    }
    
    // Abstract methods to be implemented by concrete models
    protected abstract void initialize();
    protected abstract DataProcessor createDataProcessor();
    protected abstract HyperparameterTuner createHyperparameterTuner();
    protected abstract TrainingResult performCoreTraining(TrainingRequest request, ProcessedData data);
    protected abstract PredictionResult performCorePrediction(PredictionRequest request, ProcessedData data);
    protected abstract EvaluationResult performModelEvaluation(EvaluationRequest request, ProcessedData data);
    protected abstract ValidationResult validateModelSpecificParameters(Map<String, Object> parameters);
    
    // Concrete methods with default implementations
    protected ValidationResult performPreTrainingValidation(TrainingRequest request) {
        try {
            // Check training data
            if (request.getTrainingData() == null || request.getTrainingData().isEmpty()) {
                return ValidationResult.failure("Training data is empty");
            }
            
            // Check data quality
            DataQualityResult qualityResult = dataProcessor.checkDataQuality(request.getTrainingData());
            if (!qualityResult.isAcceptable()) {
                return ValidationResult.failure("Data quality issues: " + qualityResult.getIssues());
            }
            
            // Validate model-specific parameters
            ValidationResult paramValidation = validateModelSpecificParameters(request.getParameters());
            if (!paramValidation.isSuccess()) {
                return paramValidation;
            }
            
            return ValidationResult.success("Pre-training validation passed");
            
        } catch (Exception e) {
            return ValidationResult.failure("Pre-training validation error: " + e.getMessage());
        }
    }
    
    protected ValidationResult performPostTrainingValidation(TrainingResult trainingResult) {
        try {
            // Check if training converged
            if (!trainingResult.hasConverged()) {
                return ValidationResult.failure("Training did not converge");
            }
            
            // Check model performance metrics
            ModelMetrics metrics = trainingResult.getMetrics();
            if (metrics == null) {
                return ValidationResult.failure("No performance metrics available");
            }
            
            // Validate metrics against thresholds
            ValidationResult metricsValidation = validator.validateMetrics(metrics, modelType);
            if (!metricsValidation.isSuccess()) {
                return metricsValidation;
            }
            
            return ValidationResult.success("Post-training validation passed");
            
        } catch (Exception e) {
            return ValidationResult.failure("Post-training validation error: " + e.getMessage());
        }
    }
    
    protected DataProcessingResult preprocessData(MLDataset dataset) {
        try {
            return dataProcessor.processDataset(dataset);
        } catch (Exception e) {
            return DataProcessingResult.failure("Data preprocessing failed: " + e.getMessage());
        }
    }
    
    protected DataProcessingResult preprocessInput(MLDataset input) {
        try {
            return dataProcessor.processInput(input);
        } catch (Exception e) {
            return DataProcessingResult.failure("Input preprocessing failed: " + e.getMessage());
        }
    }
    
    protected ValidationResult validateInput(MLDataset input) {
        try {
            return validator.validateInput(input, modelType);
        } catch (Exception e) {
            return ValidationResult.failure("Input validation error: " + e.getMessage());
        }
    }
    
    protected TuningResult performHyperparameterTuning(TrainingRequest request) {
        try {
            return tuner.tuneHyperparameters(request, this);
        } catch (Exception e) {
            return TuningResult.failure("Hyperparameter tuning failed: " + e.getMessage());
        }
    }
    
    protected PredictionResult postprocessPredictions(PredictionResult rawResult) {
        try {
            return dataProcessor.postprocessPredictions(rawResult);
        } catch (Exception e) {
            return PredictionResult.failure("Prediction postprocessing failed: " + e.getMessage());
        }
    }
    
    protected void updateModelMetrics(TrainingResult trainingResult) {
        this.currentMetrics = trainingResult.getMetrics();
        performanceMonitor.updateTrainingMetrics(trainingResult);
    }
    
    protected void updateModelMetricsFromEvaluation(EvaluationResult evaluationResult) {
        performanceMonitor.updateEvaluationMetrics(evaluationResult);
    }
    
    // Model deployment and management
    public CompletableFuture<DeploymentResult> deploy(DeploymentRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (currentState != ModelState.TRAINED) {
                    return DeploymentResult.failure("Model must be trained before deployment");
                }
                
                // Perform deployment-specific validation
                ValidationResult deployValidation = validator.validateForDeployment(this, request);
                if (!deployValidation.isSuccess()) {
                    return DeploymentResult.failure("Deployment validation failed: " + deployValidation.getMessage());
                }
                
                // Deploy model
                DeploymentResult result = performDeployment(request);
                
                if (result.isSuccess()) {
                    currentState = ModelState.DEPLOYED;
                }
                
                return result;
                
            } catch (Exception e) {
                return DeploymentResult.failure("Deployment failed: " + e.getMessage());
            }
        });
    }
    
    protected DeploymentResult performDeployment(DeploymentRequest request) {
        // Default deployment implementation
        return DeploymentResult.success("Model deployed successfully", request.getDeploymentEndpoint());
    }
    
    public void stop() {
        try {
            currentState = ModelState.STOPPED;
            performanceMonitor.stop();
        } catch (Exception e) {
            currentState = ModelState.ERROR;
            throw new MLException("Failed to stop model: " + e.getMessage());
        }
    }
    
    // Getters and status methods
    public String getModelId() { return modelId; }
    public String getModelName() { return modelName; }
    public ModelType getModelType() { return modelType; }
    public ModelState getCurrentState() { return currentState; }
    public ModelMetrics getCurrentMetrics() { return currentMetrics; }
    
    public ModelStatus getStatus() {
        return new ModelStatus(
            modelId,
            modelName,
            modelType,
            currentState,
            currentMetrics,
            performanceMonitor.getRecentPerformance(),
            LocalDateTime.now()
        );
    }
    
    public DataProcessor getDataProcessor() { return dataProcessor; }
    public ModelValidator getValidator() { return validator; }
    public PerformanceMonitor getPerformanceMonitor() { return performanceMonitor; }
    public HyperparameterTuner getTuner() { return tuner; }
    
    // Configuration
    public boolean supportsFeature(String feature) {
        return configuration.containsKey(feature) && 
               Boolean.TRUE.equals(configuration.get(feature));
    }
    
    public Object getConfigurationValue(String key) {
        return configuration.get(key);
    }
}
