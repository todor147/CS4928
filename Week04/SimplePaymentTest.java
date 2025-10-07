public class SimplePaymentTest {
    public static void main(String[] args) {
        System.out.println("Testing payment strategies...");
        SimpleProduct product = new SimpleProduct("TEST001", "Test Product", Money.of(10.00));
        Order order = new Order(42L);
        order.addItem(new LineItem(product, 1));

        final boolean[] called = {false};
        PaymentStrategy fake = o -> called[0] = true;
        
        order.pay(fake);
        
        if (called[0]) {
            System.out.println("Payment strategy delegation test PASSED");
        } else {
            System.out.println("Payment strategy delegation test FAILED");
        }

        System.out.println("\nTesting actual payment strategies:");
        

        System.out.println("Cash payment:");
        order.pay(new CashPayment());

        System.out.println("Card payment:");
        order.pay(new CardPayment("1234567812341234"));

        System.out.println("Wallet payment:");
        order.pay(new WalletPayment("alice-wallet-01"));
        
        System.out.println("\nAll payment strategy tests completed!");
    }
}
