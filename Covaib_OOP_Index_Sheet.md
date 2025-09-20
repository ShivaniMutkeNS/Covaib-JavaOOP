## COVAIB OOP Sheet

This sheet aggregates core OOP concepts across the repository and provides a learning index to navigate the demos quickly.

### The 5 Core OOP Concepts

1) Encapsulation
- **Idea**: Hide internal state and expose behavior via a stable interface. Control access through methods; keep fields private or immutable.
- **Where to see**: [encapsulation/](encapsulation/) (e.g., [1-immutable-config-loader](encapsulation/1-immutable-config-loader/), [2-thread-safe-counter](encapsulation/2-thread-safe-counter/), [6-immutable-money-class](encapsulation/6-immutable-money-class/))
- **Benefits**: Safety, invariants, easier refactoring.

2) Abstraction
- **Idea**: Expose essential features and hide details. Define contracts (interfaces/abstract classes) that multiple implementations can fulfill.
- **Where to see**: [abstraction/](abstraction/) (e.g., [01-payment-processor](abstraction/01-payment-processor/), [02-file-storage](abstraction/02-file-storage/), [03-logger-framework](abstraction/03-logger-framework/))
- **Benefits**: Reduces complexity, improves modularity and substitution.

3) Inheritance
- **Idea**: Create specialized types from generalized types; reuse behavior from base classes.
- **Where to see**: [inheritance/basic](inheritance/basic/), [inheritance/intermediate](inheritance/intermediate/), [inheritance/advanced](inheritance/advanced/)
- **Benefits**: Code reuse, shared behaviors, but use judiciously to avoid tight coupling.

4) Polymorphism
- **Idea**: Same interface, different implementations. Callers rely on the contract, not the concrete class.
- **Where to see**: [polymorphism/](polymorphism/) (15 mini-projects such as shape-area-calculator, payment-gateway, job-scheduler)
- **Benefits**: Extensibility, open/closed design, simpler client code.

5) Composition (and Aggregation/Association)
- **Idea**: Build complex behavior by composing objects; favor "has-a" over "is-a" relationships.
- **Where to see**: [composition-aggregation-associations/](composition-aggregation-associations/) (e.g., [01-library-management](composition-aggregation-associations/01-library-management/), [08-media-player](composition-aggregation-associations/08-media-player/), [10-ecommerce-cart](composition-aggregation-associations/10-ecommerce-cart/))
- **Benefits**: Flexibility, testability, reduced inheritance pitfalls.

### COVAIB Learning Index

Use this index to browse examples by concept and real-world domain.

- Encapsulation
  - Immutable and Safety: [encapsulation/1-immutable-config-loader](encapsulation/1-immutable-config-loader/), [encapsulation/6-immutable-money-class](encapsulation/6-immutable-money-class/)
  - Concurrency and Privacy: [encapsulation/2-thread-safe-counter](encapsulation/2-thread-safe-counter/), [encapsulation/3-bank-vault-multilayer-security](encapsulation/3-bank-vault-multilayer-security/)

- Abstraction
  - Services and Protocols: [abstraction/01-payment-processor](abstraction/01-payment-processor/), [abstraction/02-file-storage](abstraction/02-file-storage/), [abstraction/03-logger-framework](abstraction/03-logger-framework/)
  - Devices and Systems: [abstraction/04-smart-home-device](abstraction/04-smart-home-device/), [abstraction/05-transport-abstraction](abstraction/05-transport-abstraction/), [abstraction/06-machine-learning-models](abstraction/06-machine-learning-models/)

- Inheritance
  - Step-by-step Exercises: [inheritance/basic](inheritance/basic/), [inheritance/intermediate](inheritance/intermediate/), [inheritance/advanced](inheritance/advanced/)
  - Demo Outputs: [inheritance/MASTER_DEMO_OUTPUTS.md](inheritance/MASTER_DEMO_OUTPUTS.md)

