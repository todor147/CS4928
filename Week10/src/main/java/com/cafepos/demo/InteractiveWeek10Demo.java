package com.cafepos.demo;

import com.cafepos.app.CheckoutService;
import com.cafepos.app.events.*;
import com.cafepos.common.Money;
import com.cafepos.domain.*;
import com.cafepos.infra.Wiring;
import com.cafepos.payment.*;
import com.cafepos.pricing.*;
import com.cafepos.ui.OrderController;

import java.util.Scanner;

public final class InteractiveWeek10Demo {
    private static final Scanner scanner = new Scanner(System.in);
    private static OrderController controller;
    private static OrderRepository repo;
    private static PricingService pricing;
    private static CheckoutService checkout;
    private static EventBus eventBus;
    private static long currentOrderId;
    private static int taxPercent = 10;
    private static int discountPercent = 5; // Current discount percentage

    public static void main(String[] args) {
        System.out.println("Week 10 - Interactive Demo");
        System.out.println("POS System - MVC Pattern & Layered Architecture");
        System.out.println();

        initializeSystem();

        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getIntInput("Enter your choice: ");
            System.out.println();

            switch (choice) {
                case 1 -> viewOrder();
                case 2 -> viewAvailableProducts();
                case 3 -> addItemManually();
                case 4 -> removeLastItem();
                case 5 -> viewPricingBreakdown();
                case 6 -> checkout();
                case 7 -> eventBusDemo();
                case 8 -> applyDiscount();
                case 9 -> changeTaxRate();
                case 10 -> newOrder();
                case 11 -> running = false;
                default -> System.out.println("Invalid choice. Please try again.");
            }

            if (running) {
                System.out.println();
            }
        }

