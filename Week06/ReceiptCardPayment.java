/**
 * ReceiptCardPayment - Card payment display for receipts.
 */
public final class ReceiptCardPayment implements ReceiptPaymentStrategy {
    @Override
    public String getDescription() {
        return "Payment: Card";
    }
}

