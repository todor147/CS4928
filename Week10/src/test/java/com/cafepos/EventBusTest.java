package com.cafepos;

import com.cafepos.app.events.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EventBusTest {
    @Test
    void testEmitOrderCreated() {
        EventBus bus = new EventBus();
        List<Long> receivedIds = new ArrayList<>();
        
        bus.on(OrderCreated.class, e -> receivedIds.add(e.orderId()));
        
        bus.emit(new OrderCreated(1001L));
        bus.emit(new OrderCreated(1002L));
        
        assertEquals(2, receivedIds.size());
        assertEquals(1001L, receivedIds.get(0));
        assertEquals(1002L, receivedIds.get(1));
    }

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

    @Test
    void testDifferentEventTypes() {
        EventBus bus = new EventBus();
        List<String> events = new ArrayList<>();
        
        bus.on(OrderCreated.class, e -> events.add("Created: " + e.orderId()));
        bus.on(OrderPaid.class, e -> events.add("Paid: " + e.orderId()));
        
        bus.emit(new OrderCreated(3001L));
        bus.emit(new OrderPaid(3001L));
        
        assertEquals(2, events.size());
        assertTrue(events.contains("Created: 3001"));
        assertTrue(events.contains("Paid: 3001"));
    }

    @Test
    void testNoHandlersDoesNotThrow() {
        EventBus bus = new EventBus();
        // Should not throw if no handlers registered
        assertDoesNotThrow(() -> {
            bus.emit(new OrderCreated(4001L));
        });
    }

    @Test
    void testHandlerOnlyReceivesMatchingEventType() {
        EventBus bus = new EventBus();
        List<Long> createdIds = new ArrayList<>();
        List<Long> paidIds = new ArrayList<>();
        
        bus.on(OrderCreated.class, e -> createdIds.add(e.orderId()));
        bus.on(OrderPaid.class, e -> paidIds.add(e.orderId()));
        
        bus.emit(new OrderCreated(5001L));
        bus.emit(new OrderPaid(5002L));
        
        assertEquals(1, createdIds.size());
        assertEquals(5001L, createdIds.get(0));
        assertEquals(1, paidIds.size());
        assertEquals(5002L, paidIds.get(0));
    }
}


