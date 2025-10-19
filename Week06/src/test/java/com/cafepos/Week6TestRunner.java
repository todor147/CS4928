package com.cafepos;

import com.cafepos.pricing.DiscountPolicyTests;
import com.cafepos.pricing.TaxPolicyTests;
import com.cafepos.pricing.PricingServiceTests;

public class Week6TestRunner {
    public static void main(String[] args) {
        System.out.println("=== Week 6 Complete Test Suite ===");
        
        // Run characterization tests
        System.out.println("\n--- Part 1: Characterization Tests ---");
        Week6CharacterizationTests charTests = new Week6CharacterizationTests();
        runTest("no_discount_cash_payment", () -> charTests.no_discount_cash_payment());
        runTest("loyalty_discount_card_payment", () -> charTests.loyalty_discount_card_payment());
        runTest("coupon_fixed_amount_and_qty_clamp", () -> charTests.coupon_fixed_amount_and_qty_clamp());
        
        // Run unit tests for extracted components
        System.out.println("\n--- Part 5: Unit Tests for Extracted Components ---");
        
        System.out.println("\n--- DiscountPolicy Tests ---");
        DiscountPolicyTests discountTests = new DiscountPolicyTests();
        runTest("no_discount_returns_zero", () -> discountTests.no_discount_returns_zero());
        runTest("loyalty_percent_discount_calculates_correctly", () -> discountTests.loyalty_percent_discount_calculates_correctly());
        runTest("loyalty_percent_discount_with_zero_percent", () -> discountTests.loyalty_percent_discount_with_zero_percent());
        runTest("fixed_coupon_discount_returns_amount", () -> discountTests.fixed_coupon_discount_returns_amount());
        runTest("fixed_coupon_discount_capped_at_subtotal", () -> discountTests.fixed_coupon_discount_capped_at_subtotal());
        
        System.out.println("\n--- TaxPolicy Tests ---");
        TaxPolicyTests taxTests = new TaxPolicyTests();
        runTest("fixed_rate_tax_calculates_correctly", () -> taxTests.fixed_rate_tax_calculates_correctly());
        runTest("fixed_rate_tax_with_zero_percent", () -> taxTests.fixed_rate_tax_with_zero_percent());
        runTest("fixed_rate_tax_with_high_percent", () -> taxTests.fixed_rate_tax_with_high_percent());
        
        System.out.println("\n--- PricingService Tests ---");
        PricingServiceTests pricingTests = new PricingServiceTests();
        runTest("applies_discount_and_tax_correctly", () -> pricingTests.applies_discount_and_tax_correctly());
        runTest("no_discount_with_tax", () -> pricingTests.no_discount_with_tax());
        runTest("fixed_coupon_discount_with_tax", () -> pricingTests.fixed_coupon_discount_with_tax());
        runTest("discount_exceeds_subtotal", () -> pricingTests.discount_exceeds_subtotal());
        
        System.out.println("\n=== All Tests Completed Successfully ===");
    }
    
    private static void runTest(String testName, Runnable test) {
        try {
            System.out.println("Running: " + testName);
            test.run();
            System.out.println("✓ PASSED");
        } catch (AssertionError e) {
            System.out.println("✗ FAILED: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.out.println("✗ ERROR: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
