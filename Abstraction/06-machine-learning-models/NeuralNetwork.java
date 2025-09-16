package abstraction.machinelearning;

import java.util.*;

/**
 * Neural Network implementation of MLModel
 */
public class NeuralNetwork extends MLModel {
    
    private List<Layer> layers;
    private double learningRate;
    private int epochs;
    private int batchSize;
    private ActivationFunction activationFunction;
    private double dropoutRate;
    
    public NeuralNetwork(String modelId, String modelName, Map<String, Object> configuration) {
        super(modelId, modelName, ModelType.NEURAL_NETWORK, configuration);
        
        this.learningRate = (Double) configuration.getOrDefault("learning_rate", 0.001);
        this.epochs = (Integer) configuration.getOrDefault("epochs", 100);
        this.batchSize = (Integer) configuration.getOrDefault("batch_size", 32);
        this.activationFunction = ActivationFunction.valueOf(
            configuration.getOrDefault("activation", "RELU").toString()
        );
        this.dropoutRate = (Double) configuration.getOrDefault("dropout_rate", 0.2);
    }
    
    @Override
    protected void initialize() {
        System.out.println("Initializing Neural Network model: " + modelName);
        this.layers = new ArrayList<>();
        System.out.println("Neural Network initialization complete");
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
            System.out.println("Starting Neural Network training...");
            
            MLDataset dataset = data.getProcessedDataset();
            List<DataPoint> points = dataset.getDataPoints();
            
            if (points.isEmpty()) {
                return TrainingResult.failure("No training data available");
            }
            
            // Build network architecture
            buildNetwork(points);
            
            // Training loop
            double bestLoss = Double.MAX_VALUE;
            int epochsCompleted = 0;
            
            for (int epoch = 0; epoch < epochs; epoch++) {
                // Shuffle data
                Collections.shuffle(points);
                
                double epochLoss = 0.0;
                int numBatches = 0;
                
                // Mini-batch training
                for (int i = 0; i < points.size(); i += batchSize) {
                    int endIdx = Math.min(i + batchSize, points.size());
                    List<DataPoint> batch = points.subList(i, endIdx);
                    
                    double batchLoss = trainBatch(batch);
                    epochLoss += batchLoss;
                    numBatches++;
                }
                
                epochLoss /= numBatches;
                epochsCompleted = epoch + 1;
                
                if (epoch % 10 == 0) {
                    System.out.println("Epoch " + epoch + ", Loss: " + String.format("%.6f", epochLoss));
                }
                
                // Early stopping check
                if (epochLoss < bestLoss) {
                    bestLoss = epochLoss;
                } else if (epoch > 20 && epochLoss > bestLoss * 1.1) {
                    System.out.println("Early stopping at epoch " + epoch);
                    break;
                }
            }
            
            // Calculate final metrics
            ModelMetrics metrics = calculateNetworkMetrics(points);
            
            TrainingResult result = TrainingResult.success("Neural Network training completed", metrics);
            result.setEpochsCompleted(epochsCompleted);
            result.setConverged(epochsCompleted < epochs);
            result.setFinalLoss(bestLoss);
            
            Map<String, Object> trainingData = new HashMap<>();
            trainingData.put("layers", layers.size());
            trainingData.put("total_parameters", getTotalParameters());
            trainingData.put("training_samples", points.size());
            trainingData.put("final_learning_rate", learningRate);
            result.setTrainingData(trainingData);
            
            return result;
            
        } catch (Exception e) {
            return TrainingResult.failure("Neural Network training failed: " + e.getMessage());
        }
    }
    
    @Override
    protected PredictionResult performCorePrediction(PredictionRequest request, ProcessedData data) {
        try {
            if (layers.isEmpty()) {
                return PredictionResult.failure("Model not trained");
            }
            
            MLDataset dataset = data.getProcessedDataset();
            List<DataPoint> points = dataset.getDataPoints();
            
            List<Prediction> predictions = new ArrayList<>();
            
            for (DataPoint point : points) {
                double[] input = extractFeatureVector(point);
                double[] output = forwardPass(input, false); // No dropout during prediction
                
                Object predictedValue = interpretOutput(output);
                double confidence = calculateConfidence(output);
                
                Prediction prediction = new Prediction(predictedValue, confidence);
                
                // Add probabilities for classification
                if (modelType == ModelType.CLASSIFICATION && request.isIncludeProbabilities()) {
                    Map<String, Double> probabilities = new HashMap<>();
                    for (int i = 0; i < output.length; i++) {
                        probabilities.put("class_" + i, output[i]);
                    }
                    prediction.setProbabilities(probabilities);
                }
                
                predictions.add(prediction);
            }
            
            return PredictionResult.success("Neural Network predictions completed", predictions);
            
        } catch (Exception e) {
            return PredictionResult.failure("Neural Network prediction failed: " + e.getMessage());
        }
    }
    
    @Override
    protected EvaluationResult performModelEvaluation(EvaluationRequest request, ProcessedData data) {
        try {
            if (layers.isEmpty()) {
                return EvaluationResult.failure("Model not trained for evaluation");
            }
            
            MLDataset dataset = data.getProcessedDataset();
            List<DataPoint> points = dataset.getDataPoints();
            
            List<Object> predictions = new ArrayList<>();
            List<Object> actuals = new ArrayList<>();
            
            for (DataPoint point : points) {
                double[] input = extractFeatureVector(point);
                double[] output = forwardPass(input, false);
                
                Object prediction = interpretOutput(output);
                predictions.add(prediction);
                actuals.add(point.getTarget());
            }
            
            ModelMetrics metrics = calculateEvaluationMetrics(predictions, actuals);
            
            EvaluationResult result = EvaluationResult.success("Neural Network evaluation completed", metrics);
            
            StringBuilder report = new StringBuilder();
            report.append("Neural Network Evaluation Report\n");
            report.append("=================================\n");
            report.append("Test Samples: ").append(points.size()).append("\n");
            report.append("Network Architecture: ").append(getArchitectureString()).append("\n");
            report.append("Total Parameters: ").append(getTotalParameters()).append("\n");
            report.append("Accuracy: ").append(String.format("%.4f", metrics.getAccuracy())).append("\n");
            report.append("Loss: ").append(String.format("%.6f", metrics.getLoss())).append("\n");
            
            result.setEvaluationReport(report.toString());
            
            return result;
            
        } catch (Exception e) {
            return EvaluationResult.failure("Neural Network evaluation failed: " + e.getMessage());
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
        
        if (parameters.containsKey("epochs")) {
            int ep = (Integer) parameters.get("epochs");
            if (ep <= 0) {
                issues.add("Epochs must be positive");
            }
        }
        
        if (parameters.containsKey("batch_size")) {
            int bs = (Integer) parameters.get("batch_size");
            if (bs <= 0) {
                issues.add("Batch size must be positive");
            }
        }
        
        if (parameters.containsKey("dropout_rate")) {
            double dr = (Double) parameters.get("dropout_rate");
            if (dr < 0 || dr >= 1) {
                issues.add("Dropout rate must be between 0 and 1");
            }
        }
        
        if (!issues.isEmpty()) {
            return ValidationResult.failure("Parameter validation failed: " + String.join(", ", issues));
        }
        
        return ValidationResult.success("Neural Network parameters validated");
    }
    
    private void buildNetwork(List<DataPoint> points) {
        layers.clear();
        
        int inputSize = points.get(0).getFeatures().size();
        
        // Input layer
        layers.add(new Layer(inputSize, inputSize, ActivationFunction.LINEAR));
        
        // Hidden layers
        @SuppressWarnings("unchecked")
        List<Integer> hiddenSizes = (List<Integer>) configuration.getOrDefault("hidden_layers", Arrays.asList(64, 32));
        
        int prevSize = inputSize;
        for (int hiddenSize : hiddenSizes) {
            layers.add(new Layer(prevSize, hiddenSize, activationFunction));
            prevSize = hiddenSize;
        }
        
        // Output layer
        int outputSize = determineOutputSize(points);
        layers.add(new Layer(prevSize, outputSize, ActivationFunction.SIGMOID));
        
        System.out.println("Built network with architecture: " + getArchitectureString());
    }
    
    private int determineOutputSize(List<DataPoint> points) {
        if (modelType == ModelType.CLASSIFICATION) {
            Set<Object> uniqueClasses = new HashSet<>();
            for (DataPoint point : points) {
                uniqueClasses.add(point.getTarget());
            }
            return Math.max(1, uniqueClasses.size());
        } else {
            return 1; // Regression
        }
    }
    
    private double trainBatch(List<DataPoint> batch) {
        double totalLoss = 0.0;
        
        for (DataPoint point : batch) {
            double[] input = extractFeatureVector(point);
            double[] target = createTargetVector(point);
            
            // Forward pass
            double[] output = forwardPass(input, true); // With dropout
            
            // Calculate loss
            double loss = calculateLoss(output, target);
            totalLoss += loss;
            
            // Backward pass
            backwardPass(target, output);
        }
        
        // Update weights
        updateWeights();
        
        return totalLoss / batch.size();
    }
    
    private double[] forwardPass(double[] input, boolean training) {
        double[] current = input.clone();
        
        for (int i = 0; i < layers.size(); i++) {
            Layer layer = layers.get(i);
            current = layer.forward(current);
            
            // Apply dropout during training (except output layer)
            if (training && i < layers.size() - 1 && dropoutRate > 0) {
                current = applyDropout(current, dropoutRate);
            }
        }
        
        return current;
    }
    
    private void backwardPass(double[] target, double[] output) {
        // Calculate output layer error
        double[] error = new double[output.length];
        for (int i = 0; i < output.length; i++) {
            error[i] = target[i] - output[i];
        }
        
        // Backpropagate error through layers
        for (int i = layers.size() - 1; i >= 0; i--) {
            Layer layer = layers.get(i);
            error = layer.backward(error, learningRate);
        }
    }
    
    private void updateWeights() {
        for (Layer layer : layers) {
            layer.updateWeights();
        }
    }
    
    private double[] extractFeatureVector(DataPoint point) {
        Map<String, Object> features = point.getFeatures();
        double[] vector = new double[features.size()];
        
        int i = 0;
        for (Object value : features.values()) {
            vector[i++] = value instanceof Number ? ((Number) value).doubleValue() : 0.0;
        }
        
        return vector;
    }
    
    private double[] createTargetVector(DataPoint point) {
        if (modelType == ModelType.CLASSIFICATION) {
            // One-hot encoding for classification
            int outputSize = layers.get(layers.size() - 1).getOutputSize();
            double[] target = new double[outputSize];
            
            // Simplified: assume target is class index
            int classIndex = point.getTarget() instanceof Number ? 
                ((Number) point.getTarget()).intValue() : 0;
            
            if (classIndex >= 0 && classIndex < outputSize) {
                target[classIndex] = 1.0;
            }
            
            return target;
        } else {
            // Regression
            double targetValue = point.getTarget() instanceof Number ? 
                ((Number) point.getTarget()).doubleValue() : 0.0;
            return new double[]{targetValue};
        }
    }
    
    private Object interpretOutput(double[] output) {
        if (modelType == ModelType.CLASSIFICATION) {
            // Find class with highest probability
            int maxIndex = 0;
            for (int i = 1; i < output.length; i++) {
                if (output[i] > output[maxIndex]) {
                    maxIndex = i;
                }
            }
            return maxIndex;
        } else {
            return output[0]; // Regression
        }
    }
    
    private double calculateConfidence(double[] output) {
        if (modelType == ModelType.CLASSIFICATION) {
            // Max probability as confidence
            double maxProb = 0.0;
            for (double prob : output) {
                maxProb = Math.max(maxProb, prob);
            }
            return maxProb;
        } else {
            return 0.8 + Math.random() * 0.19; // Simplified confidence for regression
        }
    }
    
    private double calculateLoss(double[] output, double[] target) {
        double loss = 0.0;
        
        for (int i = 0; i < output.length; i++) {
            double error = target[i] - output[i];
            loss += error * error;
        }
        
        return loss / output.length;
    }
    
    private double[] applyDropout(double[] input, double rate) {
        double[] output = new double[input.length];
        Random random = new Random();
        
        for (int i = 0; i < input.length; i++) {
            if (random.nextDouble() > rate) {
                output[i] = input[i] / (1.0 - rate); // Scale to maintain expected value
            } else {
                output[i] = 0.0;
            }
        }
        
        return output;
    }
    
    private ModelMetrics calculateNetworkMetrics(List<DataPoint> points) {
        int correct = 0;
        double totalLoss = 0.0;
        
        for (DataPoint point : points) {
            double[] input = extractFeatureVector(point);
            double[] output = forwardPass(input, false);
            double[] target = createTargetVector(point);
            
            totalLoss += calculateLoss(output, target);
            
            Object prediction = interpretOutput(output);
            if (prediction.equals(point.getTarget())) {
                correct++;
            }
        }
        
        double accuracy = (double) correct / points.size();
        double avgLoss = totalLoss / points.size();
        
        ModelMetrics metrics = new ModelMetrics();
        metrics.setAccuracy(accuracy);
        metrics.setLoss(avgLoss);
        metrics.setPrecision(accuracy); // Simplified
        metrics.setRecall(accuracy); // Simplified
        metrics.setF1Score(accuracy); // Simplified
        
        return metrics;
    }
    
    private ModelMetrics calculateEvaluationMetrics(List<Object> predictions, List<Object> actuals) {
        int correct = 0;
        
        for (int i = 0; i < predictions.size(); i++) {
            if (predictions.get(i).equals(actuals.get(i))) {
                correct++;
            }
        }
        
        double accuracy = (double) correct / predictions.size();
        
        ModelMetrics metrics = new ModelMetrics();
        metrics.setAccuracy(accuracy);
        metrics.setLoss(1.0 - accuracy);
        metrics.setPrecision(accuracy);
        metrics.setRecall(accuracy);
        metrics.setF1Score(accuracy);
        
        return metrics;
    }
    
    private String getArchitectureString() {
        StringBuilder arch = new StringBuilder();
        for (int i = 0; i < layers.size(); i++) {
            if (i > 0) arch.append("->");
            arch.append(layers.get(i).getOutputSize());
        }
        return arch.toString();
    }
    
    private int getTotalParameters() {
        int total = 0;
        for (Layer layer : layers) {
            total += layer.getParameterCount();
        }
        return total;
    }
    
    // Neural Network specific getters
    public List<Layer> getLayers() { return new ArrayList<>(layers); }
    public double getLearningRate() { return learningRate; }
    public int getEpochs() { return epochs; }
    public int getBatchSize() { return batchSize; }
    public ActivationFunction getActivationFunction() { return activationFunction; }
    public double getDropoutRate() { return dropoutRate; }
}

