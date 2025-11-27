package com.cafepos.demo;

import com.cafepos.smells.OrderManagerGod;
import com.cafepos.factory.ProductFactory;
import com.cafepos.pricing.*;
import com.cafepos.common.Money;

import java.util.*;

public final class Week6Demo {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Map<String, String> currentOrder = new HashMap<>();
    private static final ProductFactory productFactory = new ProductFactory();
    private static boolean orderCreated = false;
    
    public static void main(String[] args) {
        System.out.println("Week6 Refactoring Demo");
        System.out.println("POS System - Old vs New Comparison");
        System.out.println();
        
        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getIntInput("Enter your choice: ");
            System.out.println();
            
            switch (choice) {
                case 1:
                    createNewOrder();
                    break;
                case 2:
                    addProductToOrder();
                    break;
                case 3:
                    addExtrasToOrder();
                    break;
                case 4:
                    applyDiscount();
                    break;
                case 5:
                    viewOrder();
                    break;
                case 6:
                    processPayment();
                    break;
                case 7:
                    compareOldVsNew();
                    break;
                case 8:
                    running = false;
                    System.out.println("Thank you for using Week 6 POS System!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            
            if (running) {
                System.out.println();
            }
        }
        
        scanner.close();
    }
    
    private static void displayMenu() {
        System.out.println("\nMain Menu:");
        System.out.println("1. Create New Order");
        System.out.println("2. Add Product to Order");
        System.out.println("3. Add Extras to Order");
        System.out.println("4. Apply Discount");
        System.out.println("5. View Order");
        System.out.println("6. Process Payment");
        System.out.println("7. Compare Old vs New");
        System.out.println("8. Exit");
    }
    
    private static void createNewOrder() {
        currentOrder.clear();
        orderCreated = true;
        System.out.println("New order created. Ready to add products.");
    }
    
    private static void addProductToOrder() {
        if (!orderCreated) {
            System.out.println("Please create a new order first.");
            return;
        }
        
        System.out.println("Available Products:");
        System.out.println("ESP - Espresso (2.50 EUR)");
        System.out.println("LAT - Latte (3.20 EUR)");
        System.out.println("CAP - Cappuccino (3.00 EUR)");
        System.out.println("AME - Americano (2.80 EUR)");
        
        System.out.print("Enter product code: ");
        String productCode = scanner.nextLine().trim().toUpperCase();
        
        System.out.print("Enter quantity: ");
        int qty = getIntInput("");
        
        String key = productCode + "_" + qty;
        currentOrder.put(key, "product");
        
        System.out.println("Added " + qty + "x " + productCode + " to order.");
        
        System.out.print("Would you like to add extras to this " + productCode + "? (y/n): ");
        String addExtras = scanner.nextLine().trim().toLowerCase();
        if (addExtras.equals("y") || addExtras.equals("yes")) {
            addExtrasToProduct(productCode);
        }
    }
    
    private static void addExtrasToOrder() {
        if (!orderCreated) {
            System.out.println("Please create a new order first.");
            return;
        }
        
        System.out.println("Available Extras:");
        System.out.println("SHOT - Extra Shot (+0.80)");
        System.out.println("OAT - Oat Milk (+0.50)");
        System.out.println("L - Large Size (+0.70)");
        System.out.println("SYRUP - Syrup (+0.30)");
        
        System.out.print("Enter extra code: ");
        String extraCode = scanner.nextLine().trim().toUpperCase();
        
        String key = "EXTRA_" + extraCode;
        currentOrder.put(key, "extra");
        
        System.out.println("Added " + extraCode + " to order.");
    }
    
    private static void applyDiscount() {
        if (!orderCreated) {
            System.out.println("Please create a new order first.");
            return;
        }
        
        System.out.println("Available Discounts:");
        System.out.println("LOYAL5 - 5% Loyalty Discount");
        System.out.println("COUPON1 - 1.00 EUR Coupon");
        System.out.println("NONE - No Discount");
        
        System.out.print("Enter discount code: ");
        String discountCode = scanner.nextLine().trim().toUpperCase();
        
        currentOrder.put("DISCOUNT", discountCode);
        System.out.println("Applied discount: " + discountCode);
    }
    
    private static void viewOrder() {
        if (!orderCreated) {
            System.out.println("No order created yet.");
            return;
        }
        
        System.out.println("Current Order:");
        
        if (currentOrder.isEmpty()) {
            System.out.println("(Empty order)");
            return;
        }
        
        // Build recipe and calculate pricing
        StringBuilder recipe = new StringBuilder();
        int qty = 1;
        String paymentType = currentOrder.getOrDefault("PAYMENT", "CASH");
        String discountCode = currentOrder.getOrDefault("DISCOUNT", "NONE");
        
        // First pass: collect products and extras
        List<String> products = new ArrayList<>();
        List<String> extras = new ArrayList<>();
        
        for (Map.Entry<String, String> entry : currentOrder.entrySet()) {
            if (entry.getValue().equals("product")) {
                String[] parts = entry.getKey().split("_");
                if (parts.length == 2) {
                    products.add(parts[0]);
                    qty = Integer.parseInt(parts[1]);
                    // Show product with price
                    String productCode = parts[0];
                    String productPrice = getProductPrice(productCode);
                    System.out.println("- " + productCode + " x" + parts[1] + " (" + productPrice + " EUR)");
                }
            } else if (entry.getValue().equals("extra")) {
                String extraCode = entry.getKey().replace("EXTRA_", "");
                extras.add(extraCode);
                String extraPrice = getExtraPrice(extraCode);
                System.out.println("- Extra: " + extraCode + " (" + extraPrice + " EUR)");
            } else if (entry.getKey().equals("DISCOUNT")) {
                System.out.println("- Discount: " + entry.getValue());
            } else if (entry.getKey().equals("PAYMENT")) {
                System.out.println("- Payment: " + entry.getValue());
            }
        }
        
        // Build recipe: combine all products and extras
        if (!products.isEmpty()) {
            // For multiple products, we need to create a combined recipe
            // This is a limitation - the system works best with one product + extras
            if (products.size() == 1) {
                recipe.append(products.getFirst());
                for (String extra : extras) {
                    recipe.append("+").append(extra);
                }
            } else {
                // Multiple products - use the first one for now
                // In a real system, you'd need to handle this differently
                recipe.append(products.getFirst());
                for (String extra : extras) {
                    recipe.append("+").append(extra);
                }
                System.out.println("Note: Using first product only. Multiple products not fully supported.");
            }
        }
        
        if (recipe.length() == 0) {
            System.out.println("(No products added)");
            return;
        }
        
        // Calculate and show pricing
        try {
            var product = productFactory.create(recipe.toString());
            var unitPrice = product.basePrice();
            try {
                var priced = product instanceof com.cafepos.decorator.Priced p ? p.price() : product.basePrice();
                unitPrice = priced;
            } catch (Exception e) {
                unitPrice = product.basePrice();
            }
            if (qty <= 0) qty = 1;
            var subtotal = unitPrice.multiply(qty);
            
            var discountPolicy = createDiscountPolicy(discountCode);
            var taxPolicy = new FixedRateTaxPolicy(10);
            var pricingService = new PricingService(discountPolicy, taxPolicy);
            var pricingResult = pricingService.price(subtotal);
            
            System.out.println("\nPricing:");
            System.out.println("Subtotal: " + pricingResult.subtotal());
            if (pricingResult.discount().asBigDecimal().signum() > 0) {
                System.out.println("Discount: -" + pricingResult.discount());
            }
            System.out.println("Tax (10%): " + pricingResult.tax());
            System.out.println("Total: " + pricingResult.total());
        } catch (Exception e) {
            System.out.println("(Unable to calculate pricing)");
        }
    }
    
    private static void processPayment() {
        if (!orderCreated) {
            System.out.println("Please create a new order first.");
            return;
        }
        
        String recipe = buildRecipe();
        if (recipe.isEmpty()) {
            System.out.println("No products in order to pay for.");
            return;
        }
        
        try {
            var product = productFactory.create(recipe);
            var unitPrice = product.basePrice();
            try {
                var priced = product instanceof com.cafepos.decorator.Priced p ? p.price() : product.basePrice();
                unitPrice = priced;
            } catch (Exception e) {
                unitPrice = product.basePrice();
            }
            int qty = 1;
            var subtotal = unitPrice.multiply(qty);
            
            String discountCode = currentOrder.getOrDefault("DISCOUNT", "NONE");
            var discountPolicy = createDiscountPolicy(discountCode);
            var taxPolicy = new FixedRateTaxPolicy(10);
            var pricingService = new PricingService(discountPolicy, taxPolicy);
            var pricingResult = pricingService.price(subtotal);
            
            System.out.println("Order Total: " + pricingResult.total() + " EUR");
            System.out.println();
            
            System.out.println("Payment Methods:");
            System.out.println("1. CASH - Cash Payment");
            System.out.println("2. CARD - Card Payment");
            System.out.println("3. WALLET - Wallet Payment");
            
            int choice = getIntInput("Select payment method (1-3): ");
            
            switch (choice) {
                case 1:
                    processCashPayment(pricingResult.total());
                    break;
                case 2:
                    processCardPayment(pricingResult.total());
                    break;
                case 3:
                    processWalletPayment(pricingResult.total());
                    break;
                default:
                    System.out.println("Invalid payment method.");
                    return;
            }
            
            currentOrder.put("PAYMENT", getPaymentType(choice));
            System.out.println("Payment processed successfully!");
            
        } catch (Exception e) {
            System.out.println("Error processing payment: " + e.getMessage());
        }
    }
    
    private static String buildRecipe() {
        StringBuilder recipe = new StringBuilder();
        List<String> products = new ArrayList<>();
        List<String> extras = new ArrayList<>();
        
        for (Map.Entry<String, String> entry : currentOrder.entrySet()) {
            if (entry.getValue().equals("product")) {
                String[] parts = entry.getKey().split("_");
                if (parts.length == 2) {
                    products.add(parts[0]);
                }
            } else if (entry.getValue().equals("extra")) {
                String extraCode = entry.getKey().replace("EXTRA_", "");
                extras.add(extraCode);
            }
        }
        
        if (!products.isEmpty()) {
            recipe.append(products.getFirst());
            for (String extra : extras) {
                recipe.append("+").append(extra);
            }
        }
        
        return recipe.toString();
    }
    
    private static void processCashPayment(Money total) {
        System.out.println("Cash Payment");
        System.out.println("Amount due: " + total + " EUR");
        System.out.print("Enter amount received: ");
        double received = Double.parseDouble(scanner.nextLine().trim());
        Money receivedMoney = Money.of(received);
        
        if (receivedMoney.asBigDecimal().compareTo(total.asBigDecimal()) >= 0) {
            Money change = Money.of(receivedMoney.asBigDecimal().subtract(total.asBigDecimal()));
            System.out.println("Change: " + change + " EUR");
            System.out.println("[Cash] Customer paid " + total + " EUR");
        } else {
            System.out.println("Insufficient payment. Need " + total + " EUR");
        }
    }
    
    private static void processCardPayment(Money total) {
        System.out.println("Card Payment");
        System.out.print("Enter card number: ");
        String cardNumber = scanner.nextLine().trim();
        System.out.print("Enter expiry date (MM/YY): ");
        String expiry = scanner.nextLine().trim();
        System.out.print("Enter CVV: ");
        String cvv = scanner.nextLine().trim();
        
        // Simulate card processing
        System.out.println("Processing card payment...");
        System.out.println("Card: ****" + cardNumber.substring(Math.max(0, cardNumber.length() - 4)));
        System.out.println("[Card] Customer paid " + total + " EUR with card ****" + cardNumber.substring(Math.max(0, cardNumber.length() - 4)));
    }
    
    private static void processWalletPayment(Money total) {
        System.out.println("Wallet Payment");
        System.out.print("Enter wallet ID: ");
        String walletId = scanner.nextLine().trim();
        
        System.out.println("Processing wallet payment...");
        System.out.println("Wallet: " + walletId);
        System.out.println("[Wallet] Customer paid " + total + " EUR via wallet " + walletId);
    }
    
    private static String getPaymentType(int choice) {
        return switch (choice) {
            case 1 -> "CASH";
            case 2 -> "CARD";
            case 3 -> "WALLET";
            default -> "UNKNOWN";
        };
    }
    
    private static void compareOldVsNew() {
        if (!orderCreated) {
            System.out.println("Please create a new order first.");
            return;
        }
        
        // Build recipe string from current order
        StringBuilder recipe = new StringBuilder();
        int qty = 1;
        String paymentType = currentOrder.getOrDefault("PAYMENT", "CASH");
        String discountCode = currentOrder.getOrDefault("DISCOUNT", "NONE");
        
        // First pass: collect products and extras
        List<String> products = new ArrayList<>();
        List<String> extras = new ArrayList<>();
        
        for (Map.Entry<String, String> entry : currentOrder.entrySet()) {
            if (entry.getValue().equals("product")) {
                String[] parts = entry.getKey().split("_");
                if (parts.length == 2) {
                    products.add(parts[0]);
                    qty = Integer.parseInt(parts[1]);
                }
            } else if (entry.getValue().equals("extra")) {
                String extraCode = entry.getKey().replace("EXTRA_", "");
                extras.add(extraCode);
            }
        }
        
        // Build recipe: combine all products and extras
        if (!products.isEmpty()) {
            // For multiple products, we need to create a combined recipe
            // This is a limitation - the system works best with one product + extras
            if (products.size() == 1) {
                recipe.append(products.getFirst());
                for (String extra : extras) {
                    recipe.append("+").append(extra);
                }
            } else {
                // Multiple products - use the first one for now
                // In a real system, you'd need to handle this differently
                recipe.append(products.getFirst());
                for (String extra : extras) {
                    recipe.append("+").append(extra);
                }
                System.out.println("Note: Using first product only. Multiple products not fully supported.");
            }
        }
        
        if (recipe.length() == 0) {
            System.out.println("No products in order. Using default: ESP");
            recipe.append("ESP");
        }
        
        System.out.println("Processing order with recipe: " + recipe.toString());
        System.out.println("Quantity: " + qty);
        System.out.println("Payment: " + paymentType);
        System.out.println("Discount: " + discountCode);
        System.out.println();
        
        // Test with old smelly code
        String oldReceipt = OrderManagerGod.process(recipe.toString(), qty, paymentType, discountCode, false);
        
        // Test with new refactored code
        var product = productFactory.create(recipe.toString());
        var unitPrice = product.basePrice();
        try {
            var priced = product instanceof com.cafepos.decorator.Priced p ? p.price() : product.basePrice();
            unitPrice = priced;
        } catch (Exception e) {
            unitPrice = product.basePrice();
        }
        if (qty <= 0) qty = 1;
        var subtotal = unitPrice.multiply(qty);
        
        var discountPolicy = createDiscountPolicy(discountCode);
        var taxPolicy = new FixedRateTaxPolicy(10);
        var pricingService = new PricingService(discountPolicy, taxPolicy);
        var pricingResult = pricingService.price(subtotal);
        
        var receiptPrinter = new ReceiptPrinter();
        String newReceipt = receiptPrinter.format(recipe.toString(), qty, pricingResult, 10);
        
        System.out.println("Payment: " + getPaymentMessage(paymentType, pricingResult.total()));
        
        System.out.println("\n--- Old Receipt (Smelly Code) ---\n" + oldReceipt);
        System.out.println("\n--- New Receipt (Refactored Code) ---\n" + newReceipt);
        
        boolean match = oldReceipt.equals(newReceipt);
        System.out.println("\nResult: " + (match ? "MATCH" : "NO MATCH"));
    }
    
    private static DiscountPolicy createDiscountPolicy(String discountCode) {
        switch (discountCode.toUpperCase()) {
            case "LOYAL5":
                return new LoyaltyPercentDiscount(5);
            case "COUPON1":
                return new FixedCouponDiscount(Money.of(1.00));
            case "NONE":
            default:
                return new NoDiscount();
        }
    }
    
    private static String getPaymentMessage(String paymentType, Money total) {
        switch (paymentType.toUpperCase()) {
            case "CASH":
                return "[Cash] Customer paid " + total + " EUR";
            case "CARD":
                return "[Card] Customer paid " + total + " EUR with card ****1234";
            case "WALLET":
                return "[Wallet] Customer paid " + total + " EUR via wallet user-wallet-789";
            default:
                return "[UnknownPayment] " + total;
        }
    }
    
    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
    
    private static String getProductPrice(String productCode) {
        return switch (productCode.toUpperCase()) {
            case "ESP" -> "2.50";
            case "LAT" -> "3.20";
            case "CAP" -> "3.00";
            case "AME" -> "2.80";
            default -> "0.00";
        };
    }
    
    private static String getExtraPrice(String extraCode) {
        return switch (extraCode.toUpperCase()) {
            case "SHOT" -> "+0.80";
            case "OAT" -> "+0.50";
            case "L" -> "+0.70";
            case "SYRUP" -> "+0.30";
            default -> "+0.00";
        };
    }
    
    private static void addExtrasToProduct(String productCode) {
        System.out.println("Adding extras to " + productCode + ":");
        System.out.println("Available Extras:");
        System.out.println("SHOT - Extra Shot (+0.80)");
        System.out.println("OAT - Oat Milk (+0.50)");
        System.out.println("L - Large Size (+0.70)");
        System.out.println("SYRUP - Syrup (+0.30)");
        System.out.println("DONE - Finish adding extras");
        
        while (true) {
            System.out.print("Enter extra code (or DONE to finish): ");
            String extraCode = scanner.nextLine().trim().toUpperCase();
            
            if (extraCode.equals("DONE")) {
                break;
            }
            
            if (extraCode.equals("SHOT") || extraCode.equals("OAT") || 
                extraCode.equals("L") || extraCode.equals("SYRUP")) {
                String key = "EXTRA_" + extraCode;
                currentOrder.put(key, "extra");
                System.out.println("Added " + extraCode + " to " + productCode);
            } else {
                System.out.println("Invalid extra code. Try SHOT, OAT, L, SYRUP, or DONE.");
            }
        }
    }
}