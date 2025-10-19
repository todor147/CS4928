/**
 * ReceiptPaymentStrategy - Strategy interface for payment method display on receipts.
 * 
 * Refactoring Applied: Replace Conditional with Polymorphism
 * SOLID Principle: Open/Closed Principle - can add new payment methods without modifying existing code
 */
public interface ReceiptPaymentStrategy {
    /**
     * Get the payment method description for the receipt.
     * 
     * @return payment method description
     */
    String getDescription();
}

