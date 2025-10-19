package tests;

import smells.OrderManagerGod;

/**
 * Characterization Tests for OrderManagerGod
 * 
 * These tests lock in the existing behavior of the smelly code BEFORE refactoring.
 * They test the exact output format, pricing calculations, discount logic, and payment handling.
 * 
 * ALL TESTS MUST BE GREEN before any refactoring begins.
 * After refactoring, the new implementation must produce identical output.
 */
public final class Week6CharacterizationTests {

    // Test 1: No discount, cash payment
    public static void test_no_discount_cash_payment() {
        OrderManagerGod.resetLastDiscountCode();
        
        String receipt = OrderManagerGod.process("ESP", 1, null, "cash");
        
        // Assert key receipt components
        assert receipt.contains("CAFÉ POS RECEIPT") : "Receipt should contain header";
        assert receipt.contains("Espresso") : "Receipt should contain product name";
        assert receipt.contains("Quantity:                  1") : "Receipt should show quantity 1";
        assert receipt.contains("Unit Price:                $2.50") : "Receipt should show unit price $2.50";
        assert receipt.contains("Subtotal:                  $2.50") : "Receipt should show subtotal $2.50";
        assert receipt.contains("Tax (10%):                  $0.25") : "Receipt should show tax $0.25";
        assert receipt.contains("TOTAL:                     $2.75") : "Receipt should show total $2.75";
        assert receipt.contains("Payment: CASH") : "Receipt should show cash payment";
        assert !receipt.contains("Discount") : "Receipt should not contain discount line";
        
        System.out.println("✓ test_no_discount_cash_payment passed");
    }

    // Test 2: Loyalty discount (10%), card payment
    public static void test_loyalty_discount_card_payment() {
        OrderManagerGod.resetLastDiscountCode();
        
        String receipt = OrderManagerGod.process("LAT", 2, "LOYALTY10", "card");
        
        // Subtotal = 3.20 * 2 = 6.40
        // Discount = 6.40 * 0.10 = 0.64
        // After discount = 6.40 - 0.64 = 5.76
        // Tax = 5.76 * 0.10 = 0.58
        // Total = 5.76 + 0.58 = 6.34
        
        assert receipt.contains("Latte") : "Receipt should contain product name";
        assert receipt.contains("Quantity:                  2") : "Receipt should show quantity 2";
        assert receipt.contains("Unit Price:                $3.20") : "Receipt should show unit price $3.20";
        assert receipt.contains("Subtotal:                  $6.40") : "Receipt should show subtotal $6.40";
        assert receipt.contains("Discount (LOYALTY10):      -$0.64") : "Receipt should show 10% discount";
        assert receipt.contains("Tax (10%):                  $0.58") : "Receipt should show tax on discounted amount";
        assert receipt.contains("TOTAL:                     $6.34") : "Receipt should show final total $6.34";
        assert receipt.contains("Payment: CARD") : "Receipt should show card payment";
        assert receipt.contains("Card charged: $6.34") : "Receipt should show card charge amount";
        
        System.out.println("✓ test_loyalty_discount_card_payment passed");
    }

    // Test 3: Fixed coupon ($5 off), with quantity
    public static void test_fixed_coupon_with_quantity() {
        OrderManagerGod.resetLastDiscountCode();
        
        String receipt = OrderManagerGod.process("CAP+SHOT", 3, "SAVE5", "wallet");
        
        // Unit price = 3.00 + 0.80 = 3.80
        // Subtotal = 3.80 * 3 = 11.40
        // Discount = 5.00 (fixed)
        // After discount = 11.40 - 5.00 = 6.40
        // Tax = 6.40 * 0.10 = 0.64
        // Total = 6.40 + 0.64 = 7.04
        
        assert receipt.contains("Cappuccino + Extra Shot") : "Receipt should show decorated product";
        assert receipt.contains("Quantity:                  3") : "Receipt should show quantity 3";
        assert receipt.contains("Unit Price:                $3.80") : "Receipt should show unit price $3.80";
        assert receipt.contains("Subtotal:                  $11.40") : "Receipt should show subtotal $11.40";
        assert receipt.contains("Discount (SAVE5):          -$5.00") : "Receipt should show $5 discount";
        assert receipt.contains("Tax (10%):                  $0.64") : "Receipt should show tax $0.64";
        assert receipt.contains("TOTAL:                     $7.04") : "Receipt should show total $7.04";
        assert receipt.contains("Payment: WALLET") : "Receipt should show wallet payment";
        
        System.out.println("✓ test_fixed_coupon_with_quantity passed");
    }

