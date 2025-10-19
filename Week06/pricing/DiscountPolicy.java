/**
 * DiscountPolicy - Strategy interface for different discount types.
 * This replaces the primitive string-based discount codes.
 * 
 * Refactoring Applied: Extract Interface, Replace Conditional with Polymorphism
 * SOLID Principle: Open/Closed Principle - open for extension, closed for modification
 */
public interface DiscountPolicy {
    /**
     * Calculate the discount amount for the given subtotal.
     * 
     * @param subtotal the subtotal before discount
     * @return the discount amount (non-negative, capped at subtotal)
     */
    Money discountOf(Money subtotal);
    
    /**
     * Get a human-readable description of this discount.
     * 
     * @return description string for receipt
     */
    String description();
}

