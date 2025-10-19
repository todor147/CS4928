package tests;

import smells.OrderManagerGod;

/**
 * Characterization Tests for OrderManagerGod.
 * These tests lock down the current behavior before refactoring.
 * All tests must remain green after refactoring to ensure behavior preservation.
 */
public class Week6CharacterizationTests {
    
    private static int testsRun = 0;
    private static int testsPassed = 0;
    
    public static void main(String[] args) {
        System.out.println("=== Week 6 Characterization Tests ===\n");
        
        // Test 1: Basic order with no discount, cash payment
        testBasicOrderNoDicountCash();
        
        // Test 2: Basic order with no discount, card payment
        testBasicOrderNoDiscountCard();
        
        // Test 3: Order with LOYALTY10 discount and cash
        testLoyalty10DiscountCash();
        
        // Test 4: Order with LOYALTY10 discount and card
        testLoyalty10DiscountCard();
        
        // Test 5: Order with SUMMER20 discount
        testSummer20Discount();
        
        // Test 6: Order with COUPON5 fixed discount
        testCoupon5FixedDiscount();
        
        // Test 7: Coupon discount capped at subtotal
        testCouponDiscountCapped();
        
        // Test 8: Multiple quantities
        testMultipleQuantities();
        
        // Test 9: Quantity clamping (negative becomes 1)
        testQuantityClamping();
        
        // Test 10: Complex product with decorators
        testComplexProduct();
        
        // Test 11: Wallet payment
        testWalletPayment();
        
        // Test 12: Invalid discount code (treated as no discount)
        testInvalidDiscountCode();
        
        System.out.println("\n=== Test Summary ===");
        System.out.println("Tests run: " + testsRun);
        System.out.println("Tests passed: " + testsPassed);
        System.out.println("Tests failed: " + (testsRun - testsPassed));
        
        if (testsRun == testsPassed) {
            System.out.println("\n✓ All tests passed!");
            System.exit(0);
        } else {
            System.out.println("\n✗ Some tests failed!");
            System.exit(1);
        }
    }
    
    private static void testBasicOrderNoDicountCash() {
        testsRun++;
        String result = OrderManagerGod.process("ESP", 1, null, "CASH");
        
        assertContains(result, "Item: Espresso", "Basic order - item name");
        assertContains(result, "Quantity: 1", "Basic order - quantity");
        assertContains(result, "Unit Price: $2.50", "Basic order - unit price");
        assertContains(result, "Subtotal: $2.50", "Basic order - subtotal");
        assertContains(result, "Discount: None -$0.00", "Basic order - no discount");
        assertContains(result, "Tax (7.0%): $0.18", "Basic order - tax");
        assertContains(result, "Total: $2.68", "Basic order - total");
        assertContains(result, "Payment: Cash", "Basic order - payment method");
        
        if (allAssertsPass()) {
            testsPassed++;
            System.out.println("✓ Test 1: Basic order no discount cash - PASSED");
        } else {
            System.out.println("✗ Test 1: Basic order no discount cash - FAILED");
        }
    }
    
    private static void testBasicOrderNoDiscountCard() {
        testsRun++;
        String result = OrderManagerGod.process("LAT", 1, "", "CARD");
        
        assertContains(result, "Item: Latte", "Basic order card - item name");
        assertContains(result, "Subtotal: $3.20", "Basic order card - subtotal");
        assertContains(result, "Discount: None -$0.00", "Basic order card - no discount");
        assertContains(result, "Tax (7.0%): $0.22", "Basic order card - tax");
        assertContains(result, "Total: $3.42", "Basic order card - total");
        assertContains(result, "Payment: Card", "Basic order card - payment method");
        
        if (allAssertsPass()) {
            testsPassed++;
            System.out.println("✓ Test 2: Basic order no discount card - PASSED");
        } else {
            System.out.println("✗ Test 2: Basic order no discount card - FAILED");
        }
    }
    
