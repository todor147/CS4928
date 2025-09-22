public class SimpleTests {

    public static void main(String[] args) {
        System.out.println("Running Simple Tests\n");

        testMoney();
        testProduct();
        testLineItem();
        testOrder();
        testCatalog();
        testCompleteSystem();

        System.out.println("All Tests Completed");
    }

    private static void testMoney() {
        System.out.println("Testing Money class...");

        // Test addition
        Money two = Money.of(2.00);
        Money three = Money.of(3.00);
        Money result = two.add(three);
        assert result.equals(Money.of(5.00)) : "Money addition failed";
        System.out.println("Money addition: 2.00 + 3.00 = " + result);

        // Test multiplication
        Money price = Money.of(2.50);
        Money multiplied = price.multiply(3);
        assert multiplied.equals(Money.of(7.50)) : "Money multiplication failed";
        System.out.println("Money multiplication: 2.50 * 3 = " + multiplied);

        // Test zero
        Money zero = Money.zero();
        assert zero.equals(Money.of(0.00)) : "Money zero failed";
        System.out.println("Money zero: " + zero);

        // Test negative rejection
        try {
            Money.of(-1.0);
            System.out.println("✗ Money should reject negative values");
        } catch (IllegalArgumentException e) {
            System.out.println("Money correctly rejects negative values");
        }

        System.out.println();
    }

    private static void testProduct() {
        System.out.println("Testing Product class...");

        SimpleProduct product = new SimpleProduct("COFFEE001", "Espresso", Money.of(3.50));
        assert "COFFEE001".equals(product.id()) : "Product ID failed";
        assert "Espresso".equals(product.name()) : "Product name failed";
        assert product.basePrice().equals(Money.of(3.50)) : "Product price failed";
        System.out.println("Product creation: " + product);

        // Test validation
        try {
            new SimpleProduct(null, "Test", Money.of(1.00));
            System.out.println("✗ Product should reject null ID");
        } catch (IllegalArgumentException e) {
            System.out.println("Product correctly rejects null ID");
        }

        System.out.println();
    }

    private static void testLineItem() {
        System.out.println("Testing LineItem class...");

        SimpleProduct product = new SimpleProduct("COFFEE001", "Espresso", Money.of(3.50));
        LineItem item = new LineItem(product, 2);
        assert item.product().equals(product) : "LineItem product failed";
        assert item.quantity() == 2 : "LineItem quantity failed";
        assert item.lineTotal().equals(Money.of(7.00)) : "LineItem total failed";
        System.out.println("LineItem creation: " + item + " = " + item.lineTotal());

        // Test validation
        try {
            new LineItem(null, 1);
            System.out.println("✗ LineItem should reject null product");
        } catch (IllegalArgumentException e) {
            System.out.println("LineItem correctly rejects null product");
        }

        System.out.println();
    }

    private static void testOrder() {
        System.out.println("Testing Order class...");

        // Test the exact scenario from requirements
        var p1 = new SimpleProduct("A", "A", Money.of(2.50));
        var p2 = new SimpleProduct("B", "B", Money.of(3.50));
        var o = new Order(1);
        o.addItem(new LineItem(p1, 2));
        o.addItem(new LineItem(p2, 1));

        assert o.subtotal().equals(Money.of(8.50)) : "Order subtotal failed";
        assert o.taxAtPercent(10).equals(Money.of(0.85)) : "Order tax failed";
        assert o.totalWithTax(10).equals(Money.of(9.35)) : "Order total failed";

        System.out.println("Order subtotal: " + o.subtotal());
        System.out.println("Order tax (10%): " + o.taxAtPercent(10));
        System.out.println("Order total: " + o.totalWithTax(10));

        // Test empty order
        Order emptyOrder = new Order(999);
        assert emptyOrder.subtotal().equals(Money.zero()) : "Empty order subtotal failed";
        assert emptyOrder.taxAtPercent(10).equals(Money.zero()) : "Empty order tax failed";
        System.out.println("Empty order handling works");

        System.out.println();
    }

    private static void testCatalog() {
        System.out.println("Testing Catalog class...");

        Catalog catalog = new InMemoryCatalog();
        SimpleProduct product = new SimpleProduct("COFFEE001", "Espresso", Money.of(3.50));

        catalog.add(product);
        assert catalog.findById("COFFEE001").isPresent() : "Catalog find failed";
        assert catalog.findById("COFFEE001").get().equals(product) : "Catalog product mismatch";
        System.out.println("Catalog add and find: " + catalog.findById("COFFEE001").get());

        assert !catalog.findById("NONEXISTENT").isPresent() : "Catalog should not find non-existent";
        System.out.println("Catalog correctly handles non-existent items");

        System.out.println();
    }

    private static void testCompleteSystem() {
        System.out.println("Testing Complete System (Demo Scenario)...");

        Catalog catalog = new InMemoryCatalog();
        catalog.add(new SimpleProduct("P-ESP", "Espresso", Money.of(2.50)));
        catalog.add(new SimpleProduct("P-CCK", "Chocolate Cookie", Money.of(3.50)));

        Order order = new Order(1001L);
        order.addItem(new LineItem(catalog.findById("P-ESP").orElseThrow(), 2));
        order.addItem(new LineItem(catalog.findById("P-CCK").orElseThrow(), 1));

        int taxPct = 10;

        // Verify all calculations match expected demo output
        assert order.getId() == 1001L : "Order ID failed";
        assert order.getItemCount() == 2 : "Order item count failed";
        assert order.subtotal().equals(Money.of(8.50)) : "Demo subtotal failed";
        assert order.taxAtPercent(taxPct).equals(Money.of(0.85)) : "Demo tax failed";
        assert order.totalWithTax(taxPct).equals(Money.of(9.35)) : "Demo total failed";

        System.out.println("Order #" + order.getId());
        System.out.println("Items: " + order.getItemCount());
        System.out.println("Subtotal: " + order.subtotal());
        System.out.println("Tax (" + taxPct + "%): " + order.taxAtPercent(taxPct));
        System.out.println("Total: " + order.totalWithTax(taxPct));

        System.out.println();
    }
}
