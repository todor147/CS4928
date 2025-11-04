import java.util.Scanner;

public final class InteractiveWeek4Demo {
    private static final Scanner scanner = new Scanner(System.in);
    private static Catalog catalog;
    private static Order currentOrder;

    public static void main(String[] args) {
        initializeCatalog();
        setupObservers();

        boolean running = true;
        while (running) {
            displayMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    createNewOrder();
                    break;
                case 2:
                    addProductToOrder();
                    break;
                case 3:
                    viewCurrentOrder();
                    break;
                case 4:
                    processPayment();
                    break;
                case 5:
                    markOrderReady();
                    break;
                case 6:
                    viewCatalog();
                    break;
                case 7:
                    addProductToCatalog();
                    break;
                case 0:
                    running = false;
                    System.out.println("Thank you for using the Cafe POS system!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }

        scanner.close();
    }

    private static void initializeCatalog() {
        catalog = new InMemoryCatalog();

        // Add some default products
        catalog.add(new SimpleProduct("ESP", "Espresso", Money.of(2.50)));
        catalog.add(new SimpleProduct("LAT", "Latte", Money.of(3.50)));
        catalog.add(new SimpleProduct("CAP", "Cappuccino", Money.of(3.25)));
        catalog.add(new SimpleProduct("AME", "Americano", Money.of(2.75)));
        catalog.add(new SimpleProduct("MAC", "Macchiato", Money.of(3.75)));
        catalog.add(new SimpleProduct("TEA", "Tea", Money.of(2.00)));
        catalog.add(new SimpleProduct("COO", "Cookie", Money.of(1.50)));
        catalog.add(new SimpleProduct("MUF", "Muffin", Money.of(2.25)));

        System.out.println("Catalog initialized with default products.");
    }

    private static void setupObservers() {
        System.out.println("Setting up observers: Kitchen Display, Delivery Desk, Customer Notifier");
    }

    private static void displayMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("CAFE POS SYSTEM - MAIN MENU");
        System.out.println("=".repeat(50));
        System.out.println("1. Create New Order");
        System.out.println("2. Add Product to Current Order");
        System.out.println("3. View Current Order");
        System.out.println("4. Process Payment");
        System.out.println("5. Mark Order Ready");
        System.out.println("6. View Catalog");
        System.out.println("7. Add Product to Catalog");
        System.out.println("0. Exit");
        System.out.println("=".repeat(50));
    }

    private static void createNewOrder() {
        currentOrder = new Order(OrderIds.next());

        // Register observers
        currentOrder.register(new KitchenDisplay());
        currentOrder.register(new DeliveryDesk());
        currentOrder.register(new CustomerNotifier());

        System.out.println("✓ New order created with ID: " + currentOrder.id());
        System.out.println("✓ Observers registered: Kitchen Display, Delivery Desk, Customer Notifier");
    }

    private static void addProductToOrder() {
        if (currentOrder == null) {
            System.out.println("❌ No active order. Please create a new order first.");
            return;
        }

        viewCatalog();
        System.out.println();

        String productId = getStringInput("Enter product ID: ").toUpperCase();
        Product product = catalog.findById(productId).orElse(null);

        if (product == null) {
            System.out.println("❌ Product not found with ID: " + productId);
            return;
        }

        int quantity = getIntInput("Enter quantity: ");
        if (quantity <= 0) {
            System.out.println("❌ Quantity must be positive.");
            return;
        }

        try {
            currentOrder.addItem(new LineItem(product, quantity));
            System.out.println("✓ Added " + quantity + "x " + product.name() + " to order");
        } catch (Exception e) {
            System.out.println("❌ Error adding item: " + e.getMessage());
        }
    }

    private static void viewCurrentOrder() {
        if (currentOrder == null) {
            System.out.println("❌ No active order.");
            return;
        }

        System.out.println("\n" + "=".repeat(50));
        System.out.println("CURRENT ORDER #" + currentOrder.id());
        System.out.println("=".repeat(50));

        if (currentOrder.getItems().isEmpty()) {
            System.out.println("Order is empty.");
        } else {
            for (LineItem item : currentOrder.getItems()) {
                System.out.printf("%-30s %2d x $%.2f = $%.2f%n",
                        item.product().name(),
                        item.quantity(),
                        item.product().basePrice().getAmount().doubleValue(),
                        item.lineTotal().getAmount().doubleValue());
            }

            System.out.println("-".repeat(50));
            System.out.printf("Subtotal: $%.2f%n", currentOrder.subtotal().getAmount().doubleValue());
            System.out.printf("Tax (10%%): $%.2f%n", currentOrder.taxAtPercent(10).getAmount().doubleValue());
            System.out.printf("Total: $%.2f%n", currentOrder.totalWithTax(10).getAmount().doubleValue());
        }
        System.out.println("=".repeat(50));
    }

    private static void processPayment() {
        if (currentOrder == null) {
            System.out.println("❌ No active order.");
            return;
        }

        if (currentOrder.getItems().isEmpty()) {
            System.out.println("❌ Cannot process payment for empty order.");
            return;
        }

        System.out.println("\nPayment Options:");
        System.out.println("1. Cash Payment");
        System.out.println("2. Card Payment");

        int paymentChoice = getIntInput("Select payment method: ");

        try {
            switch (paymentChoice) {
                case 1:
                    currentOrder.pay(new CashPayment());
                    break;
                case 2:
                    String cardNumber = getStringInput("Enter card number (at least 4 digits): ");
                    currentOrder.pay(new CardPayment(cardNumber));
                    break;
                default:
                    System.out.println("❌ Invalid payment method.");
                    return;
            }
        } catch (Exception e) {
            System.out.println("❌ Payment error: " + e.getMessage());
        }
    }

    private static void markOrderReady() {
        if (currentOrder == null) {
            System.out.println("❌ No active order.");
            return;
        }

        if (currentOrder.getItems().isEmpty()) {
            System.out.println("❌ Cannot mark empty order as ready.");
            return;
        }

        try {
            currentOrder.markReady();
            System.out.println("✓ Order #" + currentOrder.id() + " marked as ready");
        } catch (Exception e) {
            System.out.println("❌ Error marking order ready: " + e.getMessage());
        }
    }

    private static void viewCatalog() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("PRODUCT CATALOG");
        System.out.println("=".repeat(50));
        System.out.printf("%-8s %-20s %-10s%n", "ID", "Name", "Price");
        System.out.println("-".repeat(50));

        // Since we don't have a method to get all products, we'll show the ones we know
        String[] productIds = { "ESP", "LAT", "CAP", "AME", "MAC", "TEA", "COO", "MUF" };
        for (String id : productIds) {
            catalog.findById(id).ifPresent(product -> System.out.printf("%-8s %-20s $%.2f%n",
                    product.id(),
                    product.name(),
                    product.basePrice().getAmount().doubleValue()));
        }
        System.out.println("=".repeat(50));
    }

    private static void addProductToCatalog() {
        System.out.println("\nAdd New Product to Catalog:");

        String id = getStringInput("Enter product ID: ").toUpperCase();
        String name = getStringInput("Enter product name: ");
        double price = getDoubleInput("Enter product price: ");

        if (id.isEmpty() || name.isEmpty() || price < 0) {
            System.out.println("❌ Invalid product information.");
            return;
        }

        try {
            Product newProduct = new SimpleProduct(id, name, Money.of(price));
            catalog.add(newProduct);
            System.out.println("✓ Product added to catalog: " + name + " ($" + price + ")");
        } catch (Exception e) {
            System.out.println("❌ Error adding product: " + e.getMessage());
        }
    }

    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid number.");
            }
        }
    }

    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid number.");
            }
        }
    }
}