- Polymorphism (15 Projects)
  - Getting Started: [polymorphism/01-shape-area-calculator](polymorphism/01-shape-area-calculator/)
  - Systems and Services: [polymorphism/03-message-sender](polymorphism/03-message-sender/), [polymorphism/07-payment-gateway](polymorphism/07-payment-gateway/), [polymorphism/11-job-scheduler](polymorphism/11-job-scheduler/)
  - Data & Analytics: [polymorphism/05-ml-model](polymorphism/05-ml-model/), [polymorphism/12-analytics-engine](polymorphism/12-analytics-engine/)
  - Devices & Operations: [polymorphism/09-smart-home-device](polymorphism/09-smart-home-device/), [polymorphism/10-bank-account-operations](polymorphism/10-bank-account-operations/), [polymorphism/14-vehicle-navigation-system](polymorphism/14-vehicle-navigation-system/)
  - Commerce & Docs: [polymorphism/04-document-exporter](polymorphism/04-document-exporter/), [polymorphism/06-ecommerce-discounts](polymorphism/06-ecommerce-discounts/)
  - Learning & Games: [polymorphism/13-game-character-attack](polymorphism/13-game-character-attack/), [polymorphism/15-e-learning-assessment-grader](polymorphism/15-e-learning-assessment-grader/)
  - Summary & Outputs: [polymorphism/MASTER_README.md](polymorphism/MASTER_README.md)

- Composition, Aggregation, Association (15 Projects)
  - End-to-end Scenarios: [composition-aggregation-associations/](composition-aggregation-associations/) (e.g., [10-ecommerce-cart](composition-aggregation-associations/10-ecommerce-cart/), [11-analytics-pipeline](composition-aggregation-associations/11-analytics-pipeline/), [12-bank-account-alerts](composition-aggregation-associations/12-bank-account-alerts/))
  - Demos Index: [composition-aggregation-associations/PROJECT_DEMONSTRATIONS.md](composition-aggregation-associations/PROJECT_DEMONSTRATIONS.md)

### How to Learn Effectively here

1) Start with Polymorphism 01 → 15 to build intuition for contracts and substitutability.
2) Visit Abstraction projects to see broader contracts across domains (payments, storage, logging).
3) Explore Encapsulation to understand correctness and safety in concurrent or sensitive contexts.
4) Use Inheritance basic → advanced to learn reuse patterns and pitfalls.
5) Apply Composition projects to design robust systems by combining objects.

### Build & Run Tips

- Quick run per mini-project folder:
  - Compile: `javac *.java`
  - Run: `java <DemoClass>`
- If your IDE cannot resolve types:
  - Ensure a JDK is installed and selected.
  - Compile from within each folder so default-package classes resolve.
  - Optionally: convert to packages + add Gradle for full-project builds.

### Repository-wide Index (All Folders)

Use these links to jump directly into any demo. Each folder contains source files and most include a README with usage notes.

- Abstraction (15)
  - [01-payment-processor](abstraction/01-payment-processor/)
  - [02-file-storage](abstraction/02-file-storage/)
  - [03-logger-framework](abstraction/03-logger-framework/)
  - [04-smart-home-device](abstraction/04-smart-home-device/)
  - [05-transport-abstraction](abstraction/05-transport-abstraction/)
  - [06-machine-learning-models](abstraction/06-machine-learning-models/)
  - [07-document-exporter](abstraction/07-document-exporter/)
  - [08-notification-system](abstraction/08-notification-system/)
  - [09-video-streaming](abstraction/09-video-streaming/)
  - [10-bank-account](abstraction/10-bank-account/)
  - [11-authentication](abstraction/11-authentication/)
  - [12-ecommerce-discounts](abstraction/12-ecommerce-discounts/)
  - [13-job-scheduling](abstraction/13-job-scheduling/)
  - [14-analytics-engine](abstraction/14-analytics-engine/)
  - [15-smart-vehicle](abstraction/15-smart-vehicle/)

- Encapsulation (15)
  - [1-immutable-config-loader](encapsulation/1-immutable-config-loader/)
  - [2-thread-safe-counter](encapsulation/2-thread-safe-counter/)
  - [3-bank-vault-multilayer-security](encapsulation/3-bank-vault-multilayer-security/)
  - [4-healthcare-records-privacy-law](encapsulation/4-healthcare-records-privacy-law/)
  - [5-online-exam-system-anticheating](encapsulation/5-online-exam-system-anticheating/)
  - [6-immutable-money-class](encapsulation/6-immutable-money-class/)
  - [7-smart-home-device-state](encapsulation/7-smart-home-device-state/)
  - [8-credit-card-system](encapsulation/8-credit-card-system/)
  - [9-immutable-audit-log](encapsulation/9-immutable-audit-log/)
  - [10-game-character-attributes](encapsulation/10-game-character-attributes/)
  - [11-ecommerce-shopping-cart](encapsulation/11-ecommerce-shopping-cart/)
  - [12-employee-payroll-hidden-salary](encapsulation/12-employee-payroll-hidden-salary/)
  - [13-iot-sensor-data-stream](encapsulation/13-iot-sensor-data-stream/)
  - [14-versioned-document](encapsulation/14-versioned-document/)
  - [15-stock-trading-system](encapsulation/15-stock-trading-system/)

