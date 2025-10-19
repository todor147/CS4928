/**
 * ReceiptCashPayment - Cash payment display for receipts.
 */
public final class ReceiptCashPayment implements ReceiptPaymentStrategy {
    @Override
    public String getDescription() {
        return "Payment: Cash";
    }
}

