# Week 10 Video Guide
## Complete Word-for-Word Script with Exact File Locations and Line Numbers

**Target Duration:** 8 minutes maximum  
**Objective:** Demonstrate how the system satisfies the assessment rubric

---

## 🎬 Quick Start Commands

```powershell
cd "C:\Users\todor\OneDrive - University of Limerick\Mine\Github\CS4928\Week10"

# Run MVC Demo
java -cp "target/classes" com.cafepos.demo.Week10Demo_MVC

# Run Event Wiring Demo
java -cp "target/classes" com.cafepos.ui.EventWiringDemo
```

---

## 📋 Complete Video Script (Word-for-Word)

### Introduction (30 seconds)

**Say exactly:**
"Hello, this is my final assessment submission for the Café POS & Delivery System, covering Weeks 8-11. In this video, I'll demonstrate how the system integrates multiple design patterns from Weeks 8-9, follows a clean four-layer architecture from Week 10, includes comprehensive tests, and documents key architectural decisions. Let me start with a quick overview of the Week 10 project structure."

**Action:**
- Show `Week10/` folder in IDE package explorer
- Highlight the four packages: `ui/`, `app/`, `domain/`, `infra/`
- Point to each package and say: "Here we have the Presentation layer, Application layer, Domain layer, and Infrastructure layer"

---

### 1. Pattern Correctness & Integration (8 marks) - ~3 minutes

#### 1.1 MVC Pattern (30 seconds)

**Say exactly:**
"First, let me show the MVC pattern in the presentation layer. The MVC pattern separates concerns: the Controller handles user actions, the View manages I/O, and the Model contains the business logic."

**Action 1: Run Demo**
- Open terminal
- Type: `java -cp "target/classes" com.cafepos.demo.Week10Demo_MVC`
- Press Enter
- Wait for output

**Say exactly:**
"As you can see, the demo creates an order, adds items, and generates a receipt. Now let me show you the code structure."

**Action 2: Show Controller**
- Open file: `Week10/src/main/java/com/cafepos/ui/OrderController.java`
- **Highlight lines 12-15** (constructor):
```java
public OrderController(OrderRepository repo, CheckoutService checkout) {
    this.repo = repo;
    this.checkout = checkout;
}
```
- **Say:** "The controller takes a repository and checkout service as dependencies. Notice it doesn't handle I/O directly."

- **Highlight lines 17-20** (createOrder method):
```java
public long createOrder(long id) {
    repo.save(new Order(id));
    return id;
}
```
- **Say:** "The controller delegates order creation to the repository."

- **Highlight lines 28-30** (checkout method):
```java
public String checkout(long orderId, int taxPercent) {
    return checkout.checkout(orderId, taxPercent);
}
```
- **Say:** "The controller delegates checkout to the application service and returns a string. It doesn't print anything."

**Action 3: Show View**
- Open file: `Week10/src/main/java/com/cafepos/ui/ConsoleView.java`
- **Highlight lines 4-6**:
```java
public void print(String s) {
    System.out.println(s);
}
```
- **Say:** "The View is responsible only for I/O. It has no business logic, just printing."

**Action 4: Mention Model**
- **Say:** "The Model is in the domain layer. Let me show you."
- Open file: `Week10/src/main/java/com/cafepos/domain/Order.java`
- **Highlight lines 7-9**:
```java
public final class Order {
    private final long id;
    private final List<LineItem> items = new ArrayList<>();
```
- **Say:** "This is our domain model - pure business logic with no dependencies on other layers."

**Say exactly:**
"This demonstrates clean separation: Controller delegates to services, View handles only I/O, and Model contains business logic."

---

#### 1.2 EventBus Pattern (30 seconds)

**Say exactly:**
"Next, the EventBus connector demonstrates event-driven communication. Components can subscribe to events without knowing who publishes them, creating loose coupling."

**Action 1: Run Demo**
- Open terminal
- Type: `java -cp "target/classes" com.cafepos.ui.EventWiringDemo`
- Press Enter
- Wait for output

**Say exactly:**
"You can see multiple handlers responding to the same event. Now let me show you how this works in code."

**Action 2: Show EventBus**
- Open file: `Week10/src/main/java/com/cafepos/app/events/EventBus.java`
- **Highlight lines 9-11** (on method):
```java
public <T> void on(Class<T> type, Consumer<T> h) {
    handlers.computeIfAbsent(type, k -> new ArrayList<>()).add(h);
}
```
- **Say:** "Components subscribe to events using the `on` method. Notice it takes an event type and a handler function."

