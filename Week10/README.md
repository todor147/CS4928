# Week 10 Lab - Architectural Design: Layered Architecture + MVC + Components/Connectors

## Overview

This lab restructures the Café POS system into a clean layered architecture with MVC in the presentation layer and an event-based component connector system. The goal is not to add new features, but to organize the system for better maintainability, testability, and evolution.

## Learning Objectives

- ✅ Apply Layered Architecture (Presentation, Application, Domain, Infrastructure)
- ✅ Implement MVC in the Presentation layer for console UI
- ✅ Define component boundaries and connectors via ports and adapters
- ✅ Understand trade-offs between Layering and Partitioning
- ✅ Produce architecture evidence demos

## Project Structure

```
Week10/
├── src/main/java/com/cafepos/
│   ├── common/          ← Value objects (Money)
│   ├── domain/          ← Core business entities (Order, LineItem, OrderRepository)
│   ├── app/             ← Application services (CheckoutService, ReceiptFormatter)
│   │   └── events/      ← Event bus and domain events
│   ├── infra/           ← Infrastructure adapters (InMemoryOrderRepository, Wiring)
│   ├── ui/              ← Presentation layer (OrderController, ConsoleView)
│   ├── catalog/         ← Product interfaces
│   ├── decorator/       ← Product decorators
│   ├── factory/         ← ProductFactory
│   └── pricing/         ← Pricing policies
└── pom.xml
```

## Architecture Layers

### Domain Layer (`com.cafepos.domain`)
- **Order**: Core business entity
- **LineItem**: Value object
- **OrderRepository**: Interface defining persistence contract

### Application Layer (`com.cafepos.app`)
- **CheckoutService**: Orchestrates checkout use case
- **ReceiptFormatter**: Formats receipt strings (no I/O)
- **events/**: Event bus and domain events

### Infrastructure Layer (`com.cafepos.infra`)
- **InMemoryOrderRepository**: In-memory implementation of OrderRepository
- **Wiring**: Composition root that wires dependencies

### Presentation Layer (`com.cafepos.ui`)
- **OrderController**: Handles user actions, delegates to application services
- **ConsoleView**: Handles console I/O only

## Running the Demos

### MVC Demo
```bash
cd Week10
mvn clean compile
java -cp target/classes com.cafepos.demo.Week10Demo_MVC
```

**Expected output:**
```
Order #4101
 - Espresso + Extra Shot + Oat Milk x1 = 3.80
 - Latte (Large) x2 = 7.80
Subtotal: 11.60
Discount: -0.58
Tax (10%): 1.10
Total: 12.12
```

### Event Wiring Demo
```bash
java -cp target/classes com.cafepos.ui.EventWiringDemo
```

**Expected output:**
```
[UI] order created: 4201
[UI] order paid: 4201
```

## Trade-offs: Layering vs Partitioning

### Why Layered Monolith (for now)?

We chose a **layered monolith** architecture because the system is still relatively small and doesn't require the operational complexity of microservices. A monolith is easier to develop, test, and deploy initially. In-process communication is faster than network calls—no serialization overhead, no network latency, and transactions can span multiple components easily. Developers can work across layers without coordinating API contracts, versioning, or deployment pipelines. ACID transactions are straightforward within a single process, ensuring data consistency without distributed transaction complexity. Finally, a single deployment target reduces infrastructure costs and operational overhead.

### Natural Partitioning Seams

When the system grows, natural candidates for extraction include: **Payment Service** (different scalability, security, compliance requirements; REST API connector), **Notification Service** (fire-and-forget operations; event-driven connector like EventBus), **Inventory Service** (shared catalog; REST API), and **Reporting/Analytics Service** (read-heavy workloads; event streaming or read replicas).

### Connectors/Protocols for Partitioning

If splitting into services, we would define: **REST APIs** for synchronous request/response (Payment, Inventory), **Event Bus/Message Queue** (AMQP/Kafka) for asynchronous events (OrderCreated, OrderPaid), **Database Replication** for read-heavy services, and **GraphQL** for flexible data fetching. The current EventBus implementation provides a foundation that could evolve into a distributed message broker when needed.

## Architecture Principles

- **Dependency Rule**: Dependencies point inward (UI → App → Domain; Infra → Domain)
- **Separation of Concerns**: Each layer has a single responsibility
- **Testability**: Interfaces allow easy mocking and testing
- **Evolution**: Clear boundaries make future refactoring easier

## Deliverables

✅ Four-layer package structure with clear dependencies  
✅ Application service (CheckoutService) and repository interface + implementation  
✅ MVC console demo (Week10Demo_MVC)  
✅ Event bus + demo wiring (EventWiringDemo)  
✅ README trade-off note (Layering vs Partitioning)  
✅ Architecture diagram (see ARCHITECTURE_DIAGRAM.md)

---

**Week 10 Lab Complete** ✅

