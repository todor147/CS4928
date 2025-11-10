package com.cafepos; 

import com.cafepos.smells.OrderManagerGod; 

public class Week6CharacterizationTests { 

    public void no_discount_cash_payment() { 
        String receipt = OrderManagerGod.process("ESP+SHOT+OAT", 1, 
"CASH", "NONE", false); 
        assertTrue(receipt.startsWith("Order (ESP+SHOT+OAT) x1")); 
        assertTrue(receipt.contains("Subtotal: 3.90")); 
        assertTrue(receipt.contains("Tax (10%): 0.39")); 
        assertTrue(receipt.contains("Total: 4.29")); 
    } 

    public void loyalty_discount_card_payment() { 
        String receipt = OrderManagerGod.process("LAT+L", 2, "CARD", 
"LOYAL5", false); 
        // Latte (Large) base = 3.20 + 1.00 = 4.20, qty 2 => 8.40 
        // 5% discount => 0.42, discounted=7.98; tax 10% => 0.80; total=8.78 
        assertTrue(receipt.contains("Subtotal: 8.40")); 
        assertTrue(receipt.contains("Discount: -0.42")); 
        assertTrue(receipt.contains("Tax (10%): 0.80")); 
        assertTrue(receipt.contains("Total: 8.78")); 
    } 

    public void coupon_fixed_amount_and_qty_clamp() { 
        String receipt = OrderManagerGod.process("ESP+SHOT", 0, "WALLET", 
"COUPON1", false); 
        // qty=0 clamped to 1; Espresso+SHOT = 2.50 + 0.80 = 3.30; coupon1 => -1 => 2.30; tax=0.23; total=2.53 
        assertTrue(receipt.contains("Order (ESP+SHOT) x1")); 
        assertTrue(receipt.contains("Subtotal: 3.30")); 
        assertTrue(receipt.contains("Discount: -1.00")); 
        assertTrue(receipt.contains("Tax (10%): 0.23")); 
        assertTrue(receipt.contains("Total: 2.53")); 
    }
    
    private void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError("Assertion failed");
        }
    }
}