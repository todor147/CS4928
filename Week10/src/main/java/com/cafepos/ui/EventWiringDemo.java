package com.cafepos.ui;

import com.cafepos.app.events.*;
import com.cafepos.infra.Wiring;

/**
 * Demonstrates event-driven component communication via EventBus.
 * Shows how components can subscribe to events without tight coupling.
 */
public final class EventWiringDemo {
    public static void main(String[] args) {
        System.out.println("=== EventBus Component Connector Demo ===\n");
        
        // Create EventBus connector
        var bus = new EventBus();
        
        // Wire up components
        var comp = Wiring.createDefault();
        var controller = new OrderController(comp.repo(), comp.checkout());
        
        // Register multiple handlers for OrderCreated event
        bus.on(OrderCreated.class, e -> 
            System.out.println("[UI Handler] Order created: " + e.orderId()));
        bus.on(OrderCreated.class, e -> 
            System.out.println("[Analytics Handler] Logging order creation: " + e.orderId()));
        
        // Register handler for OrderPaid event
        bus.on(OrderPaid.class, e -> 
            System.out.println("[Notification Handler] Order paid: " + e.orderId() + " - sending receipt"));
        
        System.out.println("Creating order and emitting events...\n");
        
        // Create an order through controller
        long id = 4201L;
        controller.createOrder(id);
        
        // Emit OrderCreated event (in real system, this would be emitted by the controller/service)
        bus.emit(new OrderCreated(id));
        
        // Add items
        controller.addItem(id, "ESP+SHOT", 1);
        controller.addItem(id, "LAT", 2);
        
        // Simulate checkout
        String receipt = controller.checkout(id, 10);
        
        // Emit OrderPaid event (in real system, after payment processing)
        bus.emit(new OrderPaid(id));
        
        System.out.println("\n=== Receipt ===");
        System.out.println(receipt);
        
        System.out.println("\n=== Key Points ===");
        System.out.println("✓ Multiple handlers can subscribe to the same event");
        System.out.println("✓ Handlers are decoupled from event publishers");
        System.out.println("✓ Easy to add new handlers without modifying existing code");
        System.out.println("✓ EventBus maintains architectural boundaries between layers");
    }
}

