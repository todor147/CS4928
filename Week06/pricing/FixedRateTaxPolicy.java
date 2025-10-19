import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * FixedRateTaxPolicy - Applies a fixed percentage tax rate.
 * 
 * Refactoring Applied: Extract Class
 * SOLID Principle: Single Responsibility - only responsible for tax calculation
 */
public final class FixedRateTaxPolicy implements TaxPolicy {
    private final double percentage;
    
    public FixedRateTaxPolicy(double percentage) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("Tax percentage must be between 0 and 100");
        }
        this.percentage = percentage;
    }
    
    @Override
    public Money taxOn(Money amount) {
        BigDecimal taxAmount = amount.getAmount()
                .multiply(BigDecimal.valueOf(percentage / 100.0))
                .setScale(2, RoundingMode.HALF_UP);
        return Money.of(taxAmount.doubleValue());
    }
    
    @Override
    public double getPercentage() {
        return percentage;
    }
}

