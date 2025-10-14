import java.util.Scanner;

public final class Week5Demo {
    private static final Scanner scanner = new Scanner(System.in);
    private static final ProductFactory factory = new ProductFactory();

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║          CAFÉ POS - Week 5 Interactive Demo              ║");
        System.out.println("║         Decorator + Factory Pattern Demonstration         ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println();

        boolean running = true;
        while (running) {
            System.out.println("\n┌─ MAIN MENU ─────────────────────────────────────────────┐");
            System.out.println("│ 1. Create Custom Order (Interactive)                    │");
            System.out.println("│ 2. Create Order Using Recipe Codes                      │");
            System.out.println("│ 3. Run Sample Demo                                       │");
            System.out.println("│ 4. View Recipe Code Reference                            │");
            System.out.println("│ 5. Exit                                                  │");
            System.out.println("└──────────────────────────────────────────────────────────┘");
            System.out.print("Select option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> createCustomOrder();
                case "2" -> createOrderWithRecipes();
                case "3" -> runSampleDemo();
                case "4" -> showRecipeReference();
                case "5" -> {
                    System.out.println("\n✓ Thank you for using Café POS!");
                    running = false;
                }
                default -> System.out.println("\n✗ Invalid option. Please try again.");
            }
        }

        scanner.close();
    }

    private static void createCustomOrder() {
        Order order = new Order(OrderIds.next());
        boolean addingItems = true;

        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║              BUILD YOUR CUSTOM ORDER                      ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");

        while (addingItems) {
            // Step 1: Choose base product
            System.out.println("\n┌─ STEP 1: Choose Your Base Drink ───────────────────────┐");
            System.out.println("│ 1. Espresso        ($2.50)                              │");
            System.out.println("│ 2. Latte           ($3.20)                              │");
            System.out.println("│ 3. Cappuccino      ($3.00)                              │");
            System.out.println("└──────────────────────────────────────────────────────────┘");
            System.out.print("Select base drink (1-3): ");
            
            String baseChoice = scanner.nextLine().trim();
            String baseCode = switch (baseChoice) {
                case "1" -> "ESP";
                case "2" -> "LAT";
                case "3" -> "CAP";
                default -> {
                    System.out.println("✗ Invalid choice. Defaulting to Espresso.");
                    yield "ESP";
                }
            };

            // Step 2: Add decorators
            System.out.println("\n┌─ STEP 2: Add Optional Features (press Enter to skip) ──┐");
            StringBuilder recipe = new StringBuilder(baseCode);
            
            System.out.print("│ Add Extra Shot? (+$0.80) [y/N]: ");
            if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
                recipe.append("+SHOT");
                System.out.println("│   ✓ Extra Shot added");
            }
            
            System.out.print("│ Add Oat Milk? (+$0.50) [y/N]: ");
            if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
                recipe.append("+OAT");
                System.out.println("│   ✓ Oat Milk added");
            }
            
