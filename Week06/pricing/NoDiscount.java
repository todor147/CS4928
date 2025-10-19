/**
 * NoDiscount - Represents no discount applied.
 * 
 * Refactoring Applied: Introduce Null Object pattern
 */
public final class NoDiscount implements DiscountPolicy {
    
    @Override
    public Money discountOf(Money subtotal) {
        return Money.zero();
    }
    
    @Override
    public String description() {
        return "None";
    }
}