- **Highlight lines 14-19** (emit method):
```java
public <T> void emit(T event) {
    var list = handlers.getOrDefault(event.getClass(), List.of());
    for (var h : list) {
        ((Consumer<T>) h).accept(event);
    }
}
```
- **Say:** "When an event is emitted, the EventBus routes it to all registered handlers for that event type. This enables multiple handlers to react to the same event."

**Action 3: Show Event Types**
- Open file: `Week10/src/main/java/com/cafepos/app/events/OrderEvent.java`
- **Highlight line 3**:
```java
public sealed interface OrderEvent permits OrderCreated, OrderPaid {}
```
- **Say:** "Events implement this sealed interface, ensuring type safety."

- Open file: `Week10/src/main/java/com/cafepos/app/events/OrderCreated.java`
- **Highlight line 3**:
```java
public record OrderCreated(long orderId) implements OrderEvent {}
```
- **Say:** "Events are simple records containing the data. This keeps them immutable and easy to work with."

**Say exactly:**
"This pattern enables future extensibility - we can add new handlers without modifying existing code, and components remain decoupled."

---

#### 1.3 Command Pattern (20 seconds)

**Say exactly:**
"From Week 8, we have the Command pattern for button actions. Commands encapsulate actions and support undo operations."

**Action:**
- Navigate to: `Week09/src/main/java/com/cafepos/command/Command.java`
- **Highlight lines 3-8**:
```java
public interface Command {
    void execute();
    
    default void undo() {
        // optional
    }
}
```
- **Say:** "The Command interface defines execute and undo operations."

- Navigate to: `Week09/src/main/java/com/cafepos/command/PosRemote.java`
- **Highlight lines 18-26** (press method):
```java
public void press(int i) {
    Command c = slots[i];
    if (c != null) {
        c.execute();
        history.push(c);
    }
}
```
- **Say:** "The PosRemote invokes commands and maintains a history for undo."

- **Highlight lines 28-34** (undo method):
```java
public void undo() {
    if (history.isEmpty()) {
        System.out.println("[Remote] Nothing to undo");
        return;
    }
    history.pop().undo();
}
```
- **Say:** "Undo pops the last command from history and calls its undo method."

**Say exactly:**
"This decouples button presses from business logic, allowing commands to be queued, logged, or undone."

---

#### 1.4 Adapter Pattern (20 seconds)

**Say exactly:**
"Also from Week 8, the Adapter pattern integrates legacy systems without modifying core code."

**Action:**
- Navigate to: `Week09/src/main/java/com/cafepos/printing/LegacyPrinterAdapter.java`
- **Highlight lines 6-11** (class declaration and constructor):
```java
public final class LegacyPrinterAdapter implements Printer {
    private final LegacyThermalPrinter adaptee;
    
    public LegacyPrinterAdapter(LegacyThermalPrinter adaptee) {
        this.adaptee = adaptee;
    }
```
- **Say:** "The adapter wraps the legacy printer and implements our Printer interface."

- **Highlight lines 14-17** (print method):
```java
@Override
public void print(String receiptText) {
    byte[] escpos = receiptText.getBytes(StandardCharsets.UTF_8);
    adaptee.legacyPrint(escpos);
}
```
- **Say:** "The adapter translates our string-based interface to the legacy byte array interface."

**Say exactly:**
"This allows integration without modifying core code or the legacy system."

---

#### 1.5 Composite & Iterator Patterns (20 seconds)

**Say exactly:**
"From Week 9, Composite and Iterator enable hierarchical menus. Composite allows treating individual items and groups uniformly."

**Action:**
- Navigate to: `Week09/src/main/java/com/cafepos/menu/MenuComponent.java`
- **Highlight lines 6-42** (entire class):
- **Say:** "MenuComponent is the base class. Notice it provides default implementations that throw exceptions for unsupported operations."

- **Highlight lines 19-21** (add method):
```java
public void add(MenuComponent component) {
    throw new UnsupportedOperationException();
}
```
- **Say:** "MenuItems throw exceptions for add operations, while Menus support them."

- **Highlight lines 31-33** (iterator method):
```java
public Iterator<MenuComponent> iterator() {
    throw new UnsupportedOperationException();
}
```
- **Say:** "The iterator method enables traversal and filtering of menu structures."

**Say exactly:**
"This allows us to treat individual menu items and menu groups uniformly, and iterate over them to filter, for example, vegetarian options."

---

#### 1.6 State Pattern (20 seconds)

