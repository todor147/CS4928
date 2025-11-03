package com.cafepos.pricing;

import com.cafepos.common.Money;

public class DiscountPolicyTests {

    public void no_discount_returns_zero() {
        DiscountPolicy d = new NoDiscount();
        Money subtotal = Money.of(10.0);
        Money discount = d.discountOf(subtotal);
        assertTrue(Money.zero().equals(discount));
    }

    public void loyalty_percent_discount_calculates_correctly() {
        DiscountPolicy d = new LoyaltyPercentDiscount(10);
        Money subtotal = Money.of(100.0);
        Money discount = d.discountOf(subtotal);
        assertTrue(Money.of(10.0).equals(discount));
    }

    public void loyalty_percent_discount_with_zero_percent() {
        DiscountPolicy d = new LoyaltyPercentDiscount(0);
        Money subtotal = Money.of(100.0);
        Money discount = d.discountOf(subtotal);
        assertTrue(Money.zero().equals(discount));
    }

    public void fixed_coupon_discount_returns_amount() {
        DiscountPolicy d = new FixedCouponDiscount(Money.of(5.0));
        Money subtotal = Money.of(100.0);
        Money discount = d.discountOf(subtotal);
        assertTrue(Money.of(5.0).equals(discount));
    }

    public void fixed_coupon_discount_capped_at_subtotal() {
        DiscountPolicy d = new FixedCouponDiscount(Money.of(15.0));
        Money subtotal = Money.of(10.0);
        Money discount = d.discountOf(subtotal);
        assertTrue(Money.of(10.0).equals(discount));
    }

    private void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError("Assertion failed");
        }
    }
}
