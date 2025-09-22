import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SimpleTests {

    @Test
    void money_addition() {
        Money two = Money.of(2.00);
        Money three = Money.of(3.00);
        Money result = two.add(three);
        assertEquals(Money.of(5.00), result);
    }

    @Test
    void money_multiplication() {
        Money price = Money.of(2.50);
        Money result = price.multiply(3);
        assertEquals(Money.of(7.50), result);
    }

    @Test
    void money_zero() {
        Money zero = Money.zero();
        assertEquals(Money.of(0.00), zero);
    }

    @Test
    void money_negative_rejected() {
        assertThrows(IllegalArgumentException.class, () -> Money.of(-1.0));
    }

    @Test
    void money_equals() {
        Money money1 = Money.of(5.00);
        Money money2 = Money.of(5.00);
        assertEquals(money1, money2);
    }

    @Test
    void money_compareTo() {
        Money small = Money.of(3.00);
        Money large = Money.of(5.00);
        assertTrue(small.compareTo(large) < 0);
        assertTrue(large.compareTo(small) > 0);
        assertEquals(0, small.compareTo(Money.of(3.00)));
    }

    @Test
    void simpleProduct_creation() {
        SimpleProduct product = new SimpleProduct("COFFEE001", "Espresso", Money.of(3.50));
        assertEquals("COFFEE001", product.id());
        assertEquals("Espresso", product.name());
        assertEquals(Money.of(3.50), product.basePrice());
    }

    @Test
    void simpleProduct_nullId_rejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new SimpleProduct(null, "Test", Money.of(1.00)));
    }

    @Test
    void simpleProduct_emptyId_rejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new SimpleProduct("", "Test", Money.of(1.00)));
    }

    @Test
    void simpleProduct_nullName_rejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new SimpleProduct("TEST001", null, Money.of(1.00)));
    }

    @Test
    void simpleProduct_nullPrice_rejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new SimpleProduct("TEST001", "Test", null));
    }

    @Test
    void simpleProduct_equals() {
        SimpleProduct p1 = new SimpleProduct("TEST001", "Test", Money.of(1.00));
        SimpleProduct p2 = new SimpleProduct("TEST001", "Test", Money.of(1.00));
        assertEquals(p1, p2);
    }

    @Test
    void lineItem_creation() {
        SimpleProduct product = new SimpleProduct("COFFEE001", "Espresso", Money.of(3.50));
        LineItem item = new LineItem(product, 2);
        assertEquals(product, item.product());
        assertEquals(2, item.quantity());
        assertEquals(Money.of(7.00), item.lineTotal());
    }

    @Test
    void lineItem_nullProduct_rejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new LineItem(null, 1));
    }

    @Test
    void lineItem_zeroQuantity_rejected() {
        SimpleProduct product = new SimpleProduct("TEST001", "Test", Money.of(1.00));
        assertThrows(IllegalArgumentException.class,
                () -> new LineItem(product, 0));
    }

    @Test
    void lineItem_negativeQuantity_rejected() {
        SimpleProduct product = new SimpleProduct("TEST001", "Test", Money.of(1.00));
        assertThrows(IllegalArgumentException.class,
                () -> new LineItem(product, -1));
    }

    @Test
    void lineItem_lineTotal() {
        SimpleProduct product = new SimpleProduct("TEST001", "Test", Money.of(2.50));
        LineItem item = new LineItem(product, 3);
        assertEquals(Money.of(7.50), item.lineTotal());
    }

    @Test
    void order_totals() {
        var p1 = new SimpleProduct("A", "A", Money.of(2.50));
        var p2 = new SimpleProduct("B", "B", Money.of(3.50));
        var o = new Order(1);
        o.addItem(new LineItem(p1, 2));
        o.addItem(new LineItem(p2, 1));
        assertEquals(Money.of(8.50), o.subtotal());
        assertEquals(Money.of(0.85), o.taxAtPercent(10));
        assertEquals(Money.of(9.35), o.totalWithTax(10));
    }

    @Test
    void order_empty_subtotal() {
        Order order = new Order(1);
        assertEquals(Money.zero(), order.subtotal());
    }

    @Test
    void order_empty_tax() {
        Order order = new Order(1);
        assertEquals(Money.zero(), order.taxAtPercent(10));
    }

    @Test
    void order_empty_total() {
        Order order = new Order(1);
        assertEquals(Money.zero(), order.totalWithTax(10));
    }

    @Test
    void order_addItem() {
        Order order = new Order(1);
        SimpleProduct product = new SimpleProduct("TEST001", "Test", Money.of(1.00));
        LineItem item = new LineItem(product, 2);
        order.addItem(item);
        assertEquals(1, order.getItemCount());
        assertEquals(Money.of(2.00), order.subtotal());
    }

    @Test
    void order_nullItem_rejected() {
        Order order = new Order(1);
        assertThrows(IllegalArgumentException.class,
                () -> order.addItem(null));
    }

    @Test
    void order_negativeTax_rejected() {
        Order order = new Order(1);
        assertThrows(IllegalArgumentException.class,
                () -> order.taxAtPercent(-5));
    }

    @Test
    void order_taxCalculations() {
        Order order = new Order(1);
        SimpleProduct product = new SimpleProduct("TEST001", "Test", Money.of(10.00));
        order.addItem(new LineItem(product, 1));

        assertEquals(Money.of(1.00), order.taxAtPercent(10));
        assertEquals(Money.of(2.00), order.taxAtPercent(20));
        assertEquals(Money.of(0.50), order.taxAtPercent(5));
    }

    @Test
    void order_totalWithTax() {
        Order order = new Order(1);
        SimpleProduct product = new SimpleProduct("TEST001", "Test", Money.of(10.00));
        order.addItem(new LineItem(product, 1));

        assertEquals(Money.of(11.00), order.totalWithTax(10));
        assertEquals(Money.of(12.00), order.totalWithTax(20));
        assertEquals(Money.of(10.50), order.totalWithTax(5));
    }

    @Test
    void catalog_addAndFind() {
        Catalog catalog = new InMemoryCatalog();
        SimpleProduct product = new SimpleProduct("COFFEE001", "Espresso", Money.of(3.50));

        catalog.add(product);
        assertTrue(catalog.findById("COFFEE001").isPresent());
        assertEquals(product, catalog.findById("COFFEE001").get());
    }

    @Test
    void catalog_findNonExistent() {
        Catalog catalog = new InMemoryCatalog();
        assertFalse(catalog.findById("NONEXISTENT").isPresent());
    }

    @Test
    void catalog_addNull_rejected() {
        Catalog catalog = new InMemoryCatalog();
        assertThrows(IllegalArgumentException.class,
                () -> catalog.add(null));
    }

    @Test
    void catalog_multipleProducts() {
        Catalog catalog = new InMemoryCatalog();
        SimpleProduct coffee = new SimpleProduct("COFFEE001", "Espresso", Money.of(3.50));
        SimpleProduct latte = new SimpleProduct("LATTE001", "Latte", Money.of(4.25));

        catalog.add(coffee);
        catalog.add(latte);

        assertTrue(catalog.findById("COFFEE001").isPresent());
        assertTrue(catalog.findById("LATTE001").isPresent());
        assertEquals(coffee, catalog.findById("COFFEE001").get());
        assertEquals(latte, catalog.findById("LATTE001").get());
    }

    @Test
    void completeSystemTest() {
        // Test the complete system as shown in the demo
        Catalog catalog = new InMemoryCatalog();
        catalog.add(new SimpleProduct("P-ESP", "Espresso", Money.of(2.50)));
        catalog.add(new SimpleProduct("P-CCK", "Chocolate Cookie", Money.of(3.50)));

        Order order = new Order(1001L);
        order.addItem(new LineItem(catalog.findById("P-ESP").orElseThrow(), 2));
        order.addItem(new LineItem(catalog.findById("P-CCK").orElseThrow(), 1));

        int taxPct = 10;

        // Verify all calculations match expected demo output
        assertEquals(1001L, order.getId());
        assertEquals(2, order.getItemCount());
        assertEquals(Money.of(8.50), order.subtotal());
        assertEquals(Money.of(0.85), order.taxAtPercent(taxPct));
        assertEquals(Money.of(9.35), order.totalWithTax(taxPct));
    }

    @Test
    void moneyPrecisionTest() {
        // Test that Money handles precision correctly
        Money price1 = Money.of(0.01);
        Money price2 = Money.of(0.02);
        Money sum = price1.add(price2);
        assertEquals(Money.of(0.03), sum);

        // Test multiplication with decimals
        Money result = Money.of(2.50).multiply(3);
        assertEquals(Money.of(7.50), result);
    }

    @Test
    void orderComplexScenario() {
        // Test a more complex order scenario
        Order order = new Order(999L);

        SimpleProduct coffee = new SimpleProduct("COFFEE", "Coffee", Money.of(2.50));
        SimpleProduct cookie = new SimpleProduct("COOKIE", "Cookie", Money.of(1.25));
        SimpleProduct sandwich = new SimpleProduct("SANDWICH", "Sandwich", Money.of(5.99));

        order.addItem(new LineItem(coffee, 3)); // 3 × $2.50 = $7.50
        order.addItem(new LineItem(cookie, 2)); // 2 × $1.25 = $2.50
        order.addItem(new LineItem(sandwich, 1)); // 1 × $5.99 = $5.99

        Money expectedSubtotal = Money.of(15.99); // $7.50 + $2.50 + $5.99
        Money expectedTax = Money.of(1.60); // 10% of $15.99 = $1.599 → $1.60
        Money expectedTotal = Money.of(17.59); // $15.99 + $1.60

        assertEquals(expectedSubtotal, order.subtotal());
        assertEquals(expectedTax, order.taxAtPercent(10));
        assertEquals(expectedTotal, order.totalWithTax(10));
    }
}