**Say exactly:**
"Finally, the State pattern models order lifecycle. Each state encapsulates its own behavior, replacing complex conditional chains."

**Action:**
- Navigate to: `Week09/src/main/java/com/cafepos/state/OrderFSM.java`
- **Highlight lines 10-24** (state transition methods):
```java
public void pay() {
    currentState.pay(this);
}

public void prepare() {
    currentState.prepare(this);
}

public void markReady() {
    currentState.markReady(this);
}

public void deliver() {
    currentState.deliver(this);
}
```
- **Say:** "The context delegates all operations to the current state."

- **Highlight lines 34-38** (setState method):
```java
void setState(State state) {
    if (state == null)
        throw new IllegalArgumentException("state required");
    this.currentState = state;
}
```
- **Say:** "State transitions happen through this method."

- Navigate to: `Week09/src/main/java/com/cafepos/state/State.java`
- **Highlight lines 3-9**:
```java
public interface State {
    void pay(OrderFSM context);
    void prepare(OrderFSM context);
    void markReady(OrderFSM context);
    void deliver(OrderFSM context);
    void cancel(OrderFSM context);
    String name();
}
```
- **Say:** "Each state implements this interface, defining what operations are valid in that state."

**Say exactly:**
"This replaces complex if-else chains with clear state transitions: NEW to PREPARING to READY to DELIVERED."

---

### 2. Architectural Integrity (7 marks) - ~2 minutes

#### 2.1 Four-Layer Architecture Overview (1 minute)

**Say exactly:**
"Now let me show the layered architecture. We have four clear layers with proper boundaries."

**Action 1: Show Package Structure**
- In IDE package explorer, expand: `Week10/src/main/java/com/cafepos/`
- Point to each package:

**Presentation Layer (ui/):**
- **Say:** "The Presentation layer contains `OrderController` and `ConsoleView`. It handles user interactions and I/O only, and delegates all business logic to the application layer."
- Open: `Week10/src/main/java/com/cafepos/ui/OrderController.java`
- **Highlight lines 3-4** (imports):
```java
import com.cafepos.app.CheckoutService;
import com.cafepos.domain.*;
```
- **Say:** "Notice the controller depends on the application layer and domain layer, but not infrastructure."

**Application Layer (app/):**
- **Say:** "The Application layer contains `CheckoutService` and `ReceiptFormatter`. It orchestrates use cases and contains the EventBus for component communication. It has no direct I/O or persistence."
- Open: `Week10/src/main/java/com/cafepos/app/CheckoutService.java`
- **Highlight lines 3-4** (imports):
```java
import com.cafepos.domain.*;
import com.cafepos.pricing.PricingService;
```
- **Say:** "The application service depends only on the domain layer."

- **Highlight lines 16-20** (checkout method):
```java
public String checkout(long orderId, int taxPercent) {
    Order order = orders.findById(orderId).orElseThrow();
    var pr = pricing.price(order.subtotal());
    return new ReceiptFormatter().format(orderId, order.items(), pr, taxPercent);
}
```
- **Say:** "Notice it returns a string but doesn't print. That's the view's responsibility."

**Domain Layer (domain/):**
- **Say:** "The Domain layer contains core business entities like `Order` and `LineItem`, and repository interfaces. It has pure business logic with no dependencies on other layers."
- Open: `Week10/src/main/java/com/cafepos/domain/OrderRepository.java`
- **Highlight lines 5-8**:
```java
public interface OrderRepository {
    void save(Order order);
    Optional<Order> findById(long id);
}
```
- **Say:** "This is an interface in the domain layer. It defines the contract, not the implementation."

**Infrastructure Layer (infra/):**
- **Say:** "The Infrastructure layer implements domain interfaces and provides adapters. It depends on the domain, but the domain doesn't depend on it."
- Open: `Week10/src/main/java/com/cafepos/infra/InMemoryOrderRepository.java`
- **Highlight line 6**:
```java
public final class InMemoryOrderRepository implements OrderRepository {
```
- **Say:** "The infrastructure implements the domain interface."

- **Highlight lines 9-12** (save method):
```java
@Override
public void save(Order order) {
    store.put(order.id(), order);
}
```
- **Say:** "This is a concrete implementation. We could swap it for a database implementation without changing the domain."

---

#### 2.2 Dependency Rule (30 seconds)

**Say exactly:**
"Notice that dependencies point inward. The UI depends on App and Domain. App depends on Domain. Infrastructure implements Domain interfaces. And Domain has no dependencies on other layers."

**Action:**
- Draw or point to the dependency flow:
  - UI → App → Domain
  - Infra → Domain
