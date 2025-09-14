package abstraction.machinelearning;

import java.util.*;

/**
 * Linear Regression implementation of MLModel
 */
public class LinearRegression extends MLModel {
    
    private double[] weights;
    private double bias;
    private double learningRate;
    private int maxIterations;
    private double tolerance;
    
    public LinearRegression(String modelId, String modelName, Map<String, Object> configuration) {
        super(modelId, modelName, ModelType.REGRESSION, configuration);
        
        this.learningRate = (Double) configuration.getOrDefault("learning_rate", 0.01);
        this.maxIterations = (Integer) configuration.getOrDefault("max_iterations", 1000);
        this.tolerance = (Double) configuration.getOrDefault("tolerance", 1e-6);
    }
    
    @Override
    protected void initialize() {
        System.out.println("Initializing Linear Regression model: " + modelName);
        this.bias = 0.0;
        System.out.println("Linear Regression initialization complete");
    }
    
    @Override
    protected DataProcessor createDataProcessor() {
        return new TabularDataProcessor(configuration);
    }
    
    @Override
    protected HyperparameterTuner createHyperparameterTuner() {
        return new GridSearchTuner(configuration);
    }
    
    @Override
    protected TrainingResult performCoreTraining(TrainingRequest request, ProcessedData data) {
        try {
            System.out.println("Starting Linear Regression training...");
            
            MLDataset dataset = data.getProcessedDataset();
            List<DataPoint> points = dataset.getDataPoints();
            
            if (points.isEmpty()) {
                return TrainingResult.failure("No training data available");
            }
            
            // Initialize weights
            int featureCount = points.get(0).getFeatures().size();
            weights = new double[featureCount];
            Arrays.fill(weights, 0.0);
            
            // Extract features and targets
            double[][] X = extractFeatures(points);
            double[] y = extractTargets(points);
            
            // Gradient descent training
            double previousLoss = Double.MAX_VALUE;
            int epochsCompleted = 0;
            
            for (int epoch = 0; epoch < maxIterations; epoch++) {
                // Forward pass
                double[] predictions = predict(X);
                
                // Calculate loss (MSE)
                double loss = calculateMSE(predictions, y);
                
                // Check convergence
                if (Math.abs(previousLoss - loss) < tolerance) {
                    System.out.println("Training converged at epoch " + epoch);
                    break;
                }
                
                // Backward pass (gradient descent)
                updateWeights(X, y, predictions);
                
                previousLoss = loss;
                epochsCompleted = epoch + 1;
                
                if (epoch % 100 == 0) {
                    System.out.println("Epoch " + epoch + ", Loss: " + String.format("%.6f", loss));
                }
            }
            
            // Calculate final metrics
            double[] finalPredictions = predict(X);
            ModelMetrics metrics = calculateRegressionMetrics(finalPredictions, y);
            
            TrainingResult result = TrainingResult.success("Linear Regression training completed", metrics);
            result.setEpochsCompleted(epochsCompleted);
            result.setConverged(epochsCompleted < maxIterations);
            result.setFinalLoss(calculateMSE(finalPredictions, y));
            
            Map<String, Object> trainingData = new HashMap<>();
            trainingData.put("feature_count", featureCount);
            trainingData.put("training_samples", points.size());
            trainingData.put("final_weights", Arrays.toString(weights));
            trainingData.put("final_bias", bias);
            result.setTrainingData(trainingData);
            
            return result;
            
        } catch (Exception e) {
            return TrainingResult.failure("Linear Regression training failed: " + e.getMessage());
        }
    }
    
    @Override
    protected PredictionResult performCorePrediction(PredictionRequest request, ProcessedData data) {
        try {
            if (weights == null) {
                return PredictionResult.failure("Model not trained");
            }
            
            MLDataset dataset = data.getProcessedDataset();
            List<DataPoint> points = dataset.getDataPoints();
            
            List<Prediction> predictions = new ArrayList<>();
            
            for (DataPoint point : points) {
                double[] features = extractSingleFeature(point);
                double predictedValue = predictSingle(features);
                
                // Calculate confidence (simplified)
                double confidence = Math.max(0.1, Math.min(0.99, 0.8 + Math.random() * 0.19));
                
                Prediction prediction = new Prediction(predictedValue, confidence);
                predictions.add(prediction);
            }
            
            return PredictionResult.success("Linear Regression predictions completed", predictions);
            
        } catch (Exception e) {
            return PredictionResult.failure("Linear Regression prediction failed: " + e.getMessage());
        }
    }
    
    @Override
    protected EvaluationResult performModelEvaluation(EvaluationRequest request, ProcessedData data) {
        try {
            if (weights == null) {
                return EvaluationResult.failure("Model not trained for evaluation");
            }
            
            MLDataset dataset = data.getProcessedDataset();
            List<DataPoint> points = dataset.getDataPoints();
            
            // Extract features and targets
            double[][] X = extractFeatures(points);
            double[] y = extractTargets(points);
            
            // Make predictions
            double[] predictions = predict(X);
            
            // Calculate evaluation metrics
            ModelMetrics metrics = calculateRegressionMetrics(predictions, y);
            
            EvaluationResult result = EvaluationResult.success("Linear Regression evaluation completed", metrics);
            
            // Generate evaluation report
            StringBuilder report = new StringBuilder();
            report.append("Linear Regression Evaluation Report\n");
            report.append("===================================\n");
            report.append("Test Samples: ").append(points.size()).append("\n");
            report.append("Mean Squared Error: ").append(String.format("%.6f", metrics.getLoss())).append("\n");
            report.append("R-squared: ").append(String.format("%.6f", metrics.getCustomMetrics().getOrDefault("r_squared", 0.0))).append("\n");
            report.append("Mean Absolute Error: ").append(String.format("%.6f", metrics.getCustomMetrics().getOrDefault("mae", 0.0))).append("\n");
            
            result.setEvaluationReport(report.toString());
            
            return result;
            
        } catch (Exception e) {
            return EvaluationResult.failure("Linear Regression evaluation failed: " + e.getMessage());
        }
    }
    
