public class SimpleTestRunner {
    public static void main(String[] args) {
        System.out.println("Running basic tests...");
        
        // Test Money operations
        Money two = Money.of(2.00);
        Money three = Money.of(3.00);
        Money result = two.add(three);
        System.out.println("Money addition: " + result);
        
        // Test Product creation
        SimpleProduct product = new SimpleProduct("COFFEE001", "Espresso", Money.of(3.50));
        System.out.println("Product created: " + product);
        
        // Test LineItem
        LineItem item = new LineItem(product, 2);
        System.out.println("LineItem total: " + item.lineTotal());
        
        // Test Order
        Order order = new Order(1001L);
        order.addItem(item);
        System.out.println("Order subtotal: " + order.subtotal());
        System.out.println("Order total with 10% tax: " + order.totalWithTax(10));
        
        // Test Catalog
        Catalog catalog = new InMemoryCatalog();
        catalog.add(product);
        System.out.println("Product found in catalog: " + catalog.findById("COFFEE001").isPresent());
        
        System.out.println("All basic tests completed successfully!");
    }
}

