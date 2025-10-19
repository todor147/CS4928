/**
 * TaxPolicy - Strategy interface for tax calculation.
 * This replaces hardcoded tax percentages.
 * 
 * Refactoring Applied: Extract Interface
 * SOLID Principle: Single Responsibility, Dependency Inversion
 */
public interface TaxPolicy {
    /**
     * Calculate the tax amount for the given amount.
     * 
     * @param amount the amount to calculate tax on
     * @return the tax amount
     */
    Money taxOn(Money amount);
    
    /**
     * Get the tax percentage for display purposes.
     * 
     * @return tax percentage as a double
     */
    double getPercentage();
}

