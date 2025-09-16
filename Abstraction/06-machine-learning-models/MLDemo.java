package abstraction.machinelearning;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Demo class showcasing polymorphic usage of different ML models
 * Demonstrates how client code remains unchanged regardless of model type
 */
public class MLDemo {
    
    public static void main(String[] args) {
        System.out.println("=== Machine Learning Models Abstraction Demo ===\n");
        
        // Create different ML models
        MLModel[] models = createMLModels();
        
        // Test each model polymorphically
        for (MLModel model : models) {
            System.out.println("Testing ML Model: " + model.getClass().getSimpleName());
            System.out.println("Model ID: " + model.getModelId());
            System.out.println("Model Name: " + model.getModelName());
            System.out.println("Model Type: " + model.getModelType());
            System.out.println("Current State: " + model.getCurrentState());
            
            try {
                // Test complete ML workflow using template method
                testMLWorkflow(model);
                
                // Test model-specific features
                testModelSpecificFeatures(model);
                
                // Display model status
                displayModelStatus(model);
                
            } catch (Exception e) {
                System.err.println("Error testing model: " + e.getMessage());
            }
            
            System.out.println("-".repeat(80));
        }
        
        System.out.println("\n=== Demo completed ===");
    }
    
    private static MLModel[] createMLModels() {
        // Linear Regression configuration
        Map<String, Object> lrConfig = new HashMap<>();
        lrConfig.put("learning_rate", 0.01);
        lrConfig.put("max_iterations", 1000);
        lrConfig.put("tolerance", 1e-6);
        
        // Random Forest configuration
        Map<String, Object> rfConfig = new HashMap<>();
        rfConfig.put("num_trees", 50);
        rfConfig.put("max_depth", 8);
        rfConfig.put("min_samples_leaf", 2);
        rfConfig.put("feature_subset_ratio", 0.8);
        
        // Neural Network configuration
        Map<String, Object> nnConfig = new HashMap<>();
        nnConfig.put("learning_rate", 0.001);
        nnConfig.put("epochs", 50);
        nnConfig.put("batch_size", 32);
        nnConfig.put("activation", "RELU");
        nnConfig.put("dropout_rate", 0.2);
        nnConfig.put("hidden_layers", Arrays.asList(64, 32));
        
        return new MLModel[] {
            new LinearRegression("lr_001", "House Price Predictor", lrConfig),
            new RandomForest("rf_001", "Customer Classifier", rfConfig),
            new NeuralNetwork("nn_001", "Image Recognizer", nnConfig)
        };
    }
    
    private static void testMLWorkflow(MLModel model) {
        try {
            System.out.println("\n1. Testing ML training workflow...");
            
            // Create training request
            TrainingRequest trainingRequest = createTrainingRequest(model);
            
            // Train model using template method
            System.out.println("   Starting model training...");
            var trainingFuture = model.train(trainingRequest);
            
            TrainingResult trainingResult = trainingFuture.get();
            
            if (trainingResult.isSuccess()) {
                System.out.println("   ✓ Training completed successfully");
                System.out.println("   Message: " + trainingResult.getMessage());
                System.out.println("   Epochs Completed: " + trainingResult.getEpochsCompleted());
                System.out.println("   Converged: " + trainingResult.hasConverged());
                System.out.println("   Final Loss: " + String.format("%.6f", trainingResult.getFinalLoss()));
                
                if (trainingResult.getMetrics() != null) {
                    ModelMetrics metrics = trainingResult.getMetrics();
                    System.out.println("   Training Metrics:");
                    System.out.println("     Accuracy: " + String.format("%.4f", metrics.getAccuracy()));
                    System.out.println("     Loss: " + String.format("%.6f", metrics.getLoss()));
                    if (metrics.getPrecision() > 0) {
                        System.out.println("     Precision: " + String.format("%.4f", metrics.getPrecision()));
                        System.out.println("     Recall: " + String.format("%.4f", metrics.getRecall()));
                        System.out.println("     F1-Score: " + String.format("%.4f", metrics.getF1Score()));
                    }
                }
                
                // Test prediction after training
                testPrediction(model);
                
                // Test evaluation
                testEvaluation(model);
                
                // Test deployment
                testDeployment(model);
                
            } else {
                System.out.println("   ✗ Training failed: " + trainingResult.getMessage());
            }
            
        } catch (Exception e) {
            System.err.println("   ML workflow test failed: " + e.getMessage());
        }
    }
    
