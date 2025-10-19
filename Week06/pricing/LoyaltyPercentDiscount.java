import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * LoyaltyPercentDiscount - Applies a percentage-based discount.
 * 
 * Refactoring Applied: Extract Class
 * SOLID Principle: Single Responsibility - only responsible for percentage discount calculation
 */
public final class LoyaltyPercentDiscount implements DiscountPolicy {
    private final double percentage;
    private final String label;
    
    public LoyaltyPercentDiscount(double percentage, String label) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Percentage must be between 0 and 100");
        }
        this.percentage = percentage;
        this.label = label;
    }
    
    @Override
    public Money discountOf(Money subtotal) {
        BigDecimal discountAmount = subtotal.getAmount()
                .multiply(BigDecimal.valueOf(percentage / 100.0))
                .setScale(2, RoundingMode.HALF_UP);
        return Money.of(discountAmount.doubleValue());
    }
    
    @Override
    public String description() {
        return label + " " + (int)percentage + "%";
    }
}

