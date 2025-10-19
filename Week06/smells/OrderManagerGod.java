package smells;

import java.math.*;

// SMELL: God Class - This class knows and does EVERYTHING
// Responsibilities: product creation, pricing, discounts, tax, receipts, payment
public class OrderManagerGod {
    // SMELL: Global State - Static mutable state shared across all instances
    private static String LAST_DISCOUNT_CODE = null;
    
    // SMELL: Primitive Obsession - Using raw int instead of a TaxRate value object
    private static final int TAX_PERCENT = 10;
    
    // SMELL: Long Method - This method does everything in 100+ lines
    // Responsibilities: parse recipe, build product, apply discount, calculate tax, format receipt, handle payment
    public static String process(String recipe, int quantity, String discountCode, String paymentMethod) {
        // SMELL: Shotgun Surgery - If we need to change quantity clamping, we touch this method
        if (quantity < 1) {
            quantity = 1;
        }
        
        // SMELL: Duplicated Logic - Product creation logic duplicated from ProductFactory
        // SMELL: Feature Envy - This code wants to be in a ProductFactory
        String[] parts = recipe.split("\\+");
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim().toUpperCase();
        }
        
        // Base product creation
        String productName;
        double basePrice;
        
        // SMELL: Primitive Obsession - Using raw doubles instead of Money objects
        switch (parts[0]) {
            case "ESP":
                productName = "Espresso";
                basePrice = 2.50;
                break;
            case "LAT":
                productName = "Latte";
                basePrice = 3.20;
                break;
            case "CAP":
                productName = "Cappuccino";
                basePrice = 3.00;
                break;
            default:
                throw new IllegalArgumentException("Unknown base: " + parts[0]);
        }
        
        // Apply decorators
        // SMELL: Duplicated Logic - Same decorator logic as ProductFactory
        double currentPrice = basePrice;
        for (int i = 1; i < parts.length; i++) {
            switch (parts[i]) {
                case "SHOT":
                    productName += " + Extra Shot";
                    currentPrice += 0.80;
                    break;
                case "OAT":
                    productName += " + Oat Milk";
                    currentPrice += 0.50;
                    break;
                case "SYP":
                    productName += " + Syrup";
                    currentPrice += 0.40;
                    break;
                case "L":
                    productName += " (Large)";
                    currentPrice += 0.70;
                    break;
                default:
                    throw new IllegalArgumentException("Unknown addon: " + parts[i]);
            }
        }
        