    // Test 4: Coupon clamping - discount cannot exceed subtotal
    public static void test_coupon_clamping() {
        OrderManagerGod.resetLastDiscountCode();
        
        String receipt = OrderManagerGod.process("ESP", 1, "SAVE5", null);
        
        // Subtotal = 2.50
        // Discount would be 5.00, but clamped to 2.50 (cannot exceed subtotal)
        // After discount = 2.50 - 2.50 = 0.00
        // Tax = 0.00 * 0.10 = 0.00
        // Total = 0.00 + 0.00 = 0.00
        
        assert receipt.contains("Subtotal:                  $2.50") : "Receipt should show subtotal $2.50";
        assert receipt.contains("Discount (SAVE5):          -$2.50") : "Discount should be clamped to subtotal";
        assert receipt.contains("Tax (10%):                  $0.00") : "Tax should be $0.00 on $0.00";
        assert receipt.contains("TOTAL:                     $0.00") : "Total should be $0.00";
        
        System.out.println("✓ test_coupon_clamping passed");
    }

    // Test 5: Welcome coupon ($2 off)
    public static void test_welcome_coupon() {
        OrderManagerGod.resetLastDiscountCode();
        
        String receipt = OrderManagerGod.process("LAT+L", 1, "WELCOME", "cash");
        
        // Unit price = 3.20 + 0.70 = 3.90
        // Subtotal = 3.90
        // Discount = 2.00
        // After discount = 3.90 - 2.00 = 1.90
        // Tax = 1.90 * 0.10 = 0.19
        // Total = 1.90 + 0.19 = 2.09
        
        assert receipt.contains("Latte (Large)") : "Receipt should show large latte";
        assert receipt.contains("Unit Price:                $3.90") : "Receipt should show unit price $3.90";
        assert receipt.contains("Subtotal:                  $3.90") : "Receipt should show subtotal $3.90";
        assert receipt.contains("Discount (WELCOME):        -$2.00") : "Receipt should show $2 welcome discount";
        assert receipt.contains("Tax (10%):                  $0.19") : "Receipt should show tax $0.19";
        assert receipt.contains("TOTAL:                     $2.09") : "Receipt should show total $2.09";
        
        System.out.println("✓ test_welcome_coupon passed");
    }

    // Test 6: Complex product with multiple decorators
    public static void test_complex_product() {
        OrderManagerGod.resetLastDiscountCode();
        
        String receipt = OrderManagerGod.process("ESP+SHOT+OAT+SYP+L", 2, null, null);
        
        // Unit price = 2.50 + 0.80 + 0.50 + 0.40 + 0.70 = 4.90
        // Subtotal = 4.90 * 2 = 9.80
        // No discount
        // Tax = 9.80 * 0.10 = 0.98
        // Total = 9.80 + 0.98 = 10.78
        
        assert receipt.contains("Espresso + Extra Shot + Oat Milk + Syrup (Large)") : 
            "Receipt should show all decorators";
        assert receipt.contains("Quantity:                  2") : "Receipt should show quantity 2";
        assert receipt.contains("Unit Price:                $4.90") : "Receipt should show unit price $4.90";
        assert receipt.contains("Subtotal:                  $9.80") : "Receipt should show subtotal $9.80";
        assert receipt.contains("Tax (10%):                  $0.98") : "Receipt should show tax $0.98";
        assert receipt.contains("TOTAL:                     $10.78") : "Receipt should show total $10.78";
        
        System.out.println("✓ test_complex_product passed");
    }

    // Test 7: Quantity clamping (0 or negative becomes 1)
    public static void test_quantity_clamping() {
        OrderManagerGod.resetLastDiscountCode();
        
        String receipt1 = OrderManagerGod.process("ESP", 0, null, null);
        assert receipt1.contains("Quantity:                  1") : "Zero quantity should be clamped to 1";
        
        String receipt2 = OrderManagerGod.process("ESP", -5, null, null);
        assert receipt2.contains("Quantity:                  1") : "Negative quantity should be clamped to 1";
        
        System.out.println("✓ test_quantity_clamping passed");
    }

    // Test 8: Invalid discount code (ignored)
    public static void test_invalid_discount_code() {
        OrderManagerGod.resetLastDiscountCode();
        
        String receipt = OrderManagerGod.process("LAT", 1, "INVALID", "cash");
        
        // Invalid code should be ignored, no discount applied
        assert !receipt.contains("Discount") : "Invalid discount code should be ignored";
        assert receipt.contains("Subtotal:                  $3.20") : "Subtotal should be $3.20";
        assert receipt.contains("Tax (10%):                  $0.32") : "Tax should be on full amount";
        assert receipt.contains("TOTAL:                     $3.52") : "Total should be full price + tax";
        
        System.out.println("✓ test_invalid_discount_code passed");
    }

