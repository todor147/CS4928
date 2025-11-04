import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class Week3JUnitTest {

    private Order testOrder;
    private SimpleProduct testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new SimpleProduct("TEST", "Test Product", Money.of(10.00));
        testOrder = new Order(1);
        testOrder.addItem(new LineItem(testProduct, 1));
    }

    // Payment Strategy Tests
    @Test
    void testPaymentStrategyDelegation() {
        final boolean[] called = { false };
        PaymentStrategy fake = o -> called[0] = true;
        testOrder.pay(fake);
        assertTrue(called[0], "Payment strategy should be called");
    }

    @Test
    void testCashPayment() {
        assertDoesNotThrow(() -> testOrder.pay(new CashPayment()));
    }

    @Test
    void testCardPayment() {
        assertDoesNotThrow(() -> testOrder.pay(new CardPayment("1234567812341234")));
    }

    @Test
    void testWalletPayment() {
        assertDoesNotThrow(() -> testOrder.pay(new WalletPayment("test-wallet-01")));
    }

    @Test
    void testCardPaymentValidationNull() {
        assertThrows(IllegalArgumentException.class, () -> new CardPayment(null));
    }

    @Test
    void testCardPaymentValidationShort() {
        assertThrows(IllegalArgumentException.class, () -> new CardPayment("123"));
    }

    @Test
    void testCardPaymentValidationEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new CardPayment(""));
    }

    @Test
    void testWalletPaymentValidationNull() {
        assertThrows(IllegalArgumentException.class, () -> new WalletPayment(null));
    }

    @Test
    void testWalletPaymentValidationEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new WalletPayment(""));
    }

    @Test
    void testOrderPayValidation() {
        assertThrows(IllegalArgumentException.class, () -> testOrder.pay(null));
    }

    // Order Tests
    @Test
    void testOrderCreation() {
        Order order = new Order(42L);
        assertEquals(42L, order.id());
        assertEquals(0, order.getItemCount());
        assertEquals(Money.zero(), order.subtotal());
    }

    @Test
    void testOrderAddItem() {
        Order order = new Order(1);
        SimpleProduct product = new SimpleProduct("P1", "Product 1", Money.of(10.50));
        LineItem item = new LineItem(product, 2);

        order.addItem(item);
        assertEquals(1, order.getItemCount());
    }

    @Test
    void testOrderAddItemNull() {
        Order order = new Order(1);
        assertThrows(IllegalArgumentException.class, () -> order.addItem(null));
    }

    @Test
    void testOrderCalculations() {
        Order order = new Order(1);
        SimpleProduct product1 = new SimpleProduct("P1", "Product 1", Money.of(10.00));
        SimpleProduct product2 = new SimpleProduct("P2", "Product 2", Money.of(5.50));

        order.addItem(new LineItem(product1, 2)); // 2 * $10.00 = $20.00
        order.addItem(new LineItem(product2, 1)); // 1 * $5.50 = $5.50

        Money expectedSubtotal = Money.of(25.50);
        assertEquals(expectedSubtotal, order.subtotal());

        Money expectedTax = Money.of(2.55); // 10% of $25.50
        assertEquals(expectedTax, order.taxAtPercent(10));

        Money expectedTotal = Money.of(28.05); // $25.50 + $2.55
        assertEquals(expectedTotal, order.totalWithTax(10));
    }

    @Test
    void testOrderTaxZero() {
        Order order = new Order(1);
        SimpleProduct product = new SimpleProduct("P1", "Product 1", Money.of(10.00));
        order.addItem(new LineItem(product, 1));

        assertEquals(Money.zero(), order.taxAtPercent(0));
    }

    @Test
    void testOrderTaxNegative() {
        Order order = new Order(1);
        SimpleProduct product = new SimpleProduct("P1", "Product 1", Money.of(10.00));
        order.addItem(new LineItem(product, 1));
        assertThrows(IllegalArgumentException.class, () -> order.taxAtPercent(-5));
    }

    // Money Tests
    @Test
    void testMoneyCreation() {
        Money money = Money.of(10.50);
        assertEquals(10.50, money.getAmount().doubleValue(), 0.01);

        Money zero = Money.zero();
        assertEquals(0.0, zero.getAmount().doubleValue(), 0.01);
    }

    @Test
    void testMoneyCreationNegative() {
        assertThrows(IllegalArgumentException.class, () -> Money.of(-5.0));
    }

    @Test
    void testMoneyOperations() {
        Money money1 = Money.of(10.50);
        Money money2 = Money.of(5.25);

        Money sum = money1.add(money2);
        assertEquals(Money.of(15.75), sum);

        Money product = money1.multiply(3);
        assertEquals(Money.of(31.50), product);
    }

    @Test
    void testMoneyAdditionNull() {
        Money money = Money.of(10.0);
        assertThrows(IllegalArgumentException.class, () -> money.add(null));
    }

    @Test
    void testMoneyMultiplicationNegative() {
        Money money = Money.of(10.0);
        assertThrows(IllegalArgumentException.class, () -> money.multiply(-1));
    }

    @Test
    void testMoneyEquality() {
        Money money1 = Money.of(10.50);
        Money money2 = Money.of(10.50);
        Money money3 = Money.of(10.51);

        assertEquals(money1, money2);
        assertNotEquals(money1, money3);
    }

    @Test
    void testMoneyComparison() {
        Money money1 = Money.of(10.0);
        Money money2 = Money.of(15.0);
        Money money3 = Money.of(10.0);

        assertTrue(money1.compareTo(money2) < 0);
        assertTrue(money2.compareTo(money1) > 0);
        assertEquals(0, money1.compareTo(money3));
    }

    @Test
    void testMoneyComparisonNull() {
        Money money = Money.of(10.0);
        assertThrows(IllegalArgumentException.class, () -> money.compareTo(null));
    }

    @Test
    void testMoneyPrecision() {
        Money money = Money.of(10.555);
        assertEquals(10.56, money.getAmount().doubleValue(), 0.01);
    }

    // LineItem Tests
    @Test
    void testLineItemCreation() {
        SimpleProduct product = new SimpleProduct("TEST", "Test Product", Money.of(10.50));
        LineItem item = new LineItem(product, 2);

        assertEquals(product, item.product());
        assertEquals(2, item.quantity());
        assertEquals(Money.of(21.00), item.lineTotal());
    }

    @Test
    void testLineItemNullProduct() {
        assertThrows(IllegalArgumentException.class, () -> new LineItem(null, 1));
    }

    @Test
    void testLineItemZeroQuantity() {
        SimpleProduct product = new SimpleProduct("TEST", "Test Product", Money.of(10.50));
        assertThrows(IllegalArgumentException.class, () -> new LineItem(product, 0));
    }

    @Test
    void testLineItemNegativeQuantity() {
        SimpleProduct product = new SimpleProduct("TEST", "Test Product", Money.of(10.50));
        assertThrows(IllegalArgumentException.class, () -> new LineItem(product, -1));
    }

    @Test
    void testLineItemEquality() {
        SimpleProduct product = new SimpleProduct("TEST", "Test Product", Money.of(10.50));
        LineItem item1 = new LineItem(product, 2);
        LineItem item2 = new LineItem(product, 2);
        LineItem item3 = new LineItem(product, 3);

        assertEquals(item1, item2);
        assertNotEquals(item1, item3);
    }

    // SimpleProduct Tests
    @Test
    void testSimpleProductCreation() {
        Money price = Money.of(10.50);
        SimpleProduct product = new SimpleProduct("TEST", "Test Product", price);

        assertEquals("TEST", product.id());
        assertEquals("Test Product", product.name());
        assertEquals(price, product.basePrice());
    }

    @Test
    void testSimpleProductNullId() {
        Money price = Money.of(10.50);
        assertThrows(IllegalArgumentException.class, () -> new SimpleProduct(null, "Test Product", price));
    }

    @Test
    void testSimpleProductEmptyId() {
        Money price = Money.of(10.50);
        assertThrows(IllegalArgumentException.class, () -> new SimpleProduct("", "Test Product", price));
    }

    @Test
    void testSimpleProductWhitespaceId() {
        Money price = Money.of(10.50);
        assertThrows(IllegalArgumentException.class, () -> new SimpleProduct("   ", "Test Product", price));
    }

    @Test
    void testSimpleProductNullName() {
        Money price = Money.of(10.50);
        assertThrows(IllegalArgumentException.class, () -> new SimpleProduct("TEST", null, price));
    }

    @Test
    void testSimpleProductEmptyName() {
        Money price = Money.of(10.50);
        assertThrows(IllegalArgumentException.class, () -> new SimpleProduct("TEST", "", price));
    }

    @Test
    void testSimpleProductWhitespaceName() {
        Money price = Money.of(10.50);
        assertThrows(IllegalArgumentException.class, () -> new SimpleProduct("TEST", "   ", price));
    }

    @Test
    void testSimpleProductNullBasePrice() {
        assertThrows(IllegalArgumentException.class, () -> new SimpleProduct("TEST", "Test Product", null));
    }

    @Test
    void testSimpleProductZeroBasePrice() {
        // Zero price should be allowed
        assertDoesNotThrow(() -> new SimpleProduct("TEST", "Test Product", Money.zero()));
    }

    @Test
    void testSimpleProductEquality() {
        Money price = Money.of(10.50);
        SimpleProduct product1 = new SimpleProduct("TEST", "Test Product", price);
        SimpleProduct product2 = new SimpleProduct("TEST", "Test Product", price);
        SimpleProduct product3 = new SimpleProduct("TEST2", "Test Product", price);

        assertEquals(product1, product2);
        assertNotEquals(product1, product3);
    }

    // Integration Tests
    @Test
    void testCompleteOrderFlowCash() {
        Catalog catalog = new InMemoryCatalog();
        catalog.add(new SimpleProduct("P-ESP", "Espresso", Money.of(2.50)));
        catalog.add(new SimpleProduct("P-CCK", "Chocolate Cookie", Money.of(3.50)));

        Order order = new Order(1002L);
        order.addItem(new LineItem(catalog.findById("P-ESP").orElseThrow(), 2));
        order.addItem(new LineItem(catalog.findById("P-CCK").orElseThrow(), 1));

        // Total should be: (2 * $2.50) + (1 * $3.50) = $8.50
        // With 10% tax: $8.50 + $0.85 = $9.35
        Money expectedTotal = Money.of(9.35);
        assertEquals(expectedTotal, order.totalWithTax(10));

        assertDoesNotThrow(() -> order.pay(new CashPayment()));
    }

    @Test
    void testCompleteOrderFlowCard() {
        Catalog catalog = new InMemoryCatalog();
        catalog.add(new SimpleProduct("P-ESP", "Espresso", Money.of(2.50)));

        Order order = new Order(1003L);
        order.addItem(new LineItem(catalog.findById("P-ESP").orElseThrow(), 1));

        assertDoesNotThrow(() -> order.pay(new CardPayment("1234567812341234")));
    }

    @Test
    void testCompleteOrderFlowWallet() {
        Catalog catalog = new InMemoryCatalog();
        catalog.add(new SimpleProduct("P-CCK", "Chocolate Cookie", Money.of(3.50)));

        Order order = new Order(1004L);
        order.addItem(new LineItem(catalog.findById("P-CCK").orElseThrow(), 2));

        assertDoesNotThrow(() -> order.pay(new WalletPayment("alice-wallet-01")));
    }
}