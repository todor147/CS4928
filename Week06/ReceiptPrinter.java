/**
 * ReceiptPrinter - Formats receipts for display.
 * 
 * Refactoring Applied: Extract Class, Single Responsibility
 * SOLID Principle: Single Responsibility - only responsible for receipt formatting
 */
public final class ReceiptPrinter {
    
    /**
     * Format a receipt with all order details.
     * 
     * @param productName the name of the product ordered
     * @param quantity the quantity ordered
     * @param unitPrice the unit price of the product
     * @param result the pricing calculation result
     * @param taxPercent the tax percentage for display
     * @return formatted receipt string
     */
    public String format(String productName, int quantity, Money unitPrice, 
                        PricingResult result, double taxPercent) {
        StringBuilder receipt = new StringBuilder();
        receipt.append("=== RECEIPT ===\n");
        receipt.append("Item: ").append(productName).append("\n");
        receipt.append("Quantity: ").append(quantity).append("\n");
        receipt.append("Unit Price: $").append(unitPrice.getAmount().toString()).append("\n");
        receipt.append("Subtotal: $").append(result.subtotal().getAmount().toString()).append("\n");
        receipt.append("Discount: ").append(result.discountDescription())
               .append(" -$").append(String.format("%.2f", result.discountAmount().getAmount())).append("\n");
        receipt.append("Tax (").append(String.format("%.1f", taxPercent))
               .append("%): $").append(result.taxAmount().getAmount().toString()).append("\n");
        receipt.append("Total: $").append(result.finalTotal().getAmount().toString()).append("\n");
        return receipt.toString();
    }
    
    /**
     * Append payment method information to a receipt.
     * 
     * @param receipt the existing receipt string
     * @param paymentMethodDescription the payment method description
     * @return receipt with payment method appended
     */
    public String appendPayment(String receipt, String paymentMethodDescription) {
        return receipt + paymentMethodDescription + "\n===============";
    }
}

