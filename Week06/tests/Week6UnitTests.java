package tests;

import factory.ProductFactory;

/**
 * Unit Tests for refactored components.
 * These tests verify that individual components work correctly.
 */
public class Week6UnitTests {
    
    private static int testsRun = 0;
    private static int testsPassed = 0;
    
    public static void main(String[] args) {
        System.out.println("=== Week 6 Unit Tests ===\n");
        
        // Discount Policy Tests
        testNoDiscount();
        testLoyaltyPercentDiscount();
        testFixedCouponDiscount();
        testFixedCouponDiscountCapped();
        
        // Tax Policy Tests
        testFixedRateTaxPolicy();
        
        // Pricing Service Tests
        testPricingServiceWithNoDiscount();
        testPricingServiceWithLoyaltyDiscount();
        testPricingServiceWithCouponDiscount();
        
        // CheckoutService Integration Tests
        testCheckoutServiceBasic();
        testCheckoutServiceWithDiscountAndPayment();
        
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
    
    private static void testNoDiscount() {
        testsRun++;
        NoDiscount discount = new NoDiscount();
        Money subtotal = Money.of(10.00);
        Money result = discount.discountOf(subtotal);
        
        if (result.equals(Money.zero()) && discount.description().equals("None")) {
            testsPassed++;
            System.out.println("✓ Test: NoDiscount - PASSED");
        } else {
            System.out.println("✗ Test: NoDiscount - FAILED");
        }
    }
    
    private static void testLoyaltyPercentDiscount() {
        testsRun++;
        LoyaltyPercentDiscount discount = new LoyaltyPercentDiscount(10.0, "Loyalty");
        Money subtotal = Money.of(100.00);
        Money result = discount.discountOf(subtotal);
        
        if (result.equals(Money.of(10.00)) && discount.description().equals("Loyalty 10%")) {
            testsPassed++;
            System.out.println("✓ Test: LoyaltyPercentDiscount - PASSED");
        } else {
            System.out.println("✗ Test: LoyaltyPercentDiscount - FAILED");
            System.out.println("  Expected: $10.00, got: " + result);
        }
    }
    
    private static void testFixedCouponDiscount() {
        testsRun++;
        FixedCouponDiscount discount = new FixedCouponDiscount(Money.of(5.00));
        Money subtotal = Money.of(20.00);
        Money result = discount.discountOf(subtotal);
        
        if (result.equals(Money.of(5.00))) {
            testsPassed++;
            System.out.println("✓ Test: FixedCouponDiscount - PASSED");
        } else {
            System.out.println("✗ Test: FixedCouponDiscount - FAILED");
        }
    }
    
    private static void testFixedCouponDiscountCapped() {
        testsRun++;
        FixedCouponDiscount discount = new FixedCouponDiscount(Money.of(10.00));
        Money subtotal = Money.of(5.00);
        Money result = discount.discountOf(subtotal);
        
        if (result.equals(Money.of(5.00))) {
            testsPassed++;
            System.out.println("✓ Test: FixedCouponDiscount capped - PASSED");
        } else {
            System.out.println("✗ Test: FixedCouponDiscount capped - FAILED");
        }
    }
    
    private static void testFixedRateTaxPolicy() {
        testsRun++;
        FixedRateTaxPolicy tax = new FixedRateTaxPolicy(7.0);
        Money amount = Money.of(100.00);
        Money result = tax.taxOn(amount);
        
        if (result.equals(Money.of(7.00)) && tax.getPercentage() == 7.0) {
            testsPassed++;
            System.out.println("✓ Test: FixedRateTaxPolicy - PASSED");
        } else {
            System.out.println("✗ Test: FixedRateTaxPolicy - FAILED");
            System.out.println("  Expected: $7.00, got: " + result);
        }
    }
    
    private static void testPricingServiceWithNoDiscount() {
        testsRun++;
        DiscountPolicy discount = new NoDiscount();
        TaxPolicy tax = new FixedRateTaxPolicy(7.0);
        PricingService service = new PricingService(discount, tax);
        
        Money subtotal = Money.of(100.00);
        PricingResult result = service.calculate(subtotal);
        
        boolean passed = result.subtotal().equals(Money.of(100.00)) &&
                        result.discountAmount().equals(Money.zero()) &&
                        result.afterDiscount().equals(Money.of(100.00)) &&
                        result.taxAmount().equals(Money.of(7.00)) &&
                        result.finalTotal().equals(Money.of(107.00));
        
        if (passed) {
            testsPassed++;
            System.out.println("✓ Test: PricingService with no discount - PASSED");
        } else {
            System.out.println("✗ Test: PricingService with no discount - FAILED");
        }
    }
    
    private static void testPricingServiceWithLoyaltyDiscount() {
        testsRun++;
        DiscountPolicy discount = new LoyaltyPercentDiscount(10.0, "Loyalty");
        TaxPolicy tax = new FixedRateTaxPolicy(7.0);
        PricingService service = new PricingService(discount, tax);
        
        Money subtotal = Money.of(100.00);
        PricingResult result = service.calculate(subtotal);
        
        boolean passed = result.subtotal().equals(Money.of(100.00)) &&
                        result.discountAmount().equals(Money.of(10.00)) &&
                        result.afterDiscount().equals(Money.of(90.00)) &&
                        result.taxAmount().equals(Money.of(6.30)) &&
                        result.finalTotal().equals(Money.of(96.30));
        
        if (passed) {
            testsPassed++;
            System.out.println("✓ Test: PricingService with loyalty discount - PASSED");
        } else {
            System.out.println("✗ Test: PricingService with loyalty discount - FAILED");
        }
    }
    
    private static void testPricingServiceWithCouponDiscount() {
        testsRun++;
        DiscountPolicy discount = new FixedCouponDiscount(Money.of(5.00));
        TaxPolicy tax = new FixedRateTaxPolicy(7.0);
        PricingService service = new PricingService(discount, tax);
        
        Money subtotal = Money.of(20.00);
        PricingResult result = service.calculate(subtotal);
        
        boolean passed = result.subtotal().equals(Money.of(20.00)) &&
                        result.discountAmount().equals(Money.of(5.00)) &&
                        result.afterDiscount().equals(Money.of(15.00)) &&
                        result.taxAmount().equals(Money.of(1.05)) &&
                        result.finalTotal().equals(Money.of(16.05));
        
        if (passed) {
            testsPassed++;
            System.out.println("✓ Test: PricingService with coupon discount - PASSED");
        } else {
            System.out.println("✗ Test: PricingService with coupon discount - FAILED");
        }
    }
    
    private static void testCheckoutServiceBasic() {
        testsRun++;
        ProductFactory factory = new ProductFactory();
        DiscountPolicy discount = new NoDiscount();
        TaxPolicy tax = new FixedRateTaxPolicy(7.0);
        PricingService pricing = new PricingService(discount, tax);
        ReceiptPrinter printer = new ReceiptPrinter();
        CheckoutService service = new CheckoutService(factory, pricing, printer, 7.0);
        
        String receipt = service.checkout("ESP", 1, new ReceiptCashPayment());
        
        boolean passed = receipt.contains("Item: Espresso") &&
                        receipt.contains("Subtotal: $2.50") &&
                        receipt.contains("Payment: Cash");
        
        if (passed) {
            testsPassed++;
            System.out.println("✓ Test: CheckoutService basic - PASSED");
        } else {
            System.out.println("✗ Test: CheckoutService basic - FAILED");
        }
    }
    
    private static void testCheckoutServiceWithDiscountAndPayment() {
        testsRun++;
        ProductFactory factory = new ProductFactory();
        DiscountPolicy discount = new LoyaltyPercentDiscount(10.0, "Loyalty");
        TaxPolicy tax = new FixedRateTaxPolicy(7.0);
        PricingService pricing = new PricingService(discount, tax);
        ReceiptPrinter printer = new ReceiptPrinter();
        CheckoutService service = new CheckoutService(factory, pricing, printer, 7.0);
        
        String receipt = service.checkout("LAT", 2, new ReceiptCardPayment());
        
        boolean passed = receipt.contains("Item: Latte") &&
                        receipt.contains("Quantity: 2") &&
                        receipt.contains("Subtotal: $6.40") &&
                        receipt.contains("Discount: Loyalty 10%") &&
                        receipt.contains("Payment: Card");
        
        if (passed) {
            testsPassed++;
            System.out.println("✓ Test: CheckoutService with discount and payment - PASSED");
        } else {
            System.out.println("✗ Test: CheckoutService with discount and payment - FAILED");
        }
    }
}