- **Say:** "This is the Dependency Inversion Principle - high-level modules don't depend on low-level modules. Both depend on abstractions."

---

#### 2.3 Component Connectors (30 seconds)

**Say exactly:**
"The EventBus is our main connector, enabling loose coupling between components."

**Action:**
- Open: `Week10/src/main/java/com/cafepos/app/events/EventBus.java`
- **Say:** "Components communicate through events, not direct calls. This maintains architectural boundaries."
- **Highlight lines 9-11** and **14-19** again
- **Say:** "When a component emits an event, any component that subscribed to that event type receives it, without knowing who published it."

---

### 3. Code Quality & Testing (6 marks) - ~1.5 minutes

#### 3.1 Code Organization (30 seconds)

**Say exactly:**
"Let me show you the code organization. We have clear package organization by layer, consistent naming conventions, and follow the Single Responsibility Principle."

**Action:**
- Show package structure in IDE
- **Say:** "Each package has a single responsibility: UI handles presentation, App handles use cases, Domain handles business logic, and Infra handles technical concerns."

---

#### 3.2 Refactoring Since Mid-Term (20 seconds)

**Say exactly:**
"Since the mid-term, we restructured from feature-based to layer-based organization. We introduced application services to orchestrate use cases, and separated concerns into UI, Application, Domain, and Infrastructure layers."

---

#### 3.3 Testing (40 seconds)

**Say exactly:**
"Now let me show you our test coverage. We have JUnit tests covering key components."

**Action:**
- Show test package: `Week10/src/test/java/com/cafepos/`
- Open: `Week10/src/test/java/com/cafepos/OrderControllerTest.java`
- **Highlight lines 31-39** (testCreateOrder):
```java
@Test
void testCreateOrder() {
    long orderId = controller.createOrder(1001L);
    assertEquals(1001L, orderId);
    
    var order = repo.findById(1001L);
    assertTrue(order.isPresent());
    assertEquals(1001L, order.get().id());
}
```
- **Say:** "This tests the MVC controller behavior - creating orders and verifying they're saved."

- Open: `Week10/src/test/java/com/cafepos/EventBusTest.java`
- **Highlight lines 27-40** (testMultipleHandlersForSameEvent):
```java
@Test
void testMultipleHandlersForSameEvent() {
    EventBus bus = new EventBus();
    List<String> messages = new ArrayList<>();
    
    bus.on(OrderCreated.class, e -> messages.add("Handler1: " + e.orderId()));
    bus.on(OrderCreated.class, e -> messages.add("Handler2: " + e.orderId()));
    
    bus.emit(new OrderCreated(2001L));
    
    assertEquals(2, messages.size());
    assertTrue(messages.contains("Handler1: 2001"));
    assertTrue(messages.contains("Handler2: 2001"));
}
```
- **Say:** "This tests that multiple handlers can subscribe to the same event and all receive it."

**Say exactly:**
"We have tests for OrderController, CheckoutService, EventBus, and Order. These verify order lifecycle, event flow, and service behavior. The tests are compiled in target/test-classes."

**Note:** If Maven is available, you can run `mvn test` here. Otherwise, just mention the tests are compiled and ready.

---

### 4. Trade-off Documentation (4 marks) - ~1 minute

#### 4.1 Architecture Decision Record (1 minute)

**Say exactly:**
"Let me show you an Architecture Decision Record that documents a key architectural choice."

**Action:**
- Open: `Week10/ADR-001-EventBus-Connector.md`

**Say exactly:**
"The context was that we needed a mechanism for components to communicate without creating tight coupling between layers. Direct method calls would violate layered architecture principles."

**Scroll to "Alternatives Considered" section**

**Say exactly:**
"We considered several alternatives. Direct method calls were too tightly coupled. The Observer pattern still required direct references. A message queue like Kafka or RabbitMQ would be overkill for our current monolith. We chose EventBus for loose coupling and simplicity."

**Scroll to "Decision" section**

**Say exactly:**
"We chose EventBus for the publish-subscribe pattern. It enables loose coupling while remaining simple - components subscribe to events without knowing publishers, and the EventBus routes events to all registered handlers."

**Scroll to "Consequences" section**

**Say exactly:**
"The positive consequences are that it's easy to extend with new handlers, it's testable, and it maintains architectural boundaries. The negative consequences are runtime type checking instead of compile-time, and no persistence - but these are acceptable for our current scope."