    private static void testLoyalty10DiscountCash() {
        testsRun++;
        String result = OrderManagerGod.process("CAP", 1, "LOYALTY10", "CASH");
        
        assertContains(result, "Item: Cappuccino", "Loyalty10 - item name");
        assertContains(result, "Subtotal: $3.00", "Loyalty10 - subtotal");
        assertContains(result, "Discount: Loyalty 10% -$0.30", "Loyalty10 - discount");
        assertContains(result, "Tax (7.0%): $0.19", "Loyalty10 - tax");
        assertContains(result, "Total: $2.89", "Loyalty10 - total");
        
        if (allAssertsPass()) {
            testsPassed++;
            System.out.println("✓ Test 3: Loyalty 10% discount cash - PASSED");
        } else {
            System.out.println("✗ Test 3: Loyalty 10% discount cash - FAILED");
        }
    }
    
    private static void testLoyalty10DiscountCard() {
        testsRun++;
        String result = OrderManagerGod.process("ESP", 2, "LOYALTY10", "CARD");
        
        assertContains(result, "Quantity: 2", "Loyalty10 card - quantity");
        assertContains(result, "Subtotal: $5.00", "Loyalty10 card - subtotal");
        assertContains(result, "Discount: Loyalty 10% -$0.50", "Loyalty10 card - discount");
        assertContains(result, "Tax (7.0%): $0.32", "Loyalty10 card - tax");
        assertContains(result, "Total: $4.82", "Loyalty10 card - total");
        assertContains(result, "Payment: Card", "Loyalty10 card - payment");
        
        if (allAssertsPass()) {
            testsPassed++;
            System.out.println("✓ Test 4: Loyalty 10% discount card - PASSED");
        } else {
            System.out.println("✗ Test 4: Loyalty 10% discount card - FAILED");
        }
    }
    
    private static void testSummer20Discount() {
        testsRun++;
        String result = OrderManagerGod.process("LAT", 1, "SUMMER20", "CASH");
        
        assertContains(result, "Subtotal: $3.20", "Summer20 - subtotal");
        assertContains(result, "Discount: Summer 20% -$0.64", "Summer20 - discount");
        assertContains(result, "Tax (7.0%): $0.18", "Summer20 - tax");
        assertContains(result, "Total: $2.74", "Summer20 - total");
        
        if (allAssertsPass()) {
            testsPassed++;
            System.out.println("✓ Test 5: Summer 20% discount - PASSED");
        } else {
            System.out.println("✗ Test 5: Summer 20% discount - FAILED");
        }
    }
    
    private static void testCoupon5FixedDiscount() {
        testsRun++;
        String result = OrderManagerGod.process("LAT", 2, "COUPON5", "CARD");
        
        assertContains(result, "Subtotal: $6.40", "Coupon5 - subtotal");
        assertContains(result, "Discount: Coupon $5.00 -$5.00", "Coupon5 - discount");
        assertContains(result, "Tax (7.0%): $0.10", "Coupon5 - tax");
        assertContains(result, "Total: $1.50", "Coupon5 - total");
        
        if (allAssertsPass()) {
            testsPassed++;
            System.out.println("✓ Test 6: Coupon $5 fixed discount - PASSED");
        } else {
            System.out.println("✗ Test 6: Coupon $5 fixed discount - FAILED");
        }
    }
    
    private static void testCouponDiscountCapped() {
        testsRun++;
        String result = OrderManagerGod.process("ESP", 1, "COUPON5", "CASH");
        
        assertContains(result, "Subtotal: $2.50", "Coupon capped - subtotal");
        assertContains(result, "Discount: Coupon $5.00 -$2.50", "Coupon capped - discount capped at subtotal");
        assertContains(result, "Tax (7.0%): $0.00", "Coupon capped - tax");
        assertContains(result, "Total: $0.00", "Coupon capped - total");
        
        if (allAssertsPass()) {
            testsPassed++;
            System.out.println("✓ Test 7: Coupon discount capped at subtotal - PASSED");
        } else {
            System.out.println("✗ Test 7: Coupon discount capped at subtotal - FAILED");
        }
    }
    
