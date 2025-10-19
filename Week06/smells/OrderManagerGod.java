package smells;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * OrderManagerGod - A monolithic class that does everything.
 * This class violates multiple design principles and contains various code smells.
 */
public class OrderManagerGod {
    // SMELL: Global State - static mutable state shared across all instances
    private static String LAST_DISCOUNT_CODE = "NONE";
    // SMELL: Global State - static configuration that should be injected
    private static final double TAX_PERCENT = 7.0;
    
    // SMELL: God Class - this class knows too much and does too much
    // SMELL: Long Method - this method is doing way too many things
    public static String process(String recipe, int quantity, String discountCode, String paymentMethod) {
        // Clamp quantity
        if (quantity < 1) {
            quantity = 1;
        }
        
        // Create product - inline factory logic
        // SMELL: Duplicated Logic - product creation logic copied here
        BigDecimal unitPrice;
        String productName;
        
        String[] raw = recipe.split("\\+");
        String[] parts = java.util.Arrays.stream(raw)
                .map(String::trim)
                .map(String::toUpperCase)
                .toArray(String[]::new);
        
        // Base price calculation
        switch (parts[0]) {
            case "ESP":
                productName = "Espresso";
                unitPrice = BigDecimal.valueOf(2.50);
                break;
            case "LAT":
                productName = "Latte";
                unitPrice = BigDecimal.valueOf(3.20);
                break;
            case "CAP":
                productName = "Cappuccino";
                unitPrice = BigDecimal.valueOf(3.00);
                break;
            default:
                throw new IllegalArgumentException("Unknown base: " + parts[0]);
        }
        
        // Add decorations
        for (int i = 1; i < parts.length; i++) {
            switch (parts[i]) {
                case "SHOT":
                    productName += " + Extra Shot";
                    unitPrice = unitPrice.add(BigDecimal.valueOf(0.80));
                    break;
                case "OAT":
                    productName += " + Oat Milk";
                    unitPrice = unitPrice.add(BigDecimal.valueOf(0.60));
                    break;
                case "SYP":
                    productName += " + Syrup";
                    unitPrice = unitPrice.add(BigDecimal.valueOf(0.50));
                    break;
                case "L":
                    productName += " + Large";
                    unitPrice = unitPrice.add(BigDecimal.valueOf(1.00));
                    break;
                default:
                    throw new IllegalArgumentException("Unknown addon: " + parts[i]);
            }
        }
        
        unitPrice = unitPrice.setScale(2, RoundingMode.HALF_UP);
        
        // Calculate subtotal
        // SMELL: Duplicated Logic - BigDecimal math repeated
        BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity))
                                      .setScale(2, RoundingMode.HALF_UP);
        
        // Calculate discount
        // SMELL: Primitive Obsession - using strings instead of proper types for discount codes
        // SMELL: Feature Envy - discount rules should be in their own class
        // SMELL: Shotgun Surgery - changing discount rules requires editing this method
        BigDecimal discountAmount = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        String discountDescription = "";
        
        if (discountCode != null && !discountCode.isBlank()) {
            // SMELL: Primitive Obsession - magic strings and conditionals
            if (discountCode.equals("LOYALTY10")) {
                // SMELL: Duplicated Logic - percentage calculation repeated
                discountAmount = subtotal.multiply(BigDecimal.valueOf(0.10))
                                        .setScale(2, RoundingMode.HALF_UP);
                discountDescription = "Loyalty 10%";
            } else if (discountCode.equals("SUMMER20")) {
                discountAmount = subtotal.multiply(BigDecimal.valueOf(0.20))
                                        .setScale(2, RoundingMode.HALF_UP);
                discountDescription = "Summer 20%";
            } else if (discountCode.equals("COUPON5")) {
                discountAmount = BigDecimal.valueOf(5.00)
                                          .setScale(2, RoundingMode.HALF_UP);
                // Cap discount at subtotal
                if (discountAmount.compareTo(subtotal) > 0) {
                    discountAmount = subtotal;
                }
                discountDescription = "Coupon $5.00";
            } else {
                // Invalid discount code
                discountDescription = "None";
            }
            // SMELL: Global State - mutating static state
            LAST_DISCOUNT_CODE = discountCode;
        } else {
            discountDescription = "None";
            LAST_DISCOUNT_CODE = "NONE";
        }
        
        // Calculate discounted total
        // SMELL: Duplicated Logic - repeated subtraction pattern
        BigDecimal afterDiscount = subtotal.subtract(discountAmount)
                                           .setScale(2, RoundingMode.HALF_UP);
        
        // Calculate tax
        // SMELL: Primitive Obsession - using raw double for TAX_PERCENT
        // SMELL: Feature Envy - tax calculation should be in its own class
        // SMELL: Duplicated Logic - tax calculation logic embedded here
        BigDecimal taxAmount = afterDiscount.multiply(BigDecimal.valueOf(TAX_PERCENT / 100.0))
                                           .setScale(2, RoundingMode.HALF_UP);
        
        // Calculate final total
        BigDecimal finalTotal = afterDiscount.add(taxAmount)
                                             .setScale(2, RoundingMode.HALF_UP);
        
        // Build receipt
        // SMELL: Long Method continues - formatting mixed with business logic
        StringBuilder receipt = new StringBuilder();
        receipt.append("=== RECEIPT ===\n");
        receipt.append("Item: ").append(productName).append("\n");
        receipt.append("Quantity: ").append(quantity).append("\n");
        receipt.append("Unit Price: $").append(unitPrice.toString()).append("\n");
        receipt.append("Subtotal: $").append(subtotal.toString()).append("\n");
        receipt.append("Discount: ").append(discountDescription)
               .append(" -$").append(String.format("%.2f", discountAmount)).append("\n");
        receipt.append("Tax (").append(String.format("%.1f", TAX_PERCENT))
               .append("%): $").append(taxAmount.toString()).append("\n");
        receipt.append("Total: $").append(finalTotal.toString()).append("\n");
        
        // Handle payment
        // SMELL: Primitive Obsession - using string switch for payment types
        // SMELL: Shotgun Surgery - adding new payment method requires editing this method
        if (paymentMethod != null) {
            switch (paymentMethod.toUpperCase()) {
                case "CASH":
                    receipt.append("Payment: Cash\n");
                    break;
                case "CARD":
                    receipt.append("Payment: Card\n");
                    break;
                case "WALLET":
                    receipt.append("Payment: Digital Wallet\n");
                    break;
                default:
                    receipt.append("Payment: Unknown\n");
            }
        }
        
        receipt.append("===============");
        
        return receipt.toString();
    }
    
    // SMELL: Global State - exposing mutable static state
    public static String getLastDiscountCode() {
        return LAST_DISCOUNT_CODE;
    }
}

