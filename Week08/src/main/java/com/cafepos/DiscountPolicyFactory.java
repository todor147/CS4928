package com.cafepos;

import com.cafepos.common.Money;
import com.cafepos.pricing.DiscountPolicy;
import com.cafepos.pricing.FixedCouponDiscount;
import com.cafepos.pricing.LoyaltyPercentDiscount;
import com.cafepos.pricing.NoDiscount;

public final class DiscountPolicyFactory {
    public static DiscountPolicy createDiscountPolicy(String discountCode) {
        if (discountCode == null) {
            return new NoDiscount();
        }
        
        switch (discountCode.toUpperCase()) {
            case "LOYAL5":
                return new LoyaltyPercentDiscount(5);
            case "COUPON1":
                return new FixedCouponDiscount(Money.of(1.00));
            case "NONE":
            default:
                return new NoDiscount();
        }
    }
}