    private static TrainingRequest createTrainingRequest(MLModel model) {
        // Create synthetic dataset based on model type
        MLDataset trainingData = createSyntheticDataset(model.getModelType(), 1000);
        
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("validation_split", 0.2);
        parameters.put("random_seed", 42);
        
        TrainingRequest request = new TrainingRequest(
            "train_" + System.currentTimeMillis(),
            trainingData,
            parameters,
            "demo_user"
        );
        
        request.setHyperparameterTuningEnabled(false); // Disable for demo speed
        request.setMaxEpochs(50);
        
        return request;
    }
    
    private static MLDataset createSyntheticDataset(ModelType modelType, int size) {
        List<DataPoint> dataPoints = new ArrayList<>();
        List<String> features = Arrays.asList("feature1", "feature2", "feature3", "feature4");
        Random random = new Random(42);
        
        for (int i = 0; i < size; i++) {
            Map<String, Object> featureMap = new HashMap<>();
            
            // Generate random features
            for (String feature : features) {
                featureMap.put(feature, random.nextGaussian() * 10 + 50); // Mean=50, std=10
            }
            
            // Generate target based on model type
            Object target;
            if (modelType == ModelType.CLASSIFICATION) {
                // Binary classification based on feature1
                double feature1 = (Double) featureMap.get("feature1");
                target = feature1 > 50 ? 1 : 0;
            } else {
                // Regression: linear combination of features with noise
                double sum = 0.0;
                for (String feature : features) {
                    sum += (Double) featureMap.get(feature);
                }
                target = sum / features.size() + random.nextGaussian() * 5; // Add noise
            }
            
            DataPoint point = new DataPoint("point_" + i, featureMap, target);
            dataPoints.add(point);
        }
        
        return new MLDataset("synthetic_" + modelType.name().toLowerCase(), dataPoints, features, DatasetType.TABULAR);
    }
    
    private static void testPrediction(MLModel model) {
        try {
            System.out.println("\n2. Testing prediction...");
            
            // Create prediction dataset
            MLDataset predictionData = createSyntheticDataset(model.getModelType(), 10);
            
            PredictionRequest predictionRequest = new PredictionRequest(
                "pred_" + System.currentTimeMillis(),
                predictionData,
                "demo_user"
            );
            predictionRequest.setIncludeProbabilities(true);
            
            var predictionFuture = model.predict(predictionRequest);
            PredictionResult predictionResult = predictionFuture.get();
            
            if (predictionResult.isSuccess()) {
                System.out.println("   ✓ Predictions completed successfully");
                System.out.println("   Number of predictions: " + predictionResult.getPredictions().size());
                
                // Show first few predictions
                List<Prediction> predictions = predictionResult.getPredictions();
                for (int i = 0; i < Math.min(3, predictions.size()); i++) {
                    Prediction pred = predictions.get(i);
                    System.out.println("   Prediction " + (i + 1) + ": " + pred.getPredictedValue() + 
                                     " (confidence: " + String.format("%.3f", pred.getConfidence()) + ")");
                    
                    if (pred.getProbabilities() != null && !pred.getProbabilities().isEmpty()) {
                        System.out.println("     Probabilities: " + pred.getProbabilities());
                    }
                }
            } else {
                System.out.println("   ✗ Prediction failed: " + predictionResult.getMessage());
            }
            
        } catch (Exception e) {
            System.err.println("   Prediction test failed: " + e.getMessage());
        }
    }
    
