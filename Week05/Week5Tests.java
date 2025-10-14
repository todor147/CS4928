public final class Week5Tests {

    // Test 1: Single decorator changes price and name as expected
    public static void test_decorator_single_addon() {
        Product espresso = new SimpleProduct("P-ESP", "Espresso", Money.of(2.50));
        Product withShot = new ExtraShot(espresso);
        
        assert withShot.name().equals("Espresso + Extra Shot") : "Name should be 'Espresso + Extra Shot'";
        assert ((Priced) withShot).price().equals(Money.of(3.30)) : "Price should be $3.30";
        
        System.out.println("✓ test_decorator_single_addon passed");
    }

    // Test 2: Multiple decorators stack correctly
    public static void test_decorator_stacks() {
        Product espresso = new SimpleProduct("P-ESP", "Espresso", Money.of(2.50));
        Product decorated = new SizeLarge(new OatMilk(new ExtraShot(espresso)));
        
        assert decorated.name().equals("Espresso + Extra Shot + Oat Milk (Large)") : 
            "Name should be 'Espresso + Extra Shot + Oat Milk (Large)'";
        assert ((Priced) decorated).price().equals(Money.of(4.50)) : 
            "Price should be $4.50 (2.50 + 0.80 + 0.50 + 0.70)";
        
        System.out.println("✓ test_decorator_stacks passed");
    }

    // Test 3: Factory parses recipes correctly
    public static void test_factory_parses_recipe() {
        ProductFactory f = new ProductFactory();
        Product p = f.create("ESP+SHOT+OAT");
        
        assert p.name().contains("Espresso") : "Name should contain 'Espresso'";
        assert p.name().contains("Oat Milk") : "Name should contain 'Oat Milk'";
        
        System.out.println("✓ test_factory_parses_recipe passed");
    }

    // Test 4: Order uses decorated price
    public static void test_order_uses_decorated_price() {
        Product espresso = new SimpleProduct("P-ESP", "Espresso", Money.of(2.50));
        Product withShot = new ExtraShot(espresso); // 3.30
        
        Order o = new Order(1);
        o.addItem(new LineItem(withShot, 2));
        
        assert o.subtotal().equals(Money.of(6.60)) : "Subtotal should be $6.60 (3.30 * 2)";
        
        System.out.println("✓ test_order_uses_decorated_price passed");
    }

    // Test 5: Factory vs Manual - they should build the same drink
    public static void test_factory_vs_manual() {
        // Via factory
        ProductFactory factory = new ProductFactory();
        Product viaFactory = factory.create("ESP+SHOT+OAT+L");
        
        // Via manual wrapping
        Product viaManual = new SizeLarge(new OatMilk(new ExtraShot(
            new SimpleProduct("P-ESP", "Espresso", Money.of(2.50))
        )));
        
        // Names should be equal
        assert viaFactory.name().equals(viaManual.name()) : 
            "Factory and manual names should match";
        
        // Prices should be equal
        Money factoryPrice = ((Priced) viaFactory).price();
        Money manualPrice = ((Priced) viaManual).price();
        assert factoryPrice.equals(manualPrice) : 
            "Factory and manual prices should match";
        
        // Orders with each should have same totals
        Order orderFactory = new Order(1);
        orderFactory.addItem(new LineItem(viaFactory, 1));
        
        Order orderManual = new Order(2);
        orderManual.addItem(new LineItem(viaManual, 1));
        
        assert orderFactory.subtotal().equals(orderManual.subtotal()) : 
            "Subtotals should match";
        assert orderFactory.totalWithTax(10).equals(orderManual.totalWithTax(10)) : 
            "Totals with tax should match";
        
        System.out.println("✓ test_factory_vs_manual passed");
    }

    // Test 6: Different decoration orders yield same price
    public static void test_decoration_order_independence() {
        Product espresso1 = new SimpleProduct("P-ESP", "Espresso", Money.of(2.50));
        Product order1 = new OatMilk(new ExtraShot(espresso1));
        
        Product espresso2 = new SimpleProduct("P-ESP", "Espresso", Money.of(2.50));
        Product order2 = new ExtraShot(new OatMilk(espresso2));
        
        // Prices should be the same (commutative)
        Money price1 = ((Priced) order1).price();
        Money price2 = ((Priced) order2).price();
        assert price1.equals(price2) : "Decoration order should not affect price";
        
        System.out.println("✓ test_decoration_order_independence passed");
    }

    // Test 7: Test all base products
    public static void test_all_base_products() {
        ProductFactory f = new ProductFactory();
        
        Product esp = f.create("ESP");
        assert esp.name().equals("Espresso") : "ESP should create Espresso";
        assert ((Priced) esp).price().equals(Money.of(2.50)) : "Espresso should be $2.50";
        
        Product lat = f.create("LAT");
        assert lat.name().equals("Latte") : "LAT should create Latte";
        assert ((Priced) lat).price().equals(Money.of(3.20)) : "Latte should be $3.20";
        
        Product cap = f.create("CAP");
        assert cap.name().equals("Cappuccino") : "CAP should create Cappuccino";
        assert ((Priced) cap).price().equals(Money.of(3.00)) : "Cappuccino should be $3.00";
        
        System.out.println("✓ test_all_base_products passed");
    }

    // Test 8: Test all decorators individually
    public static void test_all_decorators() {
        Product base = new SimpleProduct("P-BASE", "Base", Money.of(1.00));
        
        Product withShot = new ExtraShot(base);
        assert ((Priced) withShot).price().equals(Money.of(1.80)) : "ExtraShot should add $0.80";
        
        Product withOat = new OatMilk(base);
        assert ((Priced) withOat).price().equals(Money.of(1.50)) : "OatMilk should add $0.50";
        
        Product withSyrup = new Syrup(base);
        assert ((Priced) withSyrup).price().equals(Money.of(1.40)) : "Syrup should add $0.40";
        
        Product withLarge = new SizeLarge(base);
        assert ((Priced) withLarge).price().equals(Money.of(1.70)) : "SizeLarge should add $0.70";
        
        System.out.println("✓ test_all_decorators passed");
    }

    public static void main(String[] args) {
        System.out.println("Running Week 5 Tests...\n");
        
        test_decorator_single_addon();
        test_decorator_stacks();
        test_factory_parses_recipe();
        test_order_uses_decorated_price();
        test_factory_vs_manual();
        test_decoration_order_independence();
        test_all_base_products();
        test_all_decorators();
        
        System.out.println("\n✓ All tests passed!");
    }
}