        System.out.println("Thank you for using Week 10 POS System!");
        scanner.close();
    }

    private static void initializeSystem() {
        var c = Wiring.createDefault();
        repo = c.repo();
        updatePricingAndCheckout();
        controller = new OrderController(repo, checkout);
        eventBus = new EventBus();
        currentOrderId = System.currentTimeMillis() % 10000;
        controller.createOrder(currentOrderId);

        // Register event handlers
        eventBus.on(OrderCreated.class, e -> 
            System.out.println("[EventBus] Order created: " + e.orderId()));
        eventBus.on(OrderPaid.class, e -> 
            System.out.println("[EventBus] Order paid: " + e.orderId() + " - receipt sent"));

        System.out.println("System initialized!");
        System.out.println("Order ID: " + currentOrderId);
        System.out.println("Tax Rate: " + taxPercent + "%");
        System.out.println("Discount: " + discountPercent + "%");
        System.out.println();
    }

    private static void updatePricingAndCheckout() {
        DiscountPolicy discountPolicy = discountPercent == 0 
            ? new NoDiscount() 
            : new LoyaltyPercentDiscount(discountPercent);
        pricing = new PricingService(discountPolicy, new FixedRateTaxPolicy(taxPercent));
        checkout = new CheckoutService(repo, pricing);
        // Update controller with new checkout service
        controller = new OrderController(repo, checkout);
    }

    private static void displayMenu() {
        System.out.println("\nMain Menu:");
        System.out.println("1. View Current Order");
        System.out.println("2. View Available Products/Recipes");
        System.out.println("3. Add Item Manually (with recipe and quantity)");
        System.out.println("4. Remove Last Item");
        System.out.println("5. View Pricing Breakdown (with discount)");
        System.out.println("6. Checkout (view receipt)");
        System.out.println("7. EventBus Demo (demonstrate event wiring)");
        System.out.println("8. Apply/Change Discount");
        System.out.println("9. Change Tax Rate");
        System.out.println("10. New Order (start new order)");
        System.out.println("11. Exit");
    }

    private static void viewOrder() {
        System.out.println("Current Order Status");
        System.out.println();

        var orderOpt = repo.findById(currentOrderId);
        if (orderOpt.isEmpty()) {
            System.out.println("Order not found!");
            return;
        }

        Order order = orderOpt.get();
        System.out.println("Order ID: " + order.id());
        System.out.println("Items in order: " + order.items().size());
        System.out.println();

        if (order.items().isEmpty()) {
            System.out.println("(No items in order)");
        } else {
            System.out.println("Items:");
            int index = 1;
            for (LineItem item : order.items()) {
                System.out.printf("  %d. %s x%d = %s%n",
                        index++,
                        item.product().name(),
                        item.quantity(),
                        item.lineTotal());
            }
        }

        System.out.println();
        System.out.println("Subtotal: " + order.subtotal());
        System.out.println("Tax (" + taxPercent + "%): " + order.taxAtPercent(taxPercent));
        System.out.println("Total: " + order.totalWithTax(taxPercent));
    }

    private static void viewAvailableProducts() {
        System.out.println("Available Products & Recipes");
        System.out.println();
        System.out.println("Base Products:");
        System.out.println("  ESP  - Espresso (EUR 2.50)");
        System.out.println("  LAT  - Latte (EUR 3.20)");
        System.out.println("  CAP  - Cappuccino (EUR 3.00)");
        System.out.println("  AME  - Americano (EUR 2.80)");
        System.out.println();
        System.out.println("Add-ons (use + to combine):");
        System.out.println("  SHOT - Extra Shot");
        System.out.println("  OAT  - Oat Milk");
        System.out.println("  L    - Large Size");
        System.out.println();
        System.out.println("Example Recipes:");
        System.out.println("  ESP              - Plain Espresso");
        System.out.println("  ESP+SHOT         - Espresso with extra shot");
        System.out.println("  LAT+L            - Large Latte");
        System.out.println("  ESP+SHOT+OAT     - Espresso with shot and oat milk");
        System.out.println("  CAP+OAT+L        - Large Cappuccino with oat milk");
    }

    private static void addItemManually() {
        System.out.println("Add Item Manually");
        System.out.println("Available base products:");
        System.out.println("  ESP  - Espresso (EUR 2.50)");
        System.out.println("  LAT  - Latte (EUR 3.20)");
        System.out.println("  CAP  - Cappuccino (EUR 3.00)");
        System.out.println("  AME  - Americano (EUR 2.80)");
        System.out.println();
        System.out.println("Available addons (use + to combine):");
        System.out.println("  SHOT - Extra Shot");
        System.out.println("  OAT  - Oat Milk");
        System.out.println("  L    - Large Size");
        System.out.println();
        System.out.println("Examples: ESP, ESP+SHOT, LAT+L, ESP+SHOT+OAT");
        System.out.println();

        String recipe = getStringInput("Enter recipe: ").trim().toUpperCase();
        if (recipe.isEmpty()) {
            System.out.println("Recipe cannot be empty!");
            return;
        }

        int qty = getIntInput("Enter quantity: ");
        if (qty <= 0) {
            System.out.println("Quantity must be greater than 0!");
            return;
        }

        try {
            controller.addItem(currentOrderId, recipe, qty);
            System.out.println("Item added successfully!");
        } catch (IllegalArgumentException ex) {
            System.out.println("Error adding item: " + ex.getMessage());
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private static void removeLastItem() {
        System.out.println("Remove Last Item");

        var orderOpt = repo.findById(currentOrderId);
        if (orderOpt.isEmpty()) {
            System.out.println("Order not found!");
            return;
        }

        Order order = orderOpt.get();
        if (order.items().isEmpty()) {
            System.out.println("Cannot remove - order is empty!");
            return;
        }

        int sizeBefore = order.items().size();
        order.removeLastItem();
        repo.save(order);
        
        System.out.println("Last item removed successfully!");
        System.out.println("Items before: " + sizeBefore + ", Items remaining: " + order.items().size());
    }

    private static void viewPricingBreakdown() {
        System.out.println("Pricing Breakdown (with Discount)");
        System.out.println();

        var orderOpt = repo.findById(currentOrderId);
        if (orderOpt.isEmpty()) {
            System.out.println("Order not found!");
            return;
        }

        Order order = orderOpt.get();
        if (order.items().isEmpty()) {
            System.out.println("Order is empty - no pricing to show.");
            return;
        }

        var subtotal = order.subtotal();
        var pricingResult = pricing.price(subtotal);

        System.out.println("Subtotal: " + pricingResult.subtotal());
        if (pricingResult.discount().asBigDecimal().signum() > 0) {
            System.out.println("Discount (" + discountPercent + "%): -" + pricingResult.discount());
            System.out.println("After Discount: " + 
                Money.of(subtotal.asBigDecimal().subtract(pricingResult.discount().asBigDecimal())));
        } else {
            System.out.println("Discount: EUR 0.00 (none applied)");
        }
        System.out.println("Tax (" + taxPercent + "%): " + pricingResult.tax());
        System.out.println("Total: " + pricingResult.total());
    }

    private static void checkout() {
        System.out.println("Checkout");

        var orderOpt = repo.findById(currentOrderId);
        if (orderOpt.isEmpty()) {
            System.out.println("Order not found!");
            return;
        }

        Order order = orderOpt.get();
        if (order.items().isEmpty()) {
            System.out.println("Cannot checkout - order is empty!");
            return;
        }

        try {
            // Emit OrderCreated event (if not already emitted)
            eventBus.emit(new OrderCreated(currentOrderId));
            
            // Show receipt first
            String receipt = controller.checkout(currentOrderId, taxPercent);
            System.out.println("\n=== Receipt ===");
            System.out.println(receipt);
            System.out.println("================");
            
            // Calculate total with discount
            var subtotal = order.subtotal();
            var pricingResult = pricing.price(subtotal);
            Money total = pricingResult.total();
            
            System.out.println("\n=== Payment ===");
            System.out.println("Total to pay: " + total);
            System.out.println();
            System.out.println("Payment methods:");
            System.out.println("  1. Cash Payment");
            System.out.println("  2. Card Payment");
            System.out.println();
            
            int paymentMethod = getIntInput("Choose payment method (1-2): ");
            
            PaymentStrategy strategy = null;
            switch (paymentMethod) {
                case 1 -> {
                    // Cash payment - ask for amount paid and calculate change
                    double amountPaid = getDoubleInput("Enter amount customer paid: ");
                    Money paid = Money.of(amountPaid);
                    java.math.BigDecimal changeAmount = paid.asBigDecimal().subtract(total.asBigDecimal());
                    
                    if (changeAmount.compareTo(java.math.BigDecimal.ZERO) < 0) {
                        // Insufficient payment
                        java.math.BigDecimal shortBy = total.asBigDecimal().subtract(paid.asBigDecimal());
                        System.out.println();
                        System.out.println("Error: Amount paid (" + paid + ") is less than total (" + total + ")!");
                        System.out.println("  Short by: " + Money.of(shortBy));
                        System.out.println("Payment cancelled.");
                        return;
                    }
                    
                    Money change = Money.of(changeAmount);
                    
                    System.out.println();
                    System.out.println("=== Cash Payment Summary ===");
                    System.out.println("Order total:  " + total);
                    System.out.println("Amount paid:  " + paid);
                    System.out.println("Change:       " + change);
                    System.out.println("===========================");
                    
                    strategy = new CashPayment();
                }
                case 2 -> {
                    String cardNum = getStringInput("Enter card number (at least 4 digits): ");
                    if (cardNum.length() < 4) {
                        System.out.println("Invalid card number! Payment cancelled.");
                        return;
                    }
                    strategy = new CardPayment(cardNum);
                }
                default -> {
                    System.out.println("Invalid payment method! Payment cancelled.");
                    return;
                }
            }
            
            // Process payment
            if (strategy != null) {
                strategy.pay(total);
                System.out.println("Payment processed successfully!");
                
                // Emit OrderPaid event
                eventBus.emit(new OrderPaid(currentOrderId));
            }
        } catch (Exception ex) {
            System.out.println("Error during checkout: " + ex.getMessage());
        }
    }
    
    private static double getDoubleInput(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                double value = Double.parseDouble(input);
                if (value < 0) {
                    System.out.print("Amount cannot be negative. Please try again: ");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.print("Invalid number. Please try again: ");
            }
        }
    }

    private static void eventBusDemo() {
        System.out.println("EventBus Component Connector Demo");
        System.out.println();
        System.out.println("Demonstrating event-driven communication:");
        System.out.println();

        // Create a temporary event bus for demo
        EventBus demoBus = new EventBus();
        
        // Register multiple handlers
        demoBus.on(OrderCreated.class, e -> 
            System.out.println("  [UI Handler] Order created: " + e.orderId()));
        demoBus.on(OrderCreated.class, e -> 
            System.out.println("  [Analytics Handler] Logging order: " + e.orderId()));
        demoBus.on(OrderPaid.class, e -> 
            System.out.println("  [Notification Handler] Order paid: " + e.orderId() + " - sending receipt"));

        System.out.println("Registered 3 event handlers:");
        System.out.println("  - UI Handler (OrderCreated)");
        System.out.println("  - Analytics Handler (OrderCreated)");
        System.out.println("  - Notification Handler (OrderPaid)");
        System.out.println();

        System.out.println("Emitting OrderCreated event...");
        demoBus.emit(new OrderCreated(9999L));
        System.out.println();

        System.out.println("Emitting OrderPaid event...");
        demoBus.emit(new OrderPaid(9999L));
        System.out.println();

        System.out.println("Key Points:");
        System.out.println("  - Multiple handlers can subscribe to the same event");
        System.out.println("  - Handlers are decoupled from event publishers");
        System.out.println("  - Easy to add new handlers without modifying existing code");
        System.out.println("  - EventBus maintains architectural boundaries");
    }

    private static void applyDiscount() {
        System.out.println("Apply/Change Discount");
        System.out.println("Current discount: " + discountPercent + "%");
        System.out.println();
        System.out.println("Enter discount percentage (0-100):");
        System.out.println("  - 0 = No discount");
        System.out.println("  - 5 = 5% loyalty discount");
        System.out.println("  - 10 = 10% discount");
        System.out.println("  - etc.");
        System.out.println();

        int newDiscount = getIntInput("Enter discount percent (0-100): ");
        if (newDiscount < 0 || newDiscount > 100) {
            System.out.println("Invalid discount! Must be between 0 and 100.");
            return;
        }

        discountPercent = newDiscount;
        updatePricingAndCheckout();
        
        if (discountPercent == 0) {
            System.out.println("Discount removed (no discount applied)");
        } else {
            System.out.println("Discount set to " + discountPercent + "%");
        }
    }

    private static void changeTaxRate() {
        System.out.println("Change Tax Rate");
        System.out.println("Current tax rate: " + taxPercent + "%");
        System.out.println();

        int newRate = getIntInput("Enter new tax rate (0-100): ");
        if (newRate < 0 || newRate > 100) {
            System.out.println("Invalid tax rate! Must be between 0 and 100.");
            return;
        }

        taxPercent = newRate;
        updatePricingAndCheckout();
        System.out.println("Tax rate changed to " + taxPercent + "%");
    }

    private static void newOrder() {
        System.out.println("New Order");
        System.out.print("Are you sure you want to start a new order? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("yes") || confirm.equals("y")) {
            currentOrderId = System.currentTimeMillis() % 10000;
            controller.createOrder(currentOrderId);
            System.out.println("New order created! Order ID: " + currentOrderId);
        } else {
            System.out.println("New order cancelled.");
        }
    }

    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Invalid number. Please try again: ");
            }
        }
    }

    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
}
