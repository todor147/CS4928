package com.cafepos;
import com.cafepos.smells.OrderManagerGod;

public class Week6CharacterizationTests {
    
    public static void main(String[] args) {
        System.out.println("=== Week 6 Characterization Tests ===");
        
        testNoDiscountCashPayment();
        testLoyaltyDiscountCardPayment();
        testCouponFixedAmountAndQtyClamp();
        
        System.out.println("All tests completed!");
    }
    
    static void testNoDiscountCashPayment() {
        System.out.println("Testing: No discount, cash payment");
        String receipt = OrderManagerGod.process("ESP+SHOT+OAT", 1, "CASH", "NONE", false);
        System.out.println("Receipt: " + receipt);
        boolean passed = receipt.startsWith("Order (ESP+SHOT+OAT) x1") &&
                         receipt.contains("Subtotal: 3.90") &&
                         receipt.contains("Tax (10%): 0.39") &&
                         receipt.contains("Total: 4.29");
        System.out.println("Result: " + (passed ? "PASS" : "FAIL"));
        System.out.println();
    }
    
    static void testLoyaltyDiscountCardPayment() {
        System.out.println("Testing: Loyalty discount, card payment");
        String receipt = OrderManagerGod.process("LAT+L", 2, "CARD", "LOYAL5", false);
        System.out.println("Receipt: " + receipt);
        // Latte (Large) base = 3.20 + 1.00 = 4.20, qty 2 => 8.40
        // 5% discount => 0.42, discounted=7.98; tax 10% => 0.80; total=8.78
        boolean passed = receipt.contains("Subtotal: 8.40") &&
                         receipt.contains("Discount: -0.42") &&
                         receipt.contains("Tax (10%): 0.80") &&
                         receipt.contains("Total: 8.78");
        System.out.println("Result: " + (passed ? "PASS" : "FAIL"));
        System.out.println();
    }
    
    static void testCouponFixedAmountAndQtyClamp() {
        System.out.println("Testing: Coupon fixed amount and quantity clamp");
        String receipt = OrderManagerGod.process("ESP+SHOT", 0, "WALLET", "COUPON1", false);
        System.out.println("Receipt: " + receipt);
        // qty=0 clamped to 1; Espresso+SHOT = 2.50 + 0.80 = 3.30; coupon1 => -1 => 2.30; tax=0.23; total=2.53
        boolean passed = receipt.contains("Order (ESP+SHOT) x1") &&
                         receipt.contains("Subtotal: 3.30") &&
                         receipt.contains("Discount: -1.00") &&
                         receipt.contains("Tax (10%): 0.23") &&
                         receipt.contains("Total: 2.53");
        System.out.println("Result: " + (passed ? "PASS" : "FAIL"));
        System.out.println();
    }
}
