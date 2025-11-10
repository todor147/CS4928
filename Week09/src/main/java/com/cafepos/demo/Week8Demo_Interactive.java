package com.cafepos.demo;

import com.cafepos.order.*;
import com.cafepos.payment.*;
import com.cafepos.command.*;
import com.cafepos.common.Money;
import com.cafepos.DiscountPolicyFactory;
import com.cafepos.pricing.PricingService;
import com.cafepos.pricing.FixedRateTaxPolicy;
import java.util.Scanner;

public final class Week8Demo_Interactive {
    private static final Scanner scanner = new Scanner(System.in);
    private static Order order;
    private static OrderService service;
    private static PosRemote remote;
    private static final int TAX_PERCENT = 10;

    public static void main(String[] args) {
        System.out.println("Week 9 - Command Pattern Interactive Demo");
        System.out.println("POS System - Interactive Command Pattern Demo");
        System.out.println();

        // Initialize system
        order = new Order(OrderIds.next());
        service = new OrderService(order);
        remote = new PosRemote(10); // 10 slots available

        System.out.println("System initialized!");
        System.out.println("Order ID: " + order.id());
        System.out.println();

        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getIntInput("Enter your choice: ");
            System.out.println();

            switch (choice) {
                case 1 -> addItemManually();
                case 2 -> configureSlot();
                case 3 -> pressSlot();
                case 4 -> undo();
                case 5 -> viewOrder();
                case 6 -> pay();
                case 7 -> clearOrder();
                case 8 -> applyDiscountQuickly();
                case 9 -> running = false;
                default -> System.out.println("Invalid choice. Please try again.");
            }

            if (running) {
                System.out.println();
            }
        }

