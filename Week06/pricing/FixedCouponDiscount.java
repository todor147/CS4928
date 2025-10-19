/**
 * FixedCouponDiscount - Applies a fixed dollar amount discount.
 * 
 * Refactoring Applied: Extract Class
 * SOLID Principle: Single Responsibility - only responsible for fixed discount calculation
 */
public final class FixedCouponDiscount implements DiscountPolicy {
    private final Money fixedAmount;
    
    public FixedCouponDiscount(Money fixedAmount) {
        if (fixedAmount == null) {
            throw new IllegalArgumentException("Fixed amount cannot be null");
        }
        this.fixedAmount = fixedAmount;
    }
    
    @Override
    public Money discountOf(Money subtotal) {
        // Cap discount at subtotal (can't discount more than the total)
        if (fixedAmount.compareTo(subtotal) > 0) {
            return subtotal;
        }
        return fixedAmount;
    }
    
    @Override
    public String description() {
        return "Coupon " + fixedAmount.toString();
    }
}

