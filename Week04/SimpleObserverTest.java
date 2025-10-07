import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleObserverTest {

    @Test
    void observers_notified_on_item_add() {
        Product product = new SimpleProduct("A", "A", Money.of(2));
        Order order = new Order(1);
        order.addItem(new LineItem(product, 1));

        List<String> events = new ArrayList<>();
        order.register((orderObj, eventType) -> events.add(eventType));

        order.addItem(new LineItem(product, 1));

        assertTrue(events.contains("itemAdded"));
    }

    @Test
    void multiple_observers_receive_event() {
        Order order = new Order(2);

        List<String> events1 = new ArrayList<>();
        List<String> events2 = new ArrayList<>();

        order.register((orderObj, eventType) -> events1.add(eventType));
        order.register((orderObj, eventType) -> events2.add(eventType));

        order.markReady();

        assertTrue(events1.contains("ready"));
        assertTrue(events2.contains("ready"));
    }
}
