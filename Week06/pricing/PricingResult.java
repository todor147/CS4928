/**
 * PricingResult - Immutable record holding pricing calculation results.
 * 
 * Refactoring Applied: Introduce Parameter Object
 * SOLID Principle: Single Responsibility - encapsulates pricing data
 */
public record PricingResult(
    Money subtotal,
    Money discountAmount,
    String discountDescription,
    Money afterDiscount,
    Money taxAmount,
    Money finalTotal
) {
    public PricingResult {
        if (subtotal == null || discountAmount == null || discountDescription == null ||
            afterDiscount == null || taxAmount == null || finalTotal == null) {
            throw new IllegalArgumentException("All pricing fields are required");
        }
    }
}

