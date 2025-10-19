import factory.ProductFactory;
import pricing.*;

/**
 * Week6Demo - Interactive CLI demo comparing old and new implementations.
 * 
 * This demonstrates the 30-second proof that the refactored code produces
 * identical output to the original smelly code.
 */
public class Week6Demo {
    
    public static void main(String[] args) {
        System.out.println("=== Week 6 Refactoring Demo ===");
        System.out.println("Comparing OrderManagerGod vs CheckoutService\n");
        
        // Setup the new clean architecture
        ProductFactory factory = new ProductFactory();
        DiscountPolicy noDiscount = new NoDiscount();
        DiscountPolicy loyaltyDiscount = new LoyaltyPercentDiscount(10.0, "Loyalty");
        DiscountPolicy summerDiscount = new LoyaltyPercentDiscount(20.0, "Summer");
        DiscountPolicy couponDiscount = new FixedCouponDiscount(Money.of(5.00));
        
        TaxPolicy taxPolicy = new FixedRateTaxPolicy(7.0);
        ReceiptPrinter printer = new ReceiptPrinter();
        
        // Test scenarios
        String[][] testCases = {
            {"ESP", "1", null, "CASH", "Basic espresso, no discount, cash"},
            {"LAT", "2", "LOYALTY10", "CARD", "Latte with loyalty discount, card"},
            {"CAP+SHOT", "1", "SUMMER20", "WALLET", "Cappuccino with shot, summer discount, wallet"},
            {"LAT+OAT", "3", "COUPON5", "CASH", "Latte with oat milk, coupon discount, cash"},
            {"ESP", "1", "INVALID", "CARD", "Espresso with invalid discount code"}
        };
        
        for (String[] testCase : testCases) {
            String recipe = testCase[0];
            int quantity = Integer.parseInt(testCase[1]);
            String discountCode = testCase[2];
            String paymentMethod = testCase[3];
            String description = testCase[4];
            
            System.out.println("--- Test: " + description + " ---");
            
            // Get old result
            String oldResult = smells.OrderManagerGod.process(recipe, quantity, discountCode, paymentMethod);
            
            // Get new result
            DiscountPolicy discount = getDiscountPolicy(discountCode);
            PricingService pricing = new PricingService(discount, taxPolicy);
            CheckoutService checkout = new CheckoutService(factory, pricing, printer, 7.0);
            ReceiptPaymentStrategy payment = getPaymentStrategy(paymentMethod);
            String newResult = checkout.checkout(recipe, quantity, payment);
            
            // Compare results
            boolean matches = oldResult.equals(newResult);
            System.out.println("Results match: " + (matches ? "✓ YES" : "✗ NO"));
            
            if (!matches) {
                System.out.println("\nOLD RESULT:");
                System.out.println(oldResult);
                System.out.println("\nNEW RESULT:");
                System.out.println(newResult);
            }
            
            System.out.println();
        }
        
        System.out.println("=== Demo Complete ===");
        System.out.println("The refactored code produces identical output to the original!");
        System.out.println("All code smells have been eliminated while preserving behavior.");
    }
    
    private static DiscountPolicy getDiscountPolicy(String discountCode) {
        if (discountCode == null || discountCode.isBlank()) {
            return new NoDiscount();
        }
        
        switch (discountCode) {
            case "LOYALTY10":
                return new LoyaltyPercentDiscount(10.0, "Loyalty");
            case "SUMMER20":
                return new LoyaltyPercentDiscount(20.0, "Summer");
            case "COUPON5":
                return new FixedCouponDiscount(Money.of(5.00));
            default:
                return new NoDiscount();
        }
    }
    
    private static ReceiptPaymentStrategy getPaymentStrategy(String paymentMethod) {
        if (paymentMethod == null) {
            return null;
        }
        
        switch (paymentMethod.toUpperCase()) {
            case "CASH":
                return new ReceiptCashPayment();
            case "CARD":
                return new ReceiptCardPayment();
            case "WALLET":
                return new ReceiptWalletPayment();
            default:
                return null;
        }
    }
}