// Activation function enumeration
enum ActivationFunction {
    LINEAR, SIGMOID, RELU, TANH
}

// Neural network layer class
class Layer {
    private int inputSize;
    private int outputSize;
    private ActivationFunction activation;
    private double[][] weights;
    private double[] biases;
    private double[] lastInput;
    private double[] lastOutput;
    private Random random;
    
    public Layer(int inputSize, int outputSize, ActivationFunction activation) {
        this.inputSize = inputSize;
        this.outputSize = outputSize;
        this.activation = activation;
        this.random = new Random();
        
        initializeWeights();
    }
    
    private void initializeWeights() {
        weights = new double[outputSize][inputSize];
        biases = new double[outputSize];
        
        // Xavier initialization
        double scale = Math.sqrt(2.0 / (inputSize + outputSize));
        
        for (int i = 0; i < outputSize; i++) {
            for (int j = 0; j < inputSize; j++) {
                weights[i][j] = random.nextGaussian() * scale;
            }
            biases[i] = 0.0;
        }
    }
    
    public double[] forward(double[] input) {
        this.lastInput = input.clone();
        double[] output = new double[outputSize];
        
        // Linear transformation
        for (int i = 0; i < outputSize; i++) {
            output[i] = biases[i];
            for (int j = 0; j < inputSize; j++) {
                output[i] += weights[i][j] * input[j];
            }
        }
        
        // Apply activation function
        for (int i = 0; i < outputSize; i++) {
            output[i] = applyActivation(output[i]);
        }
        
        this.lastOutput = output.clone();
        return output;
    }
    
