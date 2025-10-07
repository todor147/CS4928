import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

public class ObserverPatternTest {

    private static class FakeObserver implements OrderObserver {
        private final List<String> events = new ArrayList<>();
        private final List<Order> orders = new ArrayList<>();

        @Override
        public void updated(Order order, String eventType) {
            events.add(eventType);
            orders.add(order);
        }

        public List<String> getEvents() {
            return new ArrayList<>(events);
        }

        public List<Order> getOrders() {
            return new ArrayList<>(orders);
        }

        public void clear() {
            events.clear();
            orders.clear();
        }
    }

    @Test
    void observers_notified_on_item_add() {
        Product product = new SimpleProduct("TEST", "Test Product", Money.of(10.0));
        Order order = new Order(1);
        FakeObserver observer = new FakeObserver();

        order.register(observer);
        order.addItem(new LineItem(product, 1));

        List<String> events = observer.getEvents();
        assertEquals(1, events.size());
        assertEquals("itemAdded", events.get(0));
    }

    @Test
    void observers_notified_on_payment() {
        Product product = new SimpleProduct("TEST", "Test Product", Money.of(10.0));
        Order order = new Order(2);
        FakeObserver observer = new FakeObserver();

        order.register(observer);
        order.addItem(new LineItem(product, 1));
        observer.clear();

        order.pay(new CashPayment());

        List<String> events = observer.getEvents();
        assertEquals(1, events.size());
        assertEquals("paid", events.get(0));
    }

    @Test
    void observers_notified_on_ready() {
        Order order = new Order(3);
        FakeObserver observer = new FakeObserver();

        order.register(observer);
        order.markReady();

        List<String> events = observer.getEvents();
        assertEquals(1, events.size());
        assertEquals("ready", events.get(0));
    }

    @Test
    void multiple_observers_receive_event() {
        Order order = new Order(4);
        FakeObserver observer1 = new FakeObserver();
        FakeObserver observer2 = new FakeObserver();

        order.register(observer1);
        order.register(observer2);
        order.markReady();

        List<String> events1 = observer1.getEvents();
        List<String> events2 = observer2.getEvents();

        assertEquals(1, events1.size());
        assertEquals(1, events2.size());
        assertEquals("ready", events1.get(0));
        assertEquals("ready", events2.get(0));
    }

    @Test
    void unregistered_observer_does_not_receive_event() {
        Order order = new Order(5);
        FakeObserver observer = new FakeObserver();

        order.register(observer);
        order.unregister(observer);
        order.markReady();

        List<String> events = observer.getEvents();
        assertEquals(0, events.size());
    }

    @Test
    void duplicate_observer_registration() {
        Order order = new Order(6);
        FakeObserver observer = new FakeObserver();

        order.register(observer);
        order.register(observer);
        order.markReady();

        List<String> events = observer.getEvents();
        assertEquals(1, events.size());
        assertEquals("ready", events.get(0));
    }

    @Test
    void observer_event_order() {
        Product product = new SimpleProduct("TEST", "Test Product", Money.of(10.0));
        Order order = new Order(7);
        FakeObserver observer = new FakeObserver();

        order.register(observer);
        order.addItem(new LineItem(product, 1));
        order.pay(new CashPayment());
        order.markReady();

        List<String> events = observer.getEvents();
        assertEquals(3, events.size());
        assertEquals("itemAdded", events.get(0));
        assertEquals("paid", events.get(1));
        assertEquals("ready", events.get(2));
    }

    @Test
    void null_observer_registration_throws_exception() {
        Order order = new Order(8);

        assertThrows(IllegalArgumentException.class, () -> order.register(null));
    }

    @Test
    void unregister_non_existent_observer() {
        Order order = new Order(9);
        FakeObserver observer = new FakeObserver();

        assertDoesNotThrow(() -> order.unregister(observer));
    }
}
