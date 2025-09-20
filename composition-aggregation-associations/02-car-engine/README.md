# 🚗 Car Engine System - Learning Guide

## 🎯 What You'll Learn

### Core OOP Relationships
- **Composition**: `Car` → `Engine` (Strong ownership - Engine is part of Car)
- **Composition**: `Engine` → `FuelSystem` (Fuel system integral to engine)
- **Association**: `Car` ↔ `Driver` (Driver uses car - both exist independently)

### Enterprise Patterns
- **Strategy Pattern**: Different engine types (Electric, Petrol, Hybrid)
- **State Pattern**: Engine states (Off, Idle, Running, Low Energy)
- **Observer Pattern**: Performance monitoring and alerts
- **Factory Pattern**: Engine creation and management

## 🚀 Key Learning Objectives

1. **Component Design**: Understanding part-whole relationships in complex systems
2. **Runtime Flexibility**: Dynamic engine swapping and behavior changes
3. **Performance Monitoring**: Real-time metrics and system health tracking
4. **Energy Management**: Different fuel types and efficiency optimization
5. **Fault Tolerance**: Handling low energy scenarios and emergency situations

## 🔧 How to Run

```bash
cd "02-car-engine"
javac *.java
java CarEngineDemo
```

## 📊 Expected Output

```
=== MAANG-Level Car Engine System Demo ===

🚗 Car: Tesla Model S
🔧 Engine: Petrol Engine
⛽ Fuel Level: 100.0%
🚀 Status: Ready

--- Initial Setup: Petrol Engine ---
🚗 Car Status:
  - Model: Tesla Model S
  - Engine: Petrol Engine
  - Fuel Level: 100.0%
  - Current Speed: 0.0 km/h
  - Status: Ready

--- Testing Petrol Engine Performance ---
✅ Engine started successfully
🚀 Accelerating to 50.0 km/h
📊 Current Speed: 50.0 km/h
⛽ Fuel Consumption: 8.5 L/100km
🚀 Accelerating to 100.0 km/h
📊 Current Speed: 100.0 km/h
⛽ Fuel Consumption: 12.3 L/100km
🚀 Accelerating to 150.0 km/h
📊 Current Speed: 150.0 km/h
⛽ Fuel Consumption: 18.7 L/100km

--- Runtime Engine Swap: Petrol → Electric ---
🔄 Engine swapped to Electric Engine
🔋 Battery Level: 100.0%
⚡ Charging completed

✅ Engine started successfully
🚀 Accelerating to 50.0 km/h
📊 Current Speed: 50.0 km/h
⚡ Energy Consumption: 15.2 kWh/100km
🚀 Accelerating to 100.0 km/h
📊 Current Speed: 100.0 km/h
⚡ Energy Consumption: 22.8 kWh/100km
🚀 Accelerating to 200.0 km/h
📊 Current Speed: 200.0 km/h
⚡ Energy Consumption: 45.6 kWh/100km

--- Runtime Engine Swap: Electric → Hybrid ---
🔄 Engine swapped to Hybrid Engine
⛽ Fuel Level: 100.0%
🔋 Battery Level: 100.0%

✅ Engine started successfully
--- Testing Hybrid Mode Switching ---
🚀 Accelerating to 30.0 km/h
🔋 Electric Mode: 30.0 km/h
⚡ Energy Consumption: 12.5 kWh/100km
🚀 Accelerating to 80.0 km/h
⛽ Petrol Mode: 80.0 km/h
⛽ Fuel Consumption: 10.2 L/100km
🚀 Accelerating to 40.0 km/h
🔋 Electric Mode: 40.0 km/h
⚡ Energy Consumption: 14.8 kWh/100km

--- Testing Low Energy Scenarios ---
🚀 Accelerating to 100.0 km/h (Attempt 1)
🚀 Accelerating to 100.0 km/h (Attempt 2)
...
🚀 Accelerating to 100.0 km/h (Attempt 12)
⚠️ Low battery warning: 15.0%

--- Attempting to start with low battery ---
❌ Engine start failed: Insufficient battery level (15.0%)

--- Emergency Engine Swap: Electric → Hybrid ---
🔄 Engine swapped to Hybrid Engine
⛽ Fuel Level: 100.0%
✅ Engine started successfully with fuel backup

🚗 Car Status:
  - Model: Tesla Model 3
  - Engine: Hybrid Engine
  - Fuel Level: 100.0%
  - Battery Level: 15.0%
  - Current Speed: 0.0 km/h
  - Status: Ready

=== Demo Complete: Car adapted to different engine types without modification ===
```

## 🎓 Manager++ Level Insights

### Why This Matters for Leadership
- **System Architecture**: Understanding component relationships and lifecycle management
- **Technology Strategy**: Choosing appropriate engine technologies for different use cases
- **Performance Optimization**: Energy efficiency and performance monitoring
- **Fault Tolerance**: Handling system failures and emergency scenarios
- **Innovation**: Understanding emerging technologies like electric and hybrid systems

### Real-World Applications
- Automotive industry systems
- Manufacturing and assembly lines
- Energy management systems
- Performance monitoring platforms
- Fault-tolerant system design

## 🔍 Code Analysis Points

### Relationship Types Demonstrated
- **Composition**: `Car` owns `Engine` - Engine cannot exist without Car
- **Composition**: `Engine` has `FuelSystem` - Fuel system is integral to engine
- **Association**: `Car` used by `Driver` - both exist independently

### Design Patterns Used
1. **Strategy Pattern**: Interchangeable engine types
2. **State Pattern**: Engine state management
3. **Observer Pattern**: Performance monitoring
4. **Factory Pattern**: Engine creation

## 🚀 Extension Ideas

1. Add more engine types (Diesel, Hydrogen, Solar)
2. Implement advanced performance analytics
3. Add integration with GPS and navigation systems
4. Create a mobile app for car monitoring
5. Add predictive maintenance features
6. Implement eco-driving recommendations
7. Add integration with smart charging stations
8. Create a fleet management system
