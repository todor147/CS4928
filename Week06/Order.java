import java.util.ArrayList;
import java.util.List;
import com.cafepos.common.Money;
import com.cafepos.payment.PaymentStrategy;

public final class Order implements OrderPublisher {
    private final long id;
    private final List<LineItem> items = new ArrayList<>();
    private final List<OrderObserver> observers = new ArrayList<>();

    public Order(long id) {
        this.id = id;
    }

    public void addItem(LineItem li) {
        if (li == null)
            throw new IllegalArgumentException("line item cannot be null");
        if (li.quantity() <= 0)
            throw new IllegalArgumentException("quantity must be > 0");
        items.add(li);
        notifyObservers(this, "itemAdded");
    }

    public Money subtotal() {
        return items.stream()
                .map(LineItem::lineTotal)
                .reduce(Money.zero(), Money::add);
    }

    public Money taxAtPercent(int percent) {
        if (percent < 0)
            throw new IllegalArgumentException("tax percent cannot be negative");
        Money subtotal = subtotal();
        if (subtotal.equals(Money.zero())) {
            return Money.zero();
        }
        java.math.BigDecimal taxRate = java.math.BigDecimal.valueOf(percent).divide(java.math.BigDecimal.valueOf(100));
        java.math.BigDecimal taxAmount = subtotal.getAmount().multiply(taxRate);
        return Money.of(taxAmount.doubleValue());
    }

    public Money totalWithTax(int percent) {
        return subtotal().add(taxAtPercent(percent));
    }

    public long id() {
        return id;
    }

    public List<LineItem> items() {
        return new ArrayList<>(items);
    }

    public int getItemCount() {
        return items.size();
    }

    public void pay(PaymentStrategy strategy) {
        if (strategy == null)
            throw new IllegalArgumentException("strategy required");
        strategy.pay(this);
        notifyObservers(this, "paid");
    }

    public void markReady() {
        notifyObservers(this, "ready");
    }

    // Observer management methods
    @Override
    public void register(OrderObserver o) {
        if (o == null) {
            throw new IllegalArgumentException("observer cannot be null");
        }
        if (!observers.contains(o)) {
            observers.add(o);
        }
    }

    @Override
    public void unregister(OrderObserver o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(Order order, String eventType) {
        for (OrderObserver observer : observers) {
            observer.updated(order, eventType);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Order order = (Order) obj;
        return id == order.id && items.equals(order.items);
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id) + items.hashCode();
    }

    @Override
    public String toString() {
        return "Order{id=" + id + ", items=" + items.size() + ", subtotal=" + subtotal() + "}";
    }
}