- Inheritance
  - Basic
    - [1-zoo-animals](inheritance/basic/1-zoo-animals/)
    - [2-hotel-room-booking](inheritance/basic/2-hotel-room-booking/)
    - [3-retail-membership-tiers](inheritance/basic/3-retail-membership-tiers/)
  - Intermediate
    - [4-bank-accounts](inheritance/intermediate/4-bank-accounts/)
    - [5-transport-tickets](inheritance/intermediate/5-transport-tickets/)
    - [6-notification-channels](inheritance/intermediate/6-notification-channels/)
    - [7-ecommerce-delivery](inheritance/intermediate/7-ecommerce-delivery/)
    - [8-university-roles](inheritance/intermediate/8-university-roles/)
  - Advanced
    - [9-payment-gateway](inheritance/advanced/9-payment-gateway/)
    - [10-ride-sharing](inheritance/advanced/10-ride-sharing/)
    - [11-employee-hierarchy](inheritance/advanced/11-employee-hierarchy/)
    - [12-online-learning](inheritance/advanced/12-online-learning/)
    - [13-video-streaming](inheritance/advanced/13-video-streaming/)
    - [14-chess-game](inheritance/advanced/14-chess-game/)
    - [15-gaming-characters](inheritance/advanced/15-gaming-characters/)

- Polymorphism (15)
  - [01-shape-area-calculator](polymorphism/01-shape-area-calculator/)
  - [02-ride-pricing-system](polymorphism/02-ride-pricing-system/)
  - [03-message-sender](polymorphism/03-message-sender/)
  - [04-document-exporter](polymorphism/04-document-exporter/)
  - [05-ml-model](polymorphism/05-ml-model/)
  - [06-ecommerce-discounts](polymorphism/06-ecommerce-discounts/)
  - [07-payment-gateway](polymorphism/07-payment-gateway/)
  - [08-logger-framework](polymorphism/08-logger-framework/)
  - [09-smart-home-device](polymorphism/09-smart-home-device/)
  - [10-bank-account-operations](polymorphism/10-bank-account-operations/)
  - [11-job-scheduler](polymorphism/11-job-scheduler/)
  - [12-analytics-engine](polymorphism/12-analytics-engine/)
  - [13-game-character-attack](polymorphism/13-game-character-attack/)
  - [14-vehicle-navigation-system](polymorphism/14-vehicle-navigation-system/)
  - [15-e-learning-assessment-grader](polymorphism/15-e-learning-assessment-grader/)

- Composition, Aggregation, Association (15)
  - [01-library-management](composition-aggregation-associations/01-library-management/)
  - [02-car-engine](composition-aggregation-associations/02-car-engine/)
  - [03-order-payment](composition-aggregation-associations/03-order-payment/)
  - [04-notification-service](composition-aggregation-associations/04-notification-service/)
  - [05-restaurant-order](composition-aggregation-associations/05-restaurant-order/)
  - [06-computer-system](composition-aggregation-associations/06-computer-system/)
  - [07-travel-booking](composition-aggregation-associations/07-travel-booking/)
  - [08-media-player](composition-aggregation-associations/08-media-player/)
  - [09-smart-home-hub](composition-aggregation-associations/09-smart-home-hub/)
  - [10-ecommerce-cart](composition-aggregation-associations/10-ecommerce-cart/)
  - [11-analytics-pipeline](composition-aggregation-associations/11-analytics-pipeline/)
  - [12-bank-account-alerts](composition-aggregation-associations/12-bank-account-alerts/)
  - [13-gaming-character](composition-aggregation-associations/13-gaming-character/)
  - [14-elearning-course](composition-aggregation-associations/14-elearning-course/)
  - [15-payment-reconciliation](composition-aggregation-associations/15-payment-reconciliation/)
