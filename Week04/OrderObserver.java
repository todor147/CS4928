public interface OrderObserver {
    void updated(Order order, String eventType);
}
