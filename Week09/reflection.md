Week 9 Reflection

Composite Pattern: Safety vs Transparency

In the Composite pattern implementation, I chose **safety over transparency** in the API design. The `MenuComponent` abstract base class provides default implementations for all operations that throw `UnsupportedOperationException` for unsupported operations.

Why Safety?

1.Type Safety: By throwing exceptions for unsupported operations, we catch errors at runtime if a client tries to call `add()` on a `MenuItem` or `price()` on a `Menu`. This prevents silent failures and makes bugs immediately apparent.

2.Clear Contract: The API clearly communicates what operations are valid for each component type. A `MenuItem` cannot have children, and a `Menu` does not have a price. The exception messages make this explicit.

3.Easier Debugging: When an invalid operation is attempted, the exception provides a clear stack trace pointing to the exact location of the error, rather than having to trace through complex conditional logic.

4.Future-Proofing: If we need to add new component types in the future, the base class provides a safe default that forces explicit implementation decisions.

Trade-offs

The alternative "transparent" approach would allow operations like `menu.add(item)` and `item.add(otherItem)` to silently fail or return false. However, this hides errors and makes debugging more difficult. The safety approach makes the API more explicit about what is and isn't allowed.

State Pattern: What Makes It Easy

The State pattern makes several things easy that were awkward with conditionals:

1. **Clear State Transitions**

With conditionals, you'd have code like:
```java
if (state.equals("NEW")) {
    if (action.equals("pay")) {
        state = "PREPARING";
    } else if (action.equals("prepare")) {
        // error
    }
} else if (state.equals("PREPARING")) {
    // more nested conditionals
}
```

With the State pattern, each state encapsulates its own behavior:
```java
public void pay(OrderFSM context) {
    System.out.println("[NEW] Payment received. Moving to PREPARING.");
    context.setState(new PreparingState());
}
```

2. Easy to Add New States

Adding a new state (e.g., "PAID" between NEW and PREPARING) requires:
- Creating a new state class
- Implementing the interface
- Updating transitions in related states

No need to modify existing conditional chains or risk breaking existing logic.

3. Self-Documenting Code

Each state class clearly shows what transitions are valid from that state. The `NewState` class shows that only `pay()` and `cancel()` are valid, making the state machine behavior immediately obvious.

4. Single Responsibility

Each state class has one responsibility: managing transitions from that specific state. This makes the code easier to understand, test, and maintain.

 5. No Magic Strings

With conditionals, you'd use string comparisons like `if (state.equals("NEW"))`. With the State pattern, you use type-safe state objects, reducing the risk of typos and making refactoring easier.

6. Easy Testing

Each state can be tested independently. You can test that `NewState.pay()` transitions correctly without worrying about the entire state machine.

7. Behavior Encapsulation

Each state encapsulates its own behavior and error messages. If you need to change what happens when you try to `prepare()` from `NEW`, you only modify `NewState`, not a large conditional block.

The State pattern transforms a complex web of conditionals into a clean, extensible, and maintainable design where each state is responsible for its own behavior.


