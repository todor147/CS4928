package com.cafepos.demo;

import com.cafepos.order.*;
import com.cafepos.payment.*;
import com.cafepos.command.*;
import com.cafepos.common.Money;
import com.cafepos.DiscountPolicyFactory;
import com.cafepos.pricing.PricingService;
import com.cafepos.pricing.FixedRateTaxPolicy;
import com.cafepos.menu.*;
import com.cafepos.state.OrderFSM;
import java.util.Scanner;

public final class Week9Demo_Interactive {
    private static final Scanner scanner = new Scanner(System.in);
    private static Order order;
    private static OrderService service;
    private static PosRemote remote;
    private static OrderFSM orderState;
    private static Menu cafeMenu;
    private static final int TAX_PERCENT = 10;

    public static void main(String[] args) {
        System.out.println("Week 9 - Interactive Demo");
        System.out.println("POS System - Composite, Iterator, and State Patterns");
        System.out.println();

        initializeSystem();

        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getIntInput("Enter your choice: ");
            System.out.println();

            switch (choice) {
                case 1 -> viewMenu();
                case 2 -> viewVegetarianOptions();
                case 3 -> addItemManually();
                case 4 -> addItemFromMenu();
                case 5 -> configureSlot();
                case 6 -> pressSlot();
                case 7 -> undo();
                case 8 -> viewOrder();
                case 9 -> manageOrderState();
                case 10 -> pay();
                case 11 -> clearOrder();
                case 12 -> applyDiscountQuickly();
                case 13 -> running = false;
                default -> System.out.println("Invalid choice. Please try again.");
            }

            if (running) {
                System.out.println();
            }
        }