            System.out.print("│ Add Syrup? (+$0.40) [y/N]: ");
            if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
                recipe.append("+SYP");
                System.out.println("│   ✓ Syrup added");
            }
            
            System.out.print("│ Upgrade to Large? (+$0.70) [y/N]: ");
            if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
                recipe.append("+L");
                System.out.println("│   ✓ Large size selected");
            }
            System.out.println("└──────────────────────────────────────────────────────────┘");

            // Step 3: Set quantity
            System.out.println("\n┌─ STEP 3: Set Quantity ──────────────────────────────────┐");
            System.out.print("│ Quantity: ");
            int quantity = 1;
            try {
                String qtyInput = scanner.nextLine().trim();
                if (!qtyInput.isEmpty()) {
                    quantity = Integer.parseInt(qtyInput);
                    if (quantity <= 0) quantity = 1;
                }
            } catch (NumberFormatException e) {
                System.out.println("│ ✗ Invalid quantity. Using 1.");
                quantity = 1;
            }
            System.out.println("└──────────────────────────────────────────────────────────┘");

            // Create product and add to order
            Product product = factory.create(recipe.toString());
            order.addItem(new LineItem(product, quantity));

            Money itemPrice = (product instanceof Priced p) ? p.price() : product.basePrice();
            System.out.println("\n✓ Added to order: " + product.name());
            System.out.println("  Unit price: " + itemPrice + " × " + quantity + " = " + itemPrice.multiply(quantity));

            // Ask if user wants to add more items
            System.out.print("\nAdd another item? [y/N]: ");
            addingItems = scanner.nextLine().trim().equalsIgnoreCase("y");
        }

        // Display final order
        displayOrder(order);
    }

    private static void createOrderWithRecipes() {
        Order order = new Order(OrderIds.next());
        boolean addingItems = true;

        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║           ORDER USING RECIPE CODES                        ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println("\nRecipe format: BASE+ADDON1+ADDON2+...");
        System.out.println("Example: ESP+SHOT+OAT+L");

        while (addingItems) {
            System.out.print("\nEnter recipe code: ");
            String recipe = scanner.nextLine().trim();

            if (recipe.isEmpty()) {
                System.out.println("✗ Recipe cannot be empty.");
                continue;
            }

            try {
                Product product = factory.create(recipe);
                
                System.out.print("Quantity: ");
                int quantity = 1;
                try {
                    String qtyInput = scanner.nextLine().trim();
                    if (!qtyInput.isEmpty()) {
                        quantity = Integer.parseInt(qtyInput);
                        if (quantity <= 0) quantity = 1;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("✗ Invalid quantity. Using 1.");
                    quantity = 1;
                }

                order.addItem(new LineItem(product, quantity));
                Money itemPrice = (product instanceof Priced p) ? p.price() : product.basePrice();
                System.out.println("✓ Added: " + product.name() + " × " + quantity + " = " + itemPrice.multiply(quantity));

            } catch (IllegalArgumentException e) {
                System.out.println("✗ Error: " + e.getMessage());
                System.out.println("  Use option 4 to view recipe code reference.");
            }

            System.out.print("\nAdd another item? [y/N]: ");
            addingItems = scanner.nextLine().trim().equalsIgnoreCase("y");
        }

        if (order.getItemCount() > 0) {
            displayOrder(order);
        } else {
            System.out.println("\n✗ No items added to order.");
        }
    }

    private static void runSampleDemo() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                  SAMPLE DEMO ORDER                        ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");

        Product p1 = factory.create("ESP+SHOT+OAT");
        Product p2 = factory.create("LAT+L");

        Order order = new Order(OrderIds.next());
        order.addItem(new LineItem(p1, 1));
        order.addItem(new LineItem(p2, 2));

        displayOrder(order);
    }

    private static void showRecipeReference() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║              RECIPE CODE REFERENCE                        ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println("\n┌─ BASE PRODUCTS ─────────────────────────────────────────┐");
        System.out.println("│ ESP - Espresso        ($2.50)                            │");
        System.out.println("│ LAT - Latte           ($3.20)                            │");
        System.out.println("│ CAP - Cappuccino      ($3.00)                            │");
        System.out.println("└──────────────────────────────────────────────────────────┘");
        System.out.println("\n┌─ ADD-ONS (Decorators) ──────────────────────────────────┐");
        System.out.println("│ SHOT - Extra Shot     (+$0.80)                           │");
        System.out.println("│ OAT  - Oat Milk       (+$0.50)                           │");
        System.out.println("│ SYP  - Syrup          (+$0.40)                           │");
        System.out.println("│ L    - Large Size     (+$0.70)                           │");
        System.out.println("└──────────────────────────────────────────────────────────┘");
        System.out.println("\n┌─ EXAMPLE RECIPES ───────────────────────────────────────┐");
        System.out.println("│ ESP              → Espresso ($2.50)                      │");
        System.out.println("│ ESP+SHOT         → Espresso + Extra Shot ($3.30)         │");
        System.out.println("│ LAT+L            → Latte (Large) ($3.90)                 │");
        System.out.println("│ ESP+SHOT+OAT     → Espresso + Extra Shot + Oat Milk     │");
        System.out.println("│                     ($3.80)                               │");
        System.out.println("│ ESP+SHOT+OAT+L   → Espresso + Extra Shot + Oat Milk     │");
        System.out.println("│                     (Large) ($4.50)                       │");
        System.out.println("│ CAP+SYP+OAT      → Cappuccino + Syrup + Oat Milk        │");
        System.out.println("│                     ($3.90)                               │");
        System.out.println("└──────────────────────────────────────────────────────────┘");
        System.out.println("\nNote: Recipe codes are case-insensitive and can include spaces.");
    }

    private static void displayOrder(Order order) {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                    ORDER SUMMARY                          ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println("\nOrder #" + order.id());
        System.out.println("─────────────────────────────────────────────────────────────");

        for (LineItem li : order.items()) {
            String name = li.product().name();
            int qty = li.quantity();
            Money lineTotal = li.lineTotal();
            System.out.printf(" - %-45s x%-2d = %s%n", name, qty, lineTotal);
        }

        System.out.println("─────────────────────────────────────────────────────────────");
        System.out.println("Subtotal: " + order.subtotal());
        System.out.println("Tax (10%): " + order.taxAtPercent(10));
        System.out.println("═════════════════════════════════════════════════════════════");
        System.out.println("TOTAL: " + order.totalWithTax(10));
        System.out.println("═════════════════════════════════════════════════════════════");

        // Optional: Ask about payment
        System.out.print("\nProcess payment? [y/N]: ");
        if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
            processPayment(order);
        }
    }

    private static void processPayment(Order order) {
        System.out.println("\n┌─ PAYMENT OPTIONS ───────────────────────────────────────┐");
        System.out.println("│ 1. Cash                                                  │");
        System.out.println("│ 2. Card                                                  │");
        System.out.println("│ 3. Wallet                                                │");
        System.out.println("└──────────────────────────────────────────────────────────┘");
        System.out.print("Select payment method (1-3): ");

        String paymentChoice = scanner.nextLine().trim();
        PaymentStrategy strategy = switch (paymentChoice) {
            case "1" -> new CashPayment();
            case "2" -> {
                System.out.print("Enter card number: ");
                String cardNum = scanner.nextLine().trim();
                if (cardNum.length() < 4) cardNum = "1234567890123456";
                yield new CardPayment(cardNum);
            }
            case "3" -> {
                System.out.print("Enter wallet ID: ");
                String walletId = scanner.nextLine().trim();
                if (walletId.isEmpty()) walletId = "WALLET-001";
                yield new WalletPayment(walletId);
            }
            default -> {
                System.out.println("✗ Invalid choice. Using cash.");
                yield new CashPayment();
            }
        };

        order.pay(strategy);
        System.out.println("✓ Payment processed successfully!");
    }
}