    public double[] backward(double[] error, double learningRate) {
        // Calculate gradients
        double[] inputError = new double[inputSize];
        
        // Apply activation derivative
        for (int i = 0; i < outputSize; i++) {
            error[i] *= activationDerivative(lastOutput[i]);
        }
        
        // Calculate input error for previous layer
        for (int j = 0; j < inputSize; j++) {
            for (int i = 0; i < outputSize; i++) {
                inputError[j] += error[i] * weights[i][j];
            }
        }
        
        // Update weights and biases
        for (int i = 0; i < outputSize; i++) {
            biases[i] += learningRate * error[i];
            for (int j = 0; j < inputSize; j++) {
                weights[i][j] += learningRate * error[i] * lastInput[j];
            }
        }
        
        return inputError;
    }
    
    public void updateWeights() {
        // Weights are updated in backward pass
    }
    
    private double applyActivation(double x) {
        switch (activation) {
            case SIGMOID:
                return 1.0 / (1.0 + Math.exp(-x));
            case RELU:
                return Math.max(0, x);
            case TANH:
                return Math.tanh(x);
            case LINEAR:
            default:
                return x;
        }
    }
    
    private double activationDerivative(double output) {
        switch (activation) {
            case SIGMOID:
                return output * (1.0 - output);
            case RELU:
                return output > 0 ? 1.0 : 0.0;
            case TANH:
                return 1.0 - output * output;
            case LINEAR:
            default:
                return 1.0;
        }
    }
    
    public int getInputSize() { return inputSize; }
    public int getOutputSize() { return outputSize; }
    public int getParameterCount() { return outputSize * (inputSize + 1); }
    public ActivationFunction getActivation() { return activation; }
}