        System.out.println("Thank you for using Week 9 POS System!");
        scanner.close();
    }

    private static void initializeSystem() {
        order = new Order(OrderIds.next());
        service = new OrderService(order);
        remote = new PosRemote(10);
        orderState = new OrderFSM();

        cafeMenu = new Menu("CAFÉ MENU");
        Menu drinksMenu = new Menu("Drinks");
        Menu coffeeMenu = new Menu("Coffee");
        coffeeMenu.add(new MenuItem("Espresso", Money.of(2.50), true));
        coffeeMenu.add(new MenuItem("Latte (Large)", Money.of(3.90), true));
        coffeeMenu.add(new MenuItem("Cappuccino", Money.of(3.00), true));
        drinksMenu.add(coffeeMenu);
        cafeMenu.add(drinksMenu);

        Menu dessertsMenu = new Menu("Desserts");
        dessertsMenu.add(new MenuItem("Chocolate Cake", Money.of(4.50), true));
        dessertsMenu.add(new MenuItem("Cheesecake", Money.of(4.00), false));
        dessertsMenu.add(new MenuItem("Fruit Salad", Money.of(3.50), true));
        cafeMenu.add(dessertsMenu);

        System.out.println("System initialized!");
        System.out.println("Order ID: " + order.id());
        System.out.println("Order Status: " + orderState.status());
        System.out.println();
    }

    private static void displayMenu() {
        System.out.println("\nMain Menu:");
        System.out.println("1. View Full Menu");
        System.out.println("2. View Vegetarian Options");
        System.out.println("3. Add Item Manually (with recipe and quantity)");
        System.out.println("4. Add Item From Menu");
        System.out.println("5. Configure Slot (assign command to button)");
        System.out.println("6. Press Slot (execute command from button)");
        System.out.println("7. Undo Last Action");
        System.out.println("8. View Current Order");
        System.out.println("9. Manage Order State");
        System.out.println("10. Pay Order (choose payment method)");
        System.out.println("11. Clear Order (start new order)");
        System.out.println("12. Apply Discount Quickly");
        System.out.println("13. Exit");
    }

    private static void viewMenu() {
        System.out.println("Full Menu");
        System.out.println();
        cafeMenu.print();
    }

    private static void viewVegetarianOptions() {
        System.out.println("Vegetarian Options");
        System.out.println();
        for (MenuItem item : cafeMenu.vegetarianItems()) {
            item.print();
        }
    }

    private static void addItemFromMenu() {
        System.out.println("Add Item From Menu");
        System.out.println();
        System.out.println("Available items:");
        int index = 1;
        java.util.List<MenuItem> allItems = new java.util.ArrayList<>();
        for (MenuComponent component : cafeMenu) {
            if (component instanceof MenuItem item) {
                System.out.println("  " + index + ". " + item.name() + " - " + item.price() + " EUR" + (item.vegetarian() ? " (v)" : ""));
                allItems.add(item);
                index++;
            }
        }
        System.out.println();

        if (allItems.isEmpty()) {
            System.out.println("No menu items available.");
            return;
        }

        int choice = getIntInput("Select item number: ");
        if (choice < 1 || choice > allItems.size()) {
            System.out.println("Invalid selection!");
            return;
        }

        MenuItem selectedItem = allItems.get(choice - 1);
        int qty = getIntInput("Enter quantity: ");
        if (qty <= 0) {
            System.out.println("Quantity must be greater than 0!");
            return;
        }

        String recipe = convertMenuItemToRecipe(selectedItem.name());
        if (recipe == null) {
            System.out.println("Could not convert menu item to recipe. Please use manual entry.");
            return;
        }

        try {
            AddItemCommand command = new AddItemCommand(service, recipe, qty);
            command.execute();
            try {
                java.lang.reflect.Field historyField = PosRemote.class.getDeclaredField("history");
                historyField.setAccessible(true);
                @SuppressWarnings("unchecked")
                java.util.Deque<Command> history = (java.util.Deque<Command>) historyField.get(remote);
                history.push(command);
            } catch (Exception e) {
            }
            System.out.println("Item added successfully! (Added to undo history)");
        } catch (Exception e) {
            System.out.println("Error adding item: " + e.getMessage());
        }
    }

    private static String convertMenuItemToRecipe(String itemName) {
        String upper = itemName.toUpperCase();
        if (upper.contains("ESPRESSO")) return "ESP";
        if (upper.contains("LATTE")) return "LAT+L";
        if (upper.contains("CAPPUCCINO")) return "CAP";
        if (upper.contains("CHOCOLATE CAKE")) return null;
        if (upper.contains("CHEESECAKE")) return null;
        if (upper.contains("FRUIT SALAD")) return null;
        return null;
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
            command.execute();
            try {
                java.lang.reflect.Field historyField = PosRemote.class.getDeclaredField("history");
                historyField.setAccessible(true);
                @SuppressWarnings("unchecked")
                java.util.Deque<Command> history = (java.util.Deque<Command>) historyField.get(remote);
                history.push(command);
            } catch (Exception e) {
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
        System.out.println("Order State: " + orderState.status());
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

    private static void manageOrderState() {
        System.out.println("Manage Order State");
        System.out.println();
        System.out.println("Current State: " + orderState.status());
        System.out.println();
        System.out.println("Available actions:");
        System.out.println("  1. pay()");
        System.out.println("  2. prepare()");
        System.out.println("  3. markReady()");
        System.out.println("  4. deliver()");
        System.out.println("  5. cancel()");
        System.out.println();

        int action = getIntInput("Select action (1-5): ");
        System.out.println();

        switch (action) {
            case 1 -> orderState.pay();
            case 2 -> orderState.prepare();
            case 3 -> orderState.markReady();
            case 4 -> orderState.deliver();
            case 5 -> orderState.cancel();
            default -> {
                System.out.println("Invalid action!");
                return;
            }
        }

        System.out.println("New State: " + orderState.status());
    }

    private static void pay() {
        System.out.println("Pay Order");

        if (order.items().isEmpty()) {
            System.out.println("Cannot pay - order is empty!");
            return;
        }

        if (!orderState.status().equals("NEW") && !orderState.status().equals("PREPARING")) {
            System.out.println("Order state is " + orderState.status() + ". Payment should occur from NEW state.");
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
                    double amountPaid = getDoubleInput("Enter amount customer paid: ");
                    Money paid = Money.of(amountPaid);
                    java.math.BigDecimal changeAmount = paid.getAmount().subtract(total.getAmount());

                    if (changeAmount.compareTo(java.math.BigDecimal.ZERO) < 0) {
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

            if (strategy != null) {
                PayOrderCommand payCommand = new PayOrderCommand(service, strategy, TAX_PERCENT);
                payCommand.execute();
                orderState.pay();
                System.out.println("Payment processed successfully!");

                System.out.println();
                System.out.println("Order cleared - ready for next customer!");
                order = new Order(OrderIds.next());
                service = new OrderService(order);
                remote = new PosRemote(10);
                orderState = new OrderFSM();
                System.out.println("New order ID: " + order.id());
                System.out.println("New order state: " + orderState.status());
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
            orderState = new OrderFSM();
            System.out.println("New order created! Order ID: " + order.id());
            System.out.println("Order state: " + orderState.status());
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
            command.execute();
            try {
                java.lang.reflect.Field historyField = PosRemote.class.getDeclaredField("history");
                historyField.setAccessible(true);
                @SuppressWarnings("unchecked")
                java.util.Deque<Command> history = (java.util.Deque<Command>) historyField.get(remote);
                history.push(command);
            } catch (Exception e) {
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

