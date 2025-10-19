package com.cafepos.pricing;

import com.cafepos.common.Money;

public class PricingServiceTests {

    public void applies_discount_and_tax_correctly() {
        DiscountPolicy d = new LoyaltyPercentDiscount(10);
        TaxPolicy t = new FixedRateTaxPolicy(10);
        PricingService ps = new PricingService(d, t);

        Money subtotal = Money.of(100.0);
        PricingService.PricingResult pr = ps.price(subtotal);

        assertTrue(Money.of(100.0).equals(pr.subtotal()));
        assertTrue(Money.of(10.0).equals(pr.discount()));
        assertTrue(Money.of(9.0).equals(pr.tax())); // tax on 90.0
        assertTrue(Money.of(99.0).equals(pr.total()));
    }

    public void no_discount_with_tax() {
        DiscountPolicy d = new NoDiscount();
        TaxPolicy t = new FixedRateTaxPolicy(15);
        PricingService ps = new PricingService(d, t);

        Money subtotal = Money.of(100.0);
        PricingService.PricingResult pr = ps.price(subtotal);

        assertTrue(Money.of(100.0).equals(pr.subtotal()));
        assertTrue(Money.zero().equals(pr.discount()));
        assertTrue(Money.of(15.0).equals(pr.tax()));
        assertTrue(Money.of(115.0).equals(pr.total()));
    }

    public void fixed_coupon_discount_with_tax() {
        DiscountPolicy d = new FixedCouponDiscount(Money.of(5.0));
        TaxPolicy t = new FixedRateTaxPolicy(20);
        PricingService ps = new PricingService(d, t);

        Money subtotal = Money.of(50.0);
        PricingService.PricingResult pr = ps.price(subtotal);

        assertTrue(Money.of(50.0).equals(pr.subtotal()));
        assertTrue(Money.of(5.0).equals(pr.discount()));
        assertTrue(Money.of(9.0).equals(pr.tax())); // tax on 45.0
        assertTrue(Money.of(54.0).equals(pr.total()));
    }

    public void discount_exceeds_subtotal() {
        DiscountPolicy d = new FixedCouponDiscount(Money.of(15.0));
        TaxPolicy t = new FixedRateTaxPolicy(10);
        PricingService ps = new PricingService(d, t);

        Money subtotal = Money.of(10.0);
        PricingService.PricingResult pr = ps.price(subtotal);

        assertTrue(Money.of(10.0).equals(pr.subtotal()));
        assertTrue(Money.of(10.0).equals(pr.discount())); // capped at subtotal
        assertTrue(Money.zero().equals(pr.tax())); // tax on 0.0
        assertTrue(Money.zero().equals(pr.total()));
    }

    private void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError("Assertion failed");
        }
    }
}