        // Calculate subtotal
        // SMELL: Primitive Obsession - Using raw doubles everywhere
        // SMELL: Duplicated Logic - Repeated BigDecimal math
        BigDecimal unitPrice = BigDecimal.valueOf(currentPrice).setScale(2, RoundingMode.HALF_UP);
        BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity)).setScale(2, RoundingMode.HALF_UP);
        
        // Apply discount
        // SMELL: Primitive Obsession - Using string codes instead of DiscountPolicy objects
        // SMELL: Feature Envy - Discount logic wants to be in a separate DiscountPolicy class
        // SMELL: Shotgun Surgery - To add new discount type, must edit this conditional
        BigDecimal discount = BigDecimal.ZERO;
        String discountLine = "";
        
        if (discountCode != null && !discountCode.trim().isEmpty()) {
            // SMELL: Global State - Storing discount code in static field
            LAST_DISCOUNT_CODE = discountCode;
            
            if (discountCode.equalsIgnoreCase("LOYALTY10")) {
                // SMELL: Duplicated Logic - Percentage calculation repeated
                // SMELL: Feature Envy - This calculation belongs in a discount policy
                discount = subtotal.multiply(BigDecimal.valueOf(0.10)).setScale(2, RoundingMode.HALF_UP);
                discountLine = "Discount (LOYALTY10):      -$" + discount.toString() + "\n";
            } else if (discountCode.equalsIgnoreCase("SAVE5")) {
                // SMELL: Duplicated Logic - Fixed discount logic
                discount = BigDecimal.valueOf(5.00).setScale(2, RoundingMode.HALF_UP);
                // SMELL: Duplicated Logic - Capping logic (can't exceed subtotal)
                if (discount.compareTo(subtotal) > 0) {
                    discount = subtotal;
                }
                discountLine = "Discount (SAVE5):          -$" + discount.toString() + "\n";
            } else if (discountCode.equalsIgnoreCase("WELCOME")) {
                discount = BigDecimal.valueOf(2.00).setScale(2, RoundingMode.HALF_UP);
                if (discount.compareTo(subtotal) > 0) {
                    discount = subtotal;
                }
                discountLine = "Discount (WELCOME):        -$" + discount.toString() + "\n";
            }
        }
        
        // Calculate discounted total
        // SMELL: Duplicated Logic - Repeated BigDecimal operations
        BigDecimal discountedTotal = subtotal.subtract(discount).setScale(2, RoundingMode.HALF_UP);
        
        // Calculate tax
        // SMELL: Feature Envy - Tax calculation wants to be in a TaxPolicy class
        // SMELL: Primitive Obsession - Using raw int for tax percent
        // SMELL: Duplicated Logic - Tax calculation logic repeated
        BigDecimal taxRate = BigDecimal.valueOf(TAX_PERCENT).divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
        BigDecimal tax = discountedTotal.multiply(taxRate).setScale(2, RoundingMode.HALF_UP);
        
        // Calculate final total
        BigDecimal finalTotal = discountedTotal.add(tax).setScale(2, RoundingMode.HALF_UP);
        
        // Format receipt
        // SMELL: Feature Envy - Receipt formatting wants to be in a ReceiptPrinter class
        // SMELL: Long Method - Receipt building adds another 20+ lines
        StringBuilder receipt = new StringBuilder();
        receipt.append("═══════════════════════════\n");
        receipt.append("       CAFÉ POS RECEIPT\n");
        receipt.append("═══════════════════════════\n");
        receipt.append(productName).append("\n");
        receipt.append("Quantity:                  ").append(quantity).append("\n");
        receipt.append("Unit Price:                $").append(unitPrice.toString()).append("\n");
        receipt.append("---------------------------\n");
        receipt.append("Subtotal:                  $").append(subtotal.toString()).append("\n");
        
        if (!discountLine.isEmpty()) {
            receipt.append(discountLine);
        }
        
        receipt.append("Tax (").append(TAX_PERCENT).append("%):                  $").append(tax.toString()).append("\n");
        receipt.append("═══════════════════════════\n");
        receipt.append("TOTAL:                     $").append(finalTotal.toString()).append("\n");
        receipt.append("═══════════════════════════\n");
        
        // Handle payment
        // SMELL: Primitive Obsession - Using string instead of PaymentStrategy pattern
        // SMELL: Shotgun Surgery - To add new payment method, must edit this switch
        // SMELL: Feature Envy - Payment handling wants to be in PaymentStrategy classes
        if (paymentMethod != null) {
            switch (paymentMethod.toLowerCase()) {
                case "cash":
                    receipt.append("Payment: CASH\n");
                    receipt.append("Tendered: $").append(finalTotal.add(BigDecimal.valueOf(10)).toString()).append("\n");
                    receipt.append("Change: $10.00\n");
                    break;
                case "card":
                    receipt.append("Payment: CARD\n");
                    receipt.append("Card charged: $").append(finalTotal.toString()).append("\n");
                    receipt.append("Transaction approved\n");
                    break;
                case "wallet":
                    receipt.append("Payment: WALLET\n");
                    receipt.append("Digital wallet charged: $").append(finalTotal.toString()).append("\n");
                    receipt.append("Payment successful\n");
                    break;
                default:
                    receipt.append("Payment: UNKNOWN\n");
            }
        }
        
        receipt.append("═══════════════════════════\n");
        receipt.append("Thank you for your order!\n");
        
        return receipt.toString();
    }
    
    // SMELL: Global State - Exposing static mutable state
    public static String getLastDiscountCode() {
        return LAST_DISCOUNT_CODE;
    }
    
    // SMELL: Global State - Allowing external mutation of static state
    public static void resetLastDiscountCode() {
        LAST_DISCOUNT_CODE = null;
    }
}