    private static void testEvaluation(MLModel model) {
        try {
            System.out.println("\n3. Testing evaluation...");
            
            // Create test dataset
            MLDataset testData = createSyntheticDataset(model.getModelType(), 200);
            
            List<String> metricsToCompute = Arrays.asList("accuracy", "precision", "recall", "f1_score");
            
            EvaluationRequest evaluationRequest = new EvaluationRequest(
                "eval_" + System.currentTimeMillis(),
                testData,
                metricsToCompute,
                "demo_user"
            );
            
            var evaluationFuture = model.evaluate(evaluationRequest);
            EvaluationResult evaluationResult = evaluationFuture.get();
            
            if (evaluationResult.isSuccess()) {
                System.out.println("   ✓ Evaluation completed successfully");
                
                ModelMetrics metrics = evaluationResult.getEvaluationMetrics();
                if (metrics != null) {
                    System.out.println("   Evaluation Metrics:");
                    System.out.println("     Accuracy: " + String.format("%.4f", metrics.getAccuracy()));
                    System.out.println("     Loss: " + String.format("%.6f", metrics.getLoss()));
                    if (metrics.getPrecision() > 0) {
                        System.out.println("     Precision: " + String.format("%.4f", metrics.getPrecision()));
                        System.out.println("     Recall: " + String.format("%.4f", metrics.getRecall()));
                        System.out.println("     F1-Score: " + String.format("%.4f", metrics.getF1Score()));
                    }
                }
                
                if (evaluationResult.getEvaluationReport() != null) {
                    System.out.println("   Evaluation Report Generated: " + 
                                     evaluationResult.getEvaluationReport().split("\n").length + " lines");
                }
            } else {
                System.out.println("   ✗ Evaluation failed: " + evaluationResult.getMessage());
            }
            
        } catch (Exception e) {
            System.err.println("   Evaluation test failed: " + e.getMessage());
        }
    }
    
    private static void testDeployment(MLModel model) {
        try {
            System.out.println("\n4. Testing deployment...");
            
            DeploymentRequest deploymentRequest = new DeploymentRequest(
                "deploy_" + System.currentTimeMillis(),
                "http://localhost:8080/api/predict",
                DeploymentType.REST_API,
                "demo_user"
            );
            
            Map<String, Object> deployConfig = new HashMap<>();
            deployConfig.put("max_requests_per_second", 100);
            deployConfig.put("timeout_seconds", 30);
            deploymentRequest.setDeploymentConfig(deployConfig);
            
            var deploymentFuture = model.deploy(deploymentRequest);
            DeploymentResult deploymentResult = deploymentFuture.get();
            
            if (deploymentResult.isSuccess()) {
                System.out.println("   ✓ Deployment completed successfully");
                System.out.println("   Endpoint: " + deploymentResult.getDeploymentEndpoint());
                System.out.println("   Deployment Time: " + deploymentResult.getDeploymentTime());
            } else {
                System.out.println("   ✗ Deployment failed: " + deploymentResult.getMessage());
            }
            
        } catch (Exception e) {
            System.err.println("   Deployment test failed: " + e.getMessage());
        }
    }
    
    private static void testModelSpecificFeatures(MLModel model) {
        System.out.println("\n5. Testing model-specific features...");
        
        try {
            if (model instanceof LinearRegression) {
                testLinearRegressionFeatures((LinearRegression) model);
            } else if (model instanceof RandomForest) {
                testRandomForestFeatures((RandomForest) model);
            } else if (model instanceof NeuralNetwork) {
                testNeuralNetworkFeatures((NeuralNetwork) model);
            }
        } catch (Exception e) {
            System.err.println("   Model-specific test failed: " + e.getMessage());
        }
    }
    
    private static void testLinearRegressionFeatures(LinearRegression lr) {
        System.out.println("   Testing Linear Regression specific features:");
        System.out.println("   - Learning Rate: " + lr.getLearningRate());
        System.out.println("   - Max Iterations: " + lr.getMaxIterations());
        System.out.println("   - Tolerance: " + lr.getTolerance());
        
        if (lr.getWeights() != null) {
            System.out.println("   - Weights: " + Arrays.toString(lr.getWeights()));
            System.out.println("   - Bias: " + String.format("%.6f", lr.getBias()));
        }
        
        System.out.println("   ✓ Linear Regression features tested");
    }
    
