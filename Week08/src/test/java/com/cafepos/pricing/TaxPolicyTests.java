package com.cafepos.pricing;

import com.cafepos.common.Money;

public class TaxPolicyTests {

    public void fixed_rate_tax_calculates_correctly() {
        TaxPolicy t = new FixedRateTaxPolicy(10);
        Money amount = Money.of(100.0);
        Money tax = t.taxOn(amount);
        assertTrue(Money.of(10.0).equals(tax));
    }

    public void fixed_rate_tax_with_zero_percent() {
        TaxPolicy t = new FixedRateTaxPolicy(0);
        Money amount = Money.of(100.0);
        Money tax = t.taxOn(amount);
        assertTrue(Money.zero().equals(tax));
    }

    public void fixed_rate_tax_with_high_percent() {
        TaxPolicy t = new FixedRateTaxPolicy(25);
        Money amount = Money.of(100.0);
        Money tax = t.taxOn(amount);
        assertTrue(Money.of(25.0).equals(tax));
    }

    private void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError("Assertion failed");
        }
    }
}
