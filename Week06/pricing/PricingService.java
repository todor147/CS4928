/**
 * PricingService - Orchestrates discount and tax calculations.
 * 
 * Refactoring Applied: Extract Class, Dependency Injection
 * SOLID Principles:
 *   - Single Responsibility: only responsible for pricing calculations
 *   - Dependency Inversion: depends on abstractions (DiscountPolicy, TaxPolicy)
 *   - Open/Closed: open for extension via new policies, closed for modification
 */
public final class PricingService {
    private final DiscountPolicy discountPolicy;
    private final TaxPolicy taxPolicy;
    
    public PricingService(DiscountPolicy discountPolicy, TaxPolicy taxPolicy) {
        if (discountPolicy == null) {
            throw new IllegalArgumentException("Discount policy is required");
        }
        if (taxPolicy == null) {
            throw new IllegalArgumentException("Tax policy is required");
        }
        this.discountPolicy = discountPolicy;
        this.taxPolicy = taxPolicy;
    }
    
    /**
     * Calculate complete pricing for a subtotal.
     * 
     * @param subtotal the subtotal before discounts and taxes
     * @return complete pricing result
     */
    public PricingResult calculate(Money subtotal) {
        Money discountAmount = discountPolicy.discountOf(subtotal);
        String discountDescription = discountPolicy.description();
        
        Money afterDiscount = subtotal.subtract(discountAmount);
        Money taxAmount = taxPolicy.taxOn(afterDiscount);
        Money finalTotal = afterDiscount.add(taxAmount);
        
        return new PricingResult(
            subtotal,
            discountAmount,
            discountDescription,
            afterDiscount,
            taxAmount,
            finalTotal
        );
    }
}