        System.out.println("Thank you for using the Command Pattern Demo!");
        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("\nMain Menu:");
        System.out.println("1. Add Item Manually (with recipe and quantity)");
        System.out.println("2. Configure Slot (assign command to button)");
        System.out.println("3. Press Slot (execute command from button)");
        System.out.println("4. Undo Last Action");
        System.out.println("5. View Current Order");
        System.out.println("6. Pay Order (choose payment method)");
        System.out.println("7. Clear Order (start new order)");
        System.out.println("8. Apply Discount Quickly");
        System.out.println("9. Exit");
    }

    private static void addItemManually() {
        System.out.println("Add Item Manually");
        System.out.println("Available base products:");
        System.out.println("  ESP  - Espresso (€2.50)");
        System.out.println("  LAT  - Latte (€3.20)");
        System.out.println("  CAP  - Cappuccino (€3.00)");
        System.out.println("  AME  - Americano (€2.80)");
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
            AddItemCommand command = new AddItemCommand(service, recipe, qty);
            // Execute command
            command.execute();
            // Add to undo history using reflection
            try {
                java.lang.reflect.Field historyField = PosRemote.class.getDeclaredField("history");
                historyField.setAccessible(true);
                @SuppressWarnings("unchecked")
                java.util.Deque<Command> history = (java.util.Deque<Command>) historyField.get(remote);
                history.push(command);
            } catch (Exception e) {
                // If reflection fails, just continue without undo support for this command
            }
            System.out.println("Item added successfully! (Added to undo history)");
        } catch (Exception e) {
            System.out.println("Error adding item: " + e.getMessage());
        }
    }

    private static void configureSlot() {
        System.out.println("Configure Slot");
        System.out.println("Available slots: 0-9");
        int slot = getIntInput("Enter slot number (0-9): ");
        if (slot < 0 || slot >= 10) {
            System.out.println("Invalid slot number!");
            return;
        }

        System.out.println();
        System.out.println("Command types:");
        System.out.println("  1. AddItemCommand");
        System.out.println("  2. PayOrderCommand (Card)");
        System.out.println("  3. PayOrderCommand (Cash)");
        System.out.println("  4. PayOrderCommand (Wallet)");
        System.out.println("  5. ApplyDiscountCommand");
        System.out.println();

        int cmdType = getIntInput("Enter command type (1-5): ");

        try {
            Command command = null;
            switch (cmdType) {
                case 1 -> {
                    System.out.println();
                    System.out.println("Enter recipe (e.g., ESP, LAT+L, ESP+SHOT+OAT):");
                    String recipe = getStringInput("Recipe: ").trim().toUpperCase();
                    int qty = getIntInput("Quantity: ");
                    command = new AddItemCommand(service, recipe, qty);
                }
                case 2 -> {
                    String cardNum = getStringInput("Enter card number (at least 4 digits): ");
                    command = new PayOrderCommand(service, new CardPayment(cardNum), TAX_PERCENT);
                }
                case 3 -> {
                    command = new PayOrderCommand(service, new CashPayment(), TAX_PERCENT);
                }
                case 4 -> {
                    String walletId = getStringInput("Enter wallet ID: ");
                    command = new PayOrderCommand(service, new WalletPayment(walletId), TAX_PERCENT);
                }
                case 5 -> {
                    System.out.println();
                    System.out.println("Available Discounts:");
                    System.out.println("  LOYAL5  - 5% Loyalty Discount");
                    System.out.println("  COUPON1 - 1.00 EUR Coupon");
                    System.out.println("  NONE    - Remove Discount");
                    String discountCode = getStringInput("Enter discount code: ").trim().toUpperCase();
                    if (discountCode.isEmpty()) {
                        discountCode = "NONE";
                    }
                    command = new ApplyDiscountCommand(service, discountCode);
                }
                default -> {
                    System.out.println("Invalid command type!");
                    return;
                }
            }

            remote.setSlot(slot, command);
            System.out.println("Slot " + slot + " configured successfully!");
        } catch (Exception e) {
            System.out.println("Error configuring slot: " + e.getMessage());
        }
    }

    private static void pressSlot() {
        System.out.println("Press Slot");
        int slot = getIntInput("Enter slot number to press (0-9): ");
        if (slot < 0 || slot >= 10) {
            System.out.println("Invalid slot number!");
            return;
        }

        try {
            remote.press(slot);
            System.out.println("Command executed!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void undo() {
        System.out.println("Undo Last Action");
        try {
            remote.undo();
            System.out.println("Last action undone!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewOrder() {
        System.out.println("Current Order Status");
        System.out.println();
        System.out.println("Order ID: " + order.id());
        System.out.println("Items in order: " + order.getItemCount());
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
        Money subtotal = order.subtotal();
        System.out.println("Subtotal: " + subtotal);

        // Calculate discount if one is applied
        String discountCode = service.getDiscountCode();
        if (discountCode != null && !discountCode.isEmpty() && !discountCode.equalsIgnoreCase("NONE")) {
            var discountPolicy = DiscountPolicyFactory.createDiscountPolicy(discountCode);
            var taxPolicy = new FixedRateTaxPolicy(TAX_PERCENT);
            var pricingService = new PricingService(discountPolicy, taxPolicy);
            var pricingResult = pricingService.price(subtotal);

            if (pricingResult.discount().asBigDecimal().signum() > 0) {
                System.out.println("Discount: -" + pricingResult.discount() + " (" + discountCode + ")");
                Money discounted = Money.of(subtotal.asBigDecimal().subtract(pricingResult.discount().asBigDecimal()));
                if (discounted.asBigDecimal().signum() < 0)
                    discounted = Money.zero();
                System.out.println("After discount: " + discounted);
            }
            System.out.println("Tax (" + TAX_PERCENT + "%): " + pricingResult.tax());
            System.out.println("Total: " + pricingResult.total());
        } else {
            System.out.println("Tax (" + TAX_PERCENT + "%): " + order.taxAtPercent(TAX_PERCENT));
            System.out.println("Total: " + order.totalWithTax(TAX_PERCENT));
            if (discountCode == null || discountCode.isEmpty() || discountCode.equalsIgnoreCase("NONE")) {
                System.out.println("(No discount applied)");
            }
        }
    }

    private static void pay() {
        System.out.println("Pay Order");

        if (order.items().isEmpty()) {
            System.out.println("Cannot pay - order is empty!");
            return;
        }

        Money total = order.totalWithTax(TAX_PERCENT);
        System.out.println("Order total: " + total + " EUR");
        System.out.println();
        System.out.println("Payment methods:");
        System.out.println("  1. Card Payment");
        System.out.println("  2. Cash Payment");
        System.out.println("  3. Wallet Payment");
        System.out.println();

        int method = getIntInput("Choose payment method (1-3): ");
        System.out.println();

        PaymentStrategy strategy = null;
        try {
            switch (method) {
                case 1 -> {
                    String cardNum = getStringInput("Enter card number (at least 4 digits): ");
                    strategy = new CardPayment(cardNum);
                }
                case 2 -> {
                    // Cash payment - ask for amount paid and calculate change
                    double amountPaid = getDoubleInput("Enter amount customer paid: ");
                    Money paid = Money.of(amountPaid);
                    java.math.BigDecimal changeAmount = paid.getAmount().subtract(total.getAmount());

                    if (changeAmount.compareTo(java.math.BigDecimal.ZERO) < 0) {
                        // Insufficient payment
                        java.math.BigDecimal shortBy = total.getAmount().subtract(paid.getAmount());
                        System.out.println();
                        System.out.println(
                                "Error: Amount paid (" + paid + " EUR) is less than total (" + total + " EUR)!");
                        System.out.println("  Short by: " + shortBy + " EUR");
                        System.out.println("Payment cancelled.");
                        return;
                    }

                    Money change = Money.of(changeAmount);

                    System.out.println();
                    System.out.println("Cash Payment Summary");
                    System.out.println("Order total:  " + total + " EUR");
                    System.out.println("Amount paid:  " + paid + " EUR");
                    System.out.println("Change:       " + change + " EUR");
                    System.out.println();

                    strategy = new CashPayment();
                }
                case 3 -> {
                    String walletId = getStringInput("Enter wallet ID: ");
                    strategy = new WalletPayment(walletId);
                }
                default -> {
                    System.out.println("Invalid payment method!");
                    return;
                }
            }
            ;

            if (strategy != null) {
                PayOrderCommand payCommand = new PayOrderCommand(service, strategy, TAX_PERCENT);
                payCommand.execute();
                System.out.println("Payment processed successfully!");

                // Clear the order after payment
                System.out.println();
                System.out.println("Order cleared - ready for next customer!");
                order = new Order(OrderIds.next());
                service = new OrderService(order);
                remote = new PosRemote(10);
                System.out.println("New order ID: " + order.id());
            }
        } catch (Exception e) {
            System.out.println("Payment error: " + e.getMessage());
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

    private static void clearOrder() {
        System.out.println("Clear Order");
        System.out.print("Are you sure you want to start a new order? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("yes") || confirm.equals("y")) {
            order = new Order(OrderIds.next());
            service = new OrderService(order);
            remote = new PosRemote(10);
            System.out.println("New order created! Order ID: " + order.id());
        } else {
            System.out.println("Order cleared cancelled.");
        }
    }

    private static void applyDiscountQuickly() {
        System.out.println("Apply Discount Quickly");
        System.out.println();
        System.out.println("Available Discounts:");
        System.out.println("  LOYAL5  - 5% Loyalty Discount");
        System.out.println("  COUPON1 - 1.00 EUR Coupon");
        System.out.println("  NONE    - Remove Discount");
        System.out.println();

        String discountCode = getStringInput("Enter discount code: ").trim().toUpperCase();
        if (discountCode.isEmpty()) {
            discountCode = "NONE";
        }

        try {
            ApplyDiscountCommand command = new ApplyDiscountCommand(service, discountCode);
            // Execute command
            command.execute();
            // Add to undo history using reflection
            try {
                java.lang.reflect.Field historyField = PosRemote.class.getDeclaredField("history");
                historyField.setAccessible(true);
                @SuppressWarnings("unchecked")
                java.util.Deque<Command> history = (java.util.Deque<Command>) historyField.get(remote);
                history.push(command);
            } catch (Exception e) {
                // If reflection fails, just continue without undo support for this command
            }

            if (discountCode.equals("NONE")) {
                System.out.println("Discount removed! (Added to undo history)");
            } else {
                System.out.println("Discount '" + discountCode + "' applied successfully! (Added to undo history)");
            }
        } catch (Exception e) {
            System.out.println("Error applying discount: " + e.getMessage());
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
