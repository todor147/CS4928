package com.cafepos;

public class TestRunner {
    public static void main(String[] args) {
        System.out.println("=== Week 6 Characterization Tests ===");
        
        Week6CharacterizationTests tests = new Week6CharacterizationTests();
        
        try {
            System.out.println("Running: no_discount_cash_payment");
            tests.no_discount_cash_payment();
            System.out.println("✓ PASSED");
        } catch (Exception e) {
            System.out.println("✗ FAILED: " + e.getMessage());
        }
        
        try {
            System.out.println("Running: loyalty_discount_card_payment");
            tests.loyalty_discount_card_payment();
            System.out.println("✓ PASSED");
        } catch (Exception e) {
            System.out.println("✗ FAILED: " + e.getMessage());
        }
        
        try {
            System.out.println("Running: coupon_fixed_amount_and_qty_clamp");
            tests.coupon_fixed_amount_and_qty_clamp();
            System.out.println("✓ PASSED");
        } catch (Exception e) {
            System.out.println("✗ FAILED: " + e.getMessage());
        }
        
        System.out.println("\n=== All tests completed ===");
    }
}