    // Test 9: Empty discount code (treated as null)
    public static void test_empty_discount_code() {
        OrderManagerGod.resetLastDiscountCode();
        
        String receipt = OrderManagerGod.process("CAP", 1, "", "card");
        
        assert !receipt.contains("Discount") : "Empty discount code should be treated as no discount";
        assert receipt.contains("Subtotal:                  $3.00") : "Subtotal should be $3.00";
        assert receipt.contains("Tax (10%):                  $0.30") : "Tax should be on full amount";
        assert receipt.contains("TOTAL:                     $3.30") : "Total should be full price + tax";
        
        System.out.println("✓ test_empty_discount_code passed");
    }

    // Test 10: Wallet payment
    public static void test_wallet_payment() {
        OrderManagerGod.resetLastDiscountCode();
        
        String receipt = OrderManagerGod.process("ESP", 1, null, "wallet");
        
        assert receipt.contains("Payment: WALLET") : "Receipt should show wallet payment";
        assert receipt.contains("Digital wallet charged: $2.75") : "Receipt should show wallet charge";
        assert receipt.contains("Payment successful") : "Receipt should confirm payment success";
        
        System.out.println("✓ test_wallet_payment passed");
    }

    // Test 11: Case insensitivity of discount codes
    public static void test_discount_code_case_insensitive() {
        OrderManagerGod.resetLastDiscountCode();
        
        String receipt1 = OrderManagerGod.process("ESP", 1, "loyalty10", null);
        assert receipt1.contains("Discount (loyalty10)") : "Lowercase discount code should work";
        
        OrderManagerGod.resetLastDiscountCode();
        String receipt2 = OrderManagerGod.process("ESP", 1, "LOYALTY10", null);
        assert receipt2.contains("Discount (LOYALTY10)") : "Uppercase discount code should work";
        
        OrderManagerGod.resetLastDiscountCode();
        String receipt3 = OrderManagerGod.process("ESP", 1, "LoYaLtY10", null);
        assert receipt3.contains("Discount (LoYaLtY10)") : "Mixed case discount code should work";
        
        System.out.println("✓ test_discount_code_case_insensitive passed");
    }

    // Test 12: Global state tracking (LAST_DISCOUNT_CODE)
    public static void test_global_state_tracking() {
        OrderManagerGod.resetLastDiscountCode();
        assert OrderManagerGod.getLastDiscountCode() == null : "Initial state should be null";
        
        OrderManagerGod.process("ESP", 1, "LOYALTY10", null);
        assert "LOYALTY10".equals(OrderManagerGod.getLastDiscountCode()) : 
            "Last discount code should be stored";
        
        OrderManagerGod.process("LAT", 1, "SAVE5", null);
        assert "SAVE5".equals(OrderManagerGod.getLastDiscountCode()) : 
            "Last discount code should be updated";
        
        OrderManagerGod.resetLastDiscountCode();
        assert OrderManagerGod.getLastDiscountCode() == null : "Reset should clear state";
        
        System.out.println("✓ test_global_state_tracking passed");
    }

    // Test 13: No payment method specified
    public static void test_no_payment_method() {
        OrderManagerGod.resetLastDiscountCode();
        
        String receipt = OrderManagerGod.process("ESP", 1, null, null);
        
        // No payment section should be added when payment method is null
        assert !receipt.contains("Payment:") : "Receipt should not have payment section when null";
        
        System.out.println("✓ test_no_payment_method passed");
    }

    // Test 14: Edge case - WELCOME coupon exceeds subtotal
    public static void test_welcome_coupon_clamping() {
        OrderManagerGod.resetLastDiscountCode();
        
        // Create a cheap item where WELCOME ($2) exceeds subtotal
        String receipt = OrderManagerGod.process("ESP", 1, "WELCOME", null);
        
        // Subtotal = 2.50, WELCOME = 2.00 (within limit, no clamping)
        assert receipt.contains("Discount (WELCOME):        -$2.00") : 
            "WELCOME discount should be $2.00 when subtotal is $2.50";
        
        System.out.println("✓ test_welcome_coupon_clamping passed");
    }

    public static void main(String[] args) {
        System.out.println("═════════════════════════════════════════════════════════");
        System.out.println("  Week 6 - Characterization Tests for OrderManagerGod");
        System.out.println("  These tests lock in existing behavior before refactoring");
        System.out.println("═════════════════════════════════════════════════════════\n");
        
        test_no_discount_cash_payment();
        test_loyalty_discount_card_payment();
        test_fixed_coupon_with_quantity();
        test_coupon_clamping();
        test_welcome_coupon();
        test_complex_product();
        test_quantity_clamping();
        test_invalid_discount_code();
        test_empty_discount_code();
        test_wallet_payment();
        test_discount_code_case_insensitive();
        test_global_state_tracking();
        test_no_payment_method();
        test_welcome_coupon_clamping();
        
        System.out.println("\n═════════════════════════════════════════════════════════");
        System.out.println("  ✓ All 14 characterization tests passed!");
        System.out.println("  Behavior is locked in. Safe to refactor.");
        System.out.println("═════════════════════════════════════════════════════════");
    }
}

