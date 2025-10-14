import java.util.*;

public class CafePOSDemo {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Catalog catalog = new InMemoryCatalog();
    private static final Map<Long, Order> orders = new HashMap<>();
    
    // Observer instances (shared across all orders)
    private static final KitchenDisplay kitchen = new KitchenDisplay();
    private static final DeliveryDesk delivery = new DeliveryDesk();
    private static final CustomerNotifier notifier = new CustomerNotifier();

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║   WELCOME TO CAFE POS SYSTEM         ║");
        System.out.println("║   Observer Pattern Demo - Week 04    ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.println();
        
        initializeCatalog();
        
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
                    addItemToOrder();
                    break;
                case 3:
                    viewOrder();
                    break;
                case 4:
                    payForOrder();
                    break;
                case 5:
                    markOrderReady();
                    break;
                case 6:
                    viewAllOrders();
                    break;
                case 7:
                    viewCatalog();
                    break;
                case 8:
                    demonstrateObserverPattern();
                    break;
                case 9:
                    running = false;
                    System.out.println("Thank you for using Cafe POS System!");
                    break;
                default:
                    System.out.println("❌ Invalid choice. Please try again.");
            }
            
            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
        
        scanner.close();
    }

    private static void initializeCatalog() {
        catalog.add(new SimpleProduct("COFFEE", "Coffee", Money.of(3.50)));
        catalog.add(new SimpleProduct("TEA", "Tea", Money.of(2.50)));
        catalog.add(new SimpleProduct("LATTE", "Latte", Money.of(4.50)));
        catalog.add(new SimpleProduct("CAPPUCCINO", "Cappuccino", Money.of(4.00)));
        catalog.add(new SimpleProduct("ESPRESSO", "Espresso", Money.of(2.75)));
        catalog.add(new SimpleProduct("CROISSANT", "Croissant", Money.of(3.00)));
        catalog.add(new SimpleProduct("MUFFIN", "Blueberry Muffin", Money.of(3.50)));
        catalog.add(new SimpleProduct("SANDWICH", "Ham & Cheese Sandwich", Money.of(6.50)));
        catalog.add(new SimpleProduct("COOKIE", "Chocolate Cookie", Money.of(2.00)));
        catalog.add(new SimpleProduct("CAKE", "Chocolate Cake Slice", Money.of(5.00)));
    }

    private static void displayMenu() {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║          MAIN MENU                   ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║ 1. Create New Order                  ║");
        System.out.println("║ 2. Add Item to Order                 ║");
        System.out.println("║ 3. View Order Details                ║");
        System.out.println("║ 4. Pay for Order                     ║");
        System.out.println("║ 5. Mark Order as Ready               ║");
        System.out.println("║ 6. View All Orders                   ║");
        System.out.println("║ 7. View Product Catalog              ║");
        System.out.println("║ 8. Demo: Observer Pattern            ║");
        System.out.println("║ 9. Exit                              ║");
        System.out.println("╚══════════════════════════════════════╝");
    }

    private static void createNewOrder() {
        long orderId = OrderIds.next();
        Order order = new Order(orderId);
        
        // Register all observers
        order.register(kitchen);
        order.register(delivery);
        order.register(notifier);
        
        orders.put(orderId, order);
        
        System.out.println("✅ New order created with ID: " + orderId);
        System.out.println("   Observers registered: Kitchen Display, Delivery Desk, Customer Notifier");
    }

    private static void addItemToOrder() {
        if (orders.isEmpty()) {
            System.out.println("❌ No orders exist. Please create an order first.");
            return;
        }
        
        long orderId = getIntInput("Enter Order ID: ");
        Order order = orders.get(orderId);
        
        if (order == null) {
            System.out.println("❌ Order not found!");
            return;
        }
        
        viewCatalog();
        System.out.print("\nEnter Product ID: ");
        String productId = scanner.nextLine().trim().toUpperCase();
        
        Optional<Product> productOpt = catalog.findById(productId);
        if (!productOpt.isPresent()) {
            System.out.println("❌ Product not found!");
            return;
        }
        
        int quantity = getIntInput("Enter quantity: ");
        
        Product product = productOpt.get();
        LineItem item = new LineItem(product, quantity);
        
        System.out.println("\n--- Adding item to order (Observer Pattern in action) ---");
        order.addItem(item);
        System.out.println("--- Item added successfully ---");
        
        System.out.println("\n✅ Added " + quantity + "x " + product.name() + " to Order #" + orderId);
    }

    private static void viewOrder() {
        if (orders.isEmpty()) {
            System.out.println("❌ No orders exist.");
            return;
        }
        
        long orderId = getIntInput("Enter Order ID: ");
        Order order = orders.get(orderId);
        
        if (order == null) {
            System.out.println("❌ Order not found!");
            return;
        }
        
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║        ORDER #" + orderId + "                   ║");
        System.out.println("╚══════════════════════════════════════╝");
        
        if (order.getItems().isEmpty()) {
            System.out.println("  (No items)");
        } else {
            System.out.println("\nItems:");
            for (LineItem item : order.getItems()) {
                System.out.printf("  • %dx %-25s %s\n", 
                    item.quantity(), 
                    item.product().name(),
                    item.lineTotal());
            }
            System.out.println("\n  ─────────────────────────────────────");
            System.out.println("  Subtotal:           " + order.subtotal());
            System.out.println("  Tax (10%):          " + order.taxAtPercent(10));
            System.out.println("  ─────────────────────────────────────");
            System.out.println("  TOTAL:              " + order.totalWithTax(10));
        }
    }

    private static void payForOrder() {
        if (orders.isEmpty()) {
            System.out.println("❌ No orders exist.");
            return;
        }
        
        long orderId = getIntInput("Enter Order ID: ");
        Order order = orders.get(orderId);
        
        if (order == null) {
            System.out.println("❌ Order not found!");
            return;
        }
        
        if (order.getItems().isEmpty()) {
            System.out.println("❌ Cannot pay for an empty order!");
            return;
        }
        
        System.out.println("\nSelect Payment Method:");
        System.out.println("1. Cash");
        System.out.println("2. Card");
        
        int choice = getIntInput("Enter choice: ");
        PaymentStrategy strategy;
        
        if (choice == 1) {
            strategy = new CashPayment();
        } else if (choice == 2) {
            System.out.print("Enter card number: ");
            String cardNumber = scanner.nextLine().trim();
            strategy = new CardPayment(cardNumber);
        } else {
            System.out.println("❌ Invalid payment method!");
            return;
        }
        
        System.out.println("\n--- Processing payment (Observer Pattern in action) ---");
        order.pay(strategy);
        System.out.println("--- Payment processed successfully ---");
        
        System.out.println("\n✅ Order #" + orderId + " has been paid!");
    }

    private static void markOrderReady() {
        if (orders.isEmpty()) {
            System.out.println("❌ No orders exist.");
            return;
        }
        
        long orderId = getIntInput("Enter Order ID: ");
        Order order = orders.get(orderId);
        
        if (order == null) {
            System.out.println("❌ Order not found!");
            return;
        }
        
        System.out.println("\n--- Marking order as ready (Observer Pattern in action) ---");
        order.markReady();
        System.out.println("--- Order marked as ready ---");
        
        System.out.println("\n✅ Order #" + orderId + " is ready for delivery!");
    }

    private static void viewAllOrders() {
        if (orders.isEmpty()) {
            System.out.println("❌ No orders exist.");
            return;
        }
        
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║          ALL ORDERS                  ║");
        System.out.println("╚══════════════════════════════════════╝\n");
        
        for (Order order : orders.values()) {
            System.out.printf("Order #%-4d | Items: %-2d | Total: %s\n", 
                order.id(), 
                order.getItemCount(),
                order.subtotal());
        }
    }

    private static void viewCatalog() {
        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║              PRODUCT CATALOG                         ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
        System.out.println("\nID          | Product Name              | Price");
        System.out.println("─────────────────────────────────────────────────────");
        System.out.println("COFFEE      | Coffee                    | $3.50");
        System.out.println("TEA         | Tea                       | $2.50");
        System.out.println("LATTE       | Latte                     | $4.50");
        System.out.println("CAPPUCCINO  | Cappuccino                | $4.00");
        System.out.println("ESPRESSO    | Espresso                  | $2.75");
        System.out.println("CROISSANT   | Croissant                 | $3.00");
        System.out.println("MUFFIN      | Blueberry Muffin          | $3.50");
        System.out.println("SANDWICH    | Ham & Cheese Sandwich     | $6.50");
        System.out.println("COOKIE      | Chocolate Cookie          | $2.00");
        System.out.println("CAKE        | Chocolate Cake Slice      | $5.00");
    }

    private static void demonstrateObserverPattern() {
        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║        OBSERVER PATTERN DEMONSTRATION                ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
        System.out.println("\nThis demo will create an order and show how the Observer");
        System.out.println("pattern notifies different parts of the system.\n");
        
        // Create a demo order
        long orderId = OrderIds.next();
        Order order = new Order(orderId);
        
        System.out.println("Step 1: Registering Observers");
        System.out.println("────────────────────────────────");
        order.register(kitchen);
        System.out.println("  ✓ Kitchen Display registered");
        order.register(delivery);
        System.out.println("  ✓ Delivery Desk registered");
        order.register(notifier);
        System.out.println("  ✓ Customer Notifier registered");
        
        orders.put(orderId, order);
        
        System.out.println("\nStep 2: Adding items (triggers 'itemAdded' event)");
        System.out.println("────────────────────────────────────────────────");
        Product coffee = catalog.findById("COFFEE").get();
        order.addItem(new LineItem(coffee, 2));
        
        System.out.println("\nStep 3: Adding another item");
        System.out.println("────────────────────────────");
        Product croissant = catalog.findById("CROISSANT").get();
        order.addItem(new LineItem(croissant, 1));
        
        System.out.println("\nStep 4: Processing payment (triggers 'paid' event)");
        System.out.println("──────────────────────────────────────────────────");
        order.pay(new CashPayment());
        
        System.out.println("\nStep 5: Marking order ready (triggers 'ready' event)");
        System.out.println("────────────────────────────────────────────────────");
        order.markReady();
        
        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║  OBSERVER PATTERN BENEFITS:                          ║");
        System.out.println("║  • Loose coupling between Order and Observers        ║");
        System.out.println("║  • Kitchen, Delivery, and Customer are notified      ║");
        System.out.println("║    automatically without Order knowing details       ║");
        System.out.println("║  • Easy to add/remove observers at runtime           ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
        
        System.out.println("\n✅ Demo order created with ID: " + orderId);
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid number.");
            }
        }
    }
}


