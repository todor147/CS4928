# ADR-001: EventBus as Component Connector

## Status
Accepted

## Context

As we restructured the Café POS system into a layered architecture (Week 10), we needed a mechanism for components to communicate without creating tight coupling between layers. The system has multiple components that need to react to domain events:

- **UI Layer**: Needs to update displays when orders are created or paid
- **Application Layer**: Services need to coordinate workflows
- **Future Integrations**: Payment processors, notification services, analytics

Direct method calls would create dependencies that violate the layered architecture principles. We needed a connector that allows components to communicate asynchronously while maintaining clear boundaries.

## Decision

We chose to implement an **EventBus** as the component connector for event-driven communication. The EventBus follows a publish/subscribe pattern where:

1. Components subscribe to event types using `EventBus.on(EventType.class, handler)`
2. Components publish events using `EventBus.emit(event)`
3. The EventBus routes events to all registered handlers for that event type
4. Handlers are decoupled from publishers - they don't know who published the event

## Alternatives Considered

### 1. Direct Method Calls
**Pros:**
- Simple to implement
- Type-safe at compile time
- Easy to debug (direct call stack)

**Cons:**
- Creates tight coupling between components
- Violates layered architecture (UI would need to know about all services)
- Hard to add new subscribers without modifying publishers
- Difficult to test (need to mock many dependencies)

### 2. Observer Pattern (Direct)
**Pros:**
- Decouples subject from observers
- Well-known pattern

**Cons:**
- Still requires direct references (subject knows observers)
- Doesn't scale well (subject needs to manage observer list)
- Hard to add cross-cutting concerns (logging, metrics)

### 3. Message Queue (Kafka/RabbitMQ)
**Pros:**
- Production-ready for distributed systems
- Persistence and reliability guarantees
- Can scale across services

**Cons:**
- Overkill for current monolith architecture
- Requires external infrastructure
- Adds operational complexity
- Network overhead for in-process communication

### 4. EventBus (Chosen)
**Pros:**
- ✅ Loose coupling: publishers don't know subscribers
- ✅ Easy to add new subscribers without changing publishers
- ✅ Supports multiple handlers per event type
- ✅ Simple in-process implementation (no external dependencies)
- ✅ Can evolve to distributed message queue later
- ✅ Testable (can verify events are emitted/received)
- ✅ Follows event-driven architecture principles

**Cons:**
- Runtime type checking (events matched by class at runtime)
- No persistence (events lost if handler crashes) - acceptable for current scope
- No ordering guarantees - acceptable for current use cases

## Consequences

### Positive

1. **Loose Coupling**: UI components can react to events without knowing about application services
2. **Extensibility**: Easy to add new event handlers (e.g., email notifications, analytics) without modifying existing code
3. **Testability**: Can verify events are emitted and handlers are called independently
4. **Future-Proof**: The EventBus interface can be replaced with a distributed message broker (Kafka, RabbitMQ) when we move to microservices
5. **Clear Boundaries**: Maintains layer separation - events flow through the connector, not direct dependencies

### Negative

1. **Runtime Errors**: Type mismatches only discovered at runtime (mitigated by using typed events)
2. **Debugging**: Event flow can be harder to trace than direct calls (mitigated by logging)
3. **No Guarantees**: No delivery guarantees if handler throws exception (acceptable for current scope)

## Implementation Details

The EventBus implementation (`com.cafepos.app.events.EventBus`):

- Uses `Map<Class<?>, List<Consumer<?>>>` to store handlers by event type
- Events must implement `OrderEvent` marker interface
- Handlers are invoked synchronously (can be made async later)
- Type-safe through generics: `on(Class<T>, Consumer<T>)`

Example usage:
```java
EventBus bus = new EventBus();
bus.on(OrderCreated.class, e -> System.out.println("Order " + e.orderId() + " created"));
bus.emit(new OrderCreated(123L));
```

## Related Patterns

- **Observer Pattern**: EventBus is a generalized observer
- **Mediator Pattern**: EventBus mediates between components
- **Publish/Subscribe**: Core pattern used by EventBus

## References

- See `Week10/src/main/java/com/cafepos/app/events/EventBus.java`
- See `Week10/src/main/java/com/cafepos/ui/EventWiringDemo.java` for usage example