    private static void testRandomForestFeatures(RandomForest rf) {
        System.out.println("   Testing Random Forest specific features:");
        System.out.println("   - Number of Trees: " + rf.getNumTrees());
        System.out.println("   - Max Depth: " + rf.getMaxDepth());
        System.out.println("   - Min Samples Leaf: " + rf.getMinSamplesLeaf());
        System.out.println("   - Feature Subset Ratio: " + rf.getFeatureSubsetRatio());
        System.out.println("   - Trained Trees: " + rf.getTrees().size());
        System.out.println("   ✓ Random Forest features tested");
    }
    
    private static void testNeuralNetworkFeatures(NeuralNetwork nn) {
        System.out.println("   Testing Neural Network specific features:");
        System.out.println("   - Learning Rate: " + nn.getLearningRate());
        System.out.println("   - Epochs: " + nn.getEpochs());
        System.out.println("   - Batch Size: " + nn.getBatchSize());
        System.out.println("   - Activation Function: " + nn.getActivationFunction());
        System.out.println("   - Dropout Rate: " + nn.getDropoutRate());
        System.out.println("   - Number of Layers: " + nn.getLayers().size());
        System.out.println("   ✓ Neural Network features tested");
    }
    
    private static void displayModelStatus(MLModel model) {
        System.out.println("\n6. Model Status Information:");
        
        ModelStatus status = model.getStatus();
        System.out.println("   Model ID: " + status.getModelId());
        System.out.println("   Model Name: " + status.getModelName());
        System.out.println("   Model Type: " + status.getModelType());
        System.out.println("   Current State: " + status.getCurrentState());
        System.out.println("   Status Time: " + status.getStatusTime());
        
        // Display current metrics
        if (status.getCurrentMetrics() != null) {
            ModelMetrics metrics = status.getCurrentMetrics();
            System.out.println("   Current Metrics:");
            System.out.println("     Accuracy: " + String.format("%.4f", metrics.getAccuracy()));
            System.out.println("     Loss: " + String.format("%.6f", metrics.getLoss()));
            System.out.println("     Computed At: " + metrics.getComputedAt());
        }
        
        // Display performance data
        if (status.getRecentPerformance() != null) {
            PerformanceData perf = status.getRecentPerformance();
            System.out.println("   Performance Data:");
            System.out.println("     Total Predictions: " + perf.getTotalPredictions());
            System.out.println("     Average Response Time: " + String.format("%.2f", perf.getAverageResponseTime()) + " ms");
            System.out.println("     Throughput: " + String.format("%.2f", perf.getThroughput()) + " predictions/min");
            System.out.println("     Last Updated: " + perf.getLastUpdated());
        }
        
        // Display data processor information
        DataProcessor dataProcessor = model.getDataProcessor();
        System.out.println("   Data Processor: " + dataProcessor.getClass().getSimpleName());
        
        // Display validator information
        ModelValidator validator = model.getValidator();
        System.out.println("   Model Validator: Configured for " + model.getModelType());
        
        // Display performance monitor metrics
        PerformanceMonitor monitor = model.getPerformanceMonitor();
        Map<String, Double> allMetrics = monitor.getAllMetrics();
        if (!allMetrics.isEmpty()) {
            System.out.println("   All Performance Metrics:");
            allMetrics.entrySet().stream()
                .limit(5) // Show only first 5 metrics
                .forEach(entry -> 
                    System.out.println("     " + entry.getKey() + ": " + String.format("%.4f", entry.getValue())));
        }
        
        // Display hyperparameter tuner
        HyperparameterTuner tuner = model.getTuner();
        System.out.println("   Hyperparameter Tuner: " + tuner.getClass().getSimpleName());
        
        // Test configuration features
        System.out.println("   Configuration Features:");
        System.out.println("     Supports Hyperparameter Tuning: " + model.supportsFeature("hyperparameter_tuning"));
        System.out.println("     Supports Cross Validation: " + model.supportsFeature("cross_validation"));
        System.out.println("     Supports Feature Selection: " + model.supportsFeature("feature_selection"));
    }
}
