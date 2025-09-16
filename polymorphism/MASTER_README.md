## Polymorphism – Master Demo Outputs and Summaries

Below are concise project statements, how each demonstrates polymorphism, and example outputs captured from the demo classes. Some outputs include timestamps or pseudo-random values and may vary on each run; those are noted accordingly.

### 01. Shape Area Calculator
- **Statement**: Compute areas of different shapes and aggregate total.
- **Polymorphism**: `Shape` base class with `calculateArea()` overridden by `Circle`, `Rectangle`, and `Triangle`. One array of `Shape` processed uniformly.
- **Demo**: `ShapeDemo`
```text
Circle area: 28.27
Rectangle area: 20.00
Triangle area: 7.50
Total area: 55.77
```

### 02. Ride Pricing System
- **Statement**: Calculate fare using different ride pricing strategies.
- **Polymorphism**: `RidePricingStrategy` interface implemented by `CityRidePricing`, `AirportRidePricing`, `PremiumRidePricing`. One array of strategies invoked via common method.
- **Demo**: `RidePricingDemo`
```text
CityRidePricing price: Rs. 306.80
AirportRidePricing price: Rs. 308.75
PremiumRidePricing price: Rs. 564.20
```

### 03. Message Sender
- **Statement**: Send messages via Email, SMS, and Push through a common client.
- **Polymorphism**: `MessageSender` interface with `EmailSender`, `SMSSender`, `PushNotificationSender` implementations.
- **Demo**: `MessageClientDemo`
```text
[EMAIL] to=user@example.com, subject=Hello, body=This is a test message
[SMS] to=user@example.com, body=This is a test message
[PUSH] to=user@example.com, title=Hello, body=This is a test message
```

### 04. Document Exporter
- **Statement**: Export text content into various formats.
- **Polymorphism**: `DocumentExporter` interface with `PdfExporter`, `WordExporter`, `HtmlExporter` implementations.
- **Demo**: `DocumentExporterDemo`
```text
PdfExporter => %PDF-1.7
Hello Polymorphism
%%EOF
WordExporter => <word>Hello Polymorphism</word>
HtmlExporter => <html><body>Hello Polymorphism</body></html>
```

### 05. ML Model
- **Statement**: Generate predictions from different ML models for the same features.
- **Polymorphism**: `MLModel` interface with `LinearRegressionModel`, `RandomForestModel`, `NeuralNetworkModel`.
- **Demo**: `MLDemo` (predictions depend on simple math and seeded randomness)
```text
LinearRegressionModel prediction: <value>
RandomForestModel prediction: <value>
NeuralNetworkModel prediction: <value>
```

### 06. E-commerce Discounts
- **Statement**: Apply different discount strategies to an order.
- **Polymorphism**: `DiscountStrategy` interface with `NoDiscount`, `PercentageDiscount`, `BulkDiscount`.
- **Demo**: `EcommerceDiscountDemo`
```text
NoDiscount => Rs. 1000.00
PercentageDiscount => Rs. 850.00
BulkDiscount => Rs. 930.00
```

### 07. Payment Gateway
- **Statement**: Process various payment methods via a unified payment flow.
- **Polymorphism**: Abstract `Payment` with `CreditCardPayment`, `UPIPayment`, `PayPalPayment`, `CryptoPayment` overriding `processPayment()`.
- **Demo**: `PaymentDemo`
```text
[CARD] Charged 199.99 USD to card **** **** **** 4242
[UPI] Collected 499.0 INR from alice@upi
[PAYPAL] Received 59.0 EUR from bob@example.com
[CRYPTO] Transferred 0.015 BTC to bc1qxyz...
```

### 08. Logger Framework
- **Statement**: Log messages to different destinations through a common API.
- **Polymorphism**: `Logger` interface with `ConsoleLogger`, `FileLogger`, `CloudLogger`.
- **Demo**: `LoggerDemo` (timestamps vary)
```text
[CONSOLE] Polymorphic logging with ConsoleLogger
[FILE] <timestamp> | Polymorphic logging with FileLogger
[CLOUD] Polymorphic logging with CloudLogger
```

### 09. Smart Home Device
- **Statement**: Control different smart devices via a common interface.
- **Polymorphism**: `Device` interface with `SmartLight`, `SmartFan`, `SmartThermostat`.
- **Demo**: `SmartHomeDeviceDemo`
```text
Turning on SmartLight
SmartLight is ON
Turning on SmartFan
SmartFan is ON at speed 1
Turning on SmartThermostat
SmartThermostat is ON at 24.0°C
Turning off SmartLight
SmartLight is OFF
Turning off SmartFan
SmartFan is OFF
Turning off SmartThermostat
SmartThermostat is OFF
```

### 10. Bank Account Operations
- **Statement**: Apply different banking operations to account sessions.
- **Polymorphism**: `BankOperation` interface with `DepositOperation`, `WithdrawOperation`, `TransferOperation`.
- **Demo**: `BankOperationsDemo`
```text
Alice opening balance: 500.0
After deposit: 650.0
After withdraw: 550.0
After transfer to Bob: Alice=350.0, Bob=400.0
```

### 11. Job Scheduler
- **Statement**: Schedule and cancel jobs using various scheduling strategies.
- **Polymorphism**: `JobScheduler` interface with `CronScheduler`, `RealtimeScheduler`, `PriorityScheduler`.
- **Demo**: `JobSchedulerDemo`
```text
[CRON] Scheduled job job-1 using cron expression
[CRON] Cancelled job job-1
[REALTIME] Enqueued job job-2 for immediate execution
[REALTIME] Cancelled job job-2
[PRIORITY] Added job job-3 based on priority
[PRIORITY] Cancelled job job-3
```

### 12. Analytics Engine
- **Statement**: Aggregate a dataset using multiple aggregation strategies.
- **Polymorphism**: `Aggregator` interface with `SumAggregator`, `AverageAggregator`, `MaxAggregator`.
- **Demo**: `AnalyticsDemo`
```text
SumAggregator: 54.10
AverageAggregator: 10.82
MaxAggregator: 15.20
```

### 13. Game Character Attack
- **Statement**: Compute damage using different attack styles.
- **Polymorphism**: `AttackStrategy` interface with `MeleeAttack`, `RangedAttack`, `MagicAttack`.
- **Demo**: `GameCharacterAttackDemo`
```text
MeleeAttack deals 25 damage
RangedAttack deals 18 damage
MagicAttack deals 35 damage
```

### 14. Vehicle Navigation System
- **Statement**: Produce routes for different vehicle types.
- **Polymorphism**: `Navigator` interface with `CarNavigator`, `BikeNavigator`, `TruckNavigator`.
- **Demo**: `NavigationDemo`
```text
CarNavigator: Car route from Station to Airport via highways
BikeNavigator: Bike route from Station to Airport via local streets
TruckNavigator: Truck route from Station to Airport avoiding low bridges
```

### 15. E-Learning Assessment Grader
- **Statement**: Grade different kinds of submissions with a common API.
- **Polymorphism**: `AssessmentGrader` interface with `MultipleChoiceGrader`, `EssayGrader`, `CodingGrader`.
- **Demo**: `AssessmentGraderDemo`
```text
MC: 66.66666666666667
Essay: 10.0
Code: 62.0
```


