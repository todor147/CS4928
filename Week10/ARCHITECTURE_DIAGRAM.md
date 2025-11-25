# Architecture Diagram

The architecture diagram is available in PlantUML format: `ARCHITECTURE_DIAGRAM.puml`

## Viewing the Diagram

You can view the diagram using:
1. **PlantUML Online**: http://www.plantuml.com/plantuml/uml/
2. **VS Code Extension**: PlantUML extension
3. **IntelliJ Plugin**: PlantUML integration

## Architecture Overview

The diagram shows:

### Four Main Layers

1. **Presentation Layer (UI)**
   - `OrderController`: Handles user actions
   - `ConsoleView`: Manages console I/O
   - `EventWiringDemo`: Demonstrates event wiring

2. **Application Layer (Use Cases)**
   - `CheckoutService`: Orchestrates checkout
   - `ReceiptFormatter`: Formats receipts
   - `EventBus`: Event-driven communication
   - `OrderEvent`, `OrderCreated`, `OrderPaid`: Domain events

3. **Domain Layer (Core Business)**
   - `Order`: Core business entity
   - `LineItem`: Value object
   - `OrderRepository`: Persistence interface

4. **Infrastructure Layer (Adapters)**
   - `InMemoryOrderRepository`: In-memory implementation
   - `Wiring`: Composition root

### Key Relationships

- **Dependencies point inward**: UI → App → Domain; Infra → Domain
- **Event Bus**: Provides loose coupling between components
- **Repository Pattern**: Abstracts persistence
- **MVC Pattern**: Separates concerns in presentation layer

### Component Connectors

- **EventBus**: Publish/subscribe pattern for asynchronous communication
- **Repository Interface**: Port for persistence abstraction
- **Service Interfaces**: Application service boundaries