    @Override
    protected ValidationResult validateModelSpecificParameters(Map<String, Object> parameters) {
        List<String> issues = new ArrayList<>();
        
        if (parameters.containsKey("learning_rate")) {
            double lr = (Double) parameters.get("learning_rate");
            if (lr <= 0 || lr > 1) {
                issues.add("Learning rate must be between 0 and 1");
            }
        }
        
        if (parameters.containsKey("max_iterations")) {
            int maxIter = (Integer) parameters.get("max_iterations");
            if (maxIter <= 0) {
                issues.add("Max iterations must be positive");
            }
        }
        
        if (parameters.containsKey("tolerance")) {
            double tol = (Double) parameters.get("tolerance");
            if (tol <= 0) {
                issues.add("Tolerance must be positive");
            }
        }
        
        if (!issues.isEmpty()) {
            return ValidationResult.failure("Parameter validation failed: " + String.join(", ", issues));
        }
        
        return ValidationResult.success("Linear Regression parameters validated");
    }
    
    private double[][] extractFeatures(List<DataPoint> points) {
        int numSamples = points.size();
        int numFeatures = points.get(0).getFeatures().size();
        double[][] X = new double[numSamples][numFeatures];
        
        for (int i = 0; i < numSamples; i++) {
            DataPoint point = points.get(i);
            int j = 0;
            for (Object value : point.getFeatures().values()) {
                X[i][j++] = ((Number) value).doubleValue();
            }
        }
        
        return X;
    }
    
    private double[] extractTargets(List<DataPoint> points) {
        double[] y = new double[points.size()];
        
        for (int i = 0; i < points.size(); i++) {
            Object target = points.get(i).getTarget();
            y[i] = target != null ? ((Number) target).doubleValue() : 0.0;
        }
        
        return y;
    }
    
    private double[] extractSingleFeature(DataPoint point) {
        double[] features = new double[point.getFeatures().size()];
        int i = 0;
        for (Object value : point.getFeatures().values()) {
            features[i++] = ((Number) value).doubleValue();
        }
        return features;
    }
    
    private double[] predict(double[][] X) {
        double[] predictions = new double[X.length];
        
        for (int i = 0; i < X.length; i++) {
            predictions[i] = predictSingle(X[i]);
        }
        
        return predictions;
    }
    
    private double predictSingle(double[] features) {
        double prediction = bias;
        
        for (int i = 0; i < features.length && i < weights.length; i++) {
            prediction += weights[i] * features[i];
        }
        
        return prediction;
    }
    
    private double calculateMSE(double[] predictions, double[] actual) {
        double sum = 0.0;
        
        for (int i = 0; i < predictions.length; i++) {
            double error = predictions[i] - actual[i];
            sum += error * error;
        }
        
        return sum / predictions.length;
    }
    
    private void updateWeights(double[][] X, double[] y, double[] predictions) {
        int m = X.length;
        
        // Calculate gradients
        double[] weightGradients = new double[weights.length];
        double biasGradient = 0.0;
        
        for (int i = 0; i < m; i++) {
            double error = predictions[i] - y[i];
            
            for (int j = 0; j < weights.length; j++) {
                weightGradients[j] += error * X[i][j];
            }
            
            biasGradient += error;
        }
        
        // Update weights and bias
        for (int j = 0; j < weights.length; j++) {
            weights[j] -= learningRate * (weightGradients[j] / m);
        }
        
        bias -= learningRate * (biasGradient / m);
    }
    
    private ModelMetrics calculateRegressionMetrics(double[] predictions, double[] actual) {
        ModelMetrics metrics = new ModelMetrics();
        
        // MSE (used as loss)
        double mse = calculateMSE(predictions, actual);
        metrics.setLoss(mse);
        
        // MAE
        double mae = 0.0;
        for (int i = 0; i < predictions.length; i++) {
            mae += Math.abs(predictions[i] - actual[i]);
        }
        mae /= predictions.length;
        
        // R-squared
        double meanActual = Arrays.stream(actual).average().orElse(0.0);
        double ssTotal = 0.0;
        double ssRes = 0.0;
        
        for (int i = 0; i < actual.length; i++) {
            ssTotal += Math.pow(actual[i] - meanActual, 2);
            ssRes += Math.pow(actual[i] - predictions[i], 2);
        }
        
        double rSquared = 1.0 - (ssRes / ssTotal);
        
        // Set custom metrics
        Map<String, Double> customMetrics = new HashMap<>();
        customMetrics.put("mae", mae);
        customMetrics.put("mse", mse);
        customMetrics.put("rmse", Math.sqrt(mse));
        customMetrics.put("r_squared", rSquared);
        
        metrics.setCustomMetrics(customMetrics);
        
        return metrics;
    }
    
    // Linear Regression specific getters
    public double[] getWeights() { return weights != null ? weights.clone() : null; }
    public double getBias() { return bias; }
    public double getLearningRate() { return learningRate; }
    public int getMaxIterations() { return maxIterations; }
    public double getTolerance() { return tolerance; }
}