**Action:**
- Switch back to: `Week10/src/main/java/com/cafepos/app/events/EventBus.java`
- **Highlight lines 9-11** and **14-19** briefly
- **Say:** "You can see this implementation in the EventBus class - simple but effective."

---

### Conclusion (30 seconds)

**Say exactly:**
"To summarize, this system demonstrates multiple design patterns correctly integrated, including MVC, EventBus, Command, Adapter, Composite, Iterator, and State. We have a clean four-layer architecture with proper boundaries, where dependencies point inward. We have comprehensive test coverage with JUnit tests for key components. And we've documented architectural decisions in an ADR, showing alternatives considered and consequences.

The codebase is organized, testable, and ready for future evolution. Thank you for watching."

---

## 📁 Complete File Reference

### Week 10 Files (Main Focus)

| File | Purpose | Key Lines |
|------|---------|-----------|
| `src/main/java/com/cafepos/ui/OrderController.java` | MVC Controller | 12-15 (constructor), 17-20 (createOrder), 28-30 (checkout) |
| `src/main/java/com/cafepos/ui/ConsoleView.java` | MVC View | 4-6 (print method) |
| `src/main/java/com/cafepos/domain/Order.java` | MVC Model | 7-9 (class fields) |
| `src/main/java/com/cafepos/app/events/EventBus.java` | Component Connector | 9-11 (on), 14-19 (emit) |
| `src/main/java/com/cafepos/app/events/OrderEvent.java` | Event Interface | 3 (sealed interface) |
| `src/main/java/com/cafepos/app/events/OrderCreated.java` | Event Record | 3 (record) |
| `src/main/java/com/cafepos/app/CheckoutService.java` | Application Service | 16-20 (checkout) |
| `src/main/java/com/cafepos/domain/OrderRepository.java` | Domain Interface | 5-8 (interface) |
| `src/main/java/com/cafepos/infra/InMemoryOrderRepository.java` | Infrastructure | 6 (implements), 9-12 (save) |
| `src/test/java/com/cafepos/OrderControllerTest.java` | Controller Tests | 31-39 (testCreateOrder) |
| `src/test/java/com/cafepos/EventBusTest.java` | EventBus Tests | 27-40 (testMultipleHandlers) |
| `ADR-001-EventBus-Connector.md` | Architecture Decision | Context, Alternatives, Decision, Consequences |

### Week 8/9 Files (Reference)

| File | Purpose | Key Lines |
|------|---------|-----------|
| `Week09/src/main/java/com/cafepos/command/Command.java` | Command Interface | 3-8 (interface) |
| `Week09/src/main/java/com/cafepos/command/PosRemote.java` | Command Invoker | 18-26 (press), 28-34 (undo) |
| `Week09/src/main/java/com/cafepos/printing/LegacyPrinterAdapter.java` | Adapter | 6-11 (class), 14-17 (print) |
| `Week09/src/main/java/com/cafepos/menu/MenuComponent.java` | Composite Base | 19-21 (add), 31-33 (iterator) |
| `Week09/src/main/java/com/cafepos/state/OrderFSM.java` | State Context | 10-24 (methods), 34-38 (setState) |
| `Week09/src/main/java/com/cafepos/state/State.java` | State Interface | 3-9 (interface) |

---

## ⏱️ Timing Breakdown

- **Introduction:** 30 seconds
- **Pattern Integration:** 3 minutes
  - MVC: 30s
  - EventBus: 30s
  - Command: 20s
  - Adapter: 20s
  - Composite/Iterator: 20s
  - State: 20s
- **Architecture:** 2 minutes
- **Testing:** 1.5 minutes
- **ADR:** 1 minute
- **Conclusion:** 30 seconds

**Total: 8 minutes**

---

## ✅ Pre-Recording Checklist

- [x] Project compiled (classes in `target/classes/`)
- [x] Demos tested and working
- [x] Java available (version 21.0.2)
- [ ] IDE ready with Week10 folder open
- [ ] Key files bookmarked (OrderController, EventBus, ADR)
- [ ] Terminal ready for demo commands
- [ ] Screen recording software ready
- [ ] Microphone tested
- [ ] Screen resolution: 1080p or higher

---

## 💡 Recording Tips

1. **Follow the script word-for-word** - It's timed for 8 minutes
2. **Highlight exact lines** - Use cursor or IDE highlighting
3. **Don't read code line-by-line** - Show structure, explain concepts
4. **Keep moving** - Don't spend too long on one pattern
5. **Show demos** - Running code is impressive
6. **Practice once** - Run through the script before recording

---

**Good luck with your video! 🎥**