    private static void testMultipleQuantities() {
        testsRun++;
        String result = OrderManagerGod.process("CAP", 3, null, "CASH");
        
        assertContains(result, "Quantity: 3", "Multiple qty - quantity");
        assertContains(result, "Unit Price: $3.00", "Multiple qty - unit price");
        assertContains(result, "Subtotal: $9.00", "Multiple qty - subtotal");
        assertContains(result, "Tax (7.0%): $0.63", "Multiple qty - tax");
        assertContains(result, "Total: $9.63", "Multiple qty - total");
        
        if (allAssertsPass()) {
            testsPassed++;
            System.out.println("✓ Test 8: Multiple quantities - PASSED");
        } else {
            System.out.println("✗ Test 8: Multiple quantities - FAILED");
        }
    }
    
    private static void testQuantityClamping() {
        testsRun++;
        String result = OrderManagerGod.process("ESP", -5, null, "CASH");
        
        assertContains(result, "Quantity: 1", "Quantity clamping - clamped to 1");
        assertContains(result, "Subtotal: $2.50", "Quantity clamping - subtotal");
        
        if (allAssertsPass()) {
            testsPassed++;
            System.out.println("✓ Test 9: Quantity clamping - PASSED");
        } else {
            System.out.println("✗ Test 9: Quantity clamping - FAILED");
        }
    }
    
    private static void testComplexProduct() {
        testsRun++;
        String result = OrderManagerGod.process("LAT+SHOT+OAT", 1, "LOYALTY10", "CARD");
        
        assertContains(result, "Item: Latte + Extra Shot + Oat Milk", "Complex product - name");
        assertContains(result, "Unit Price: $4.60", "Complex product - unit price");
        assertContains(result, "Subtotal: $4.60", "Complex product - subtotal");
        assertContains(result, "Discount: Loyalty 10% -$0.46", "Complex product - discount");
        assertContains(result, "Tax (7.0%): $0.29", "Complex product - tax");
        assertContains(result, "Total: $4.43", "Complex product - total");
        
        if (allAssertsPass()) {
            testsPassed++;
            System.out.println("✓ Test 10: Complex product with decorators - PASSED");
        } else {
            System.out.println("✗ Test 10: Complex product with decorators - FAILED");
        }
    }
    
    private static void testWalletPayment() {
        testsRun++;
        String result = OrderManagerGod.process("ESP", 1, null, "WALLET");
        
        assertContains(result, "Payment: Digital Wallet", "Wallet payment - method");
        
        if (allAssertsPass()) {
            testsPassed++;
            System.out.println("✓ Test 11: Wallet payment - PASSED");
        } else {
            System.out.println("✗ Test 11: Wallet payment - FAILED");
        }
    }
    
    private static void testInvalidDiscountCode() {
        testsRun++;
        String result = OrderManagerGod.process("CAP", 1, "INVALID", "CASH");
        
        assertContains(result, "Subtotal: $3.00", "Invalid discount - subtotal");
        assertContains(result, "Discount: None -$0.00", "Invalid discount - treated as no discount");
        assertContains(result, "Total: $3.21", "Invalid discount - total");
        
        if (allAssertsPass()) {
            testsPassed++;
            System.out.println("✓ Test 12: Invalid discount code - PASSED");
        } else {
            System.out.println("✗ Test 12: Invalid discount code - FAILED");
        }
    }
    
    // Helper methods for assertions
    private static boolean lastAssertPassed = true;
    
    private static void assertContains(String actual, String expected, String message) {
        if (!actual.contains(expected)) {
            System.out.println("  ASSERTION FAILED: " + message);
            System.out.println("    Expected to contain: " + expected);
            System.out.println("    Actual output:\n" + actual);
            lastAssertPassed = false;
        }
    }
    
    private static boolean allAssertsPass() {
        boolean result = lastAssertPassed;
        lastAssertPassed = true; // Reset for next test
        return result;
    }
}

