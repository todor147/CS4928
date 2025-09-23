public class PaymentStrategyTest {
    
    public static void main(String[] args) {
        System.out.println("Running PaymentStrategy tests...");
        
        boolean allTestsPassed = true;
        
        // Test 1: Payment strategy delegation
        allTestsPassed &= testPaymentStrategyCalled();
        
        // Test 2: Cash payment works
        allTestsPassed &= testCashPaymentWorks();
        
        // Test 3: Card payment works
        allTestsPassed &= testCardPaymentWorks();
        
        // Test 4: Wallet payment works
        allTestsPassed &= testWalletPaymentWorks();
        
        // Test 5: Card payment validation
        allTestsPassed &= testCardPaymentValidation();
        
        // Test 6: Wallet payment validation
        allTestsPassed &= testWalletPaymentValidation();
        
        // Test 7: Order pay validation
        allTestsPassed &= testOrderPayValidation();
        
        if (allTestsPassed) {
            System.out.println("✓ All tests PASSED!");
        } else {
            System.out.println("✗ Some tests FAILED!");
        }
    }
    
    private static boolean testPaymentStrategyCalled() {
        try {
            var p = new SimpleProduct("A", "A", Money.of(5));
            var order = new Order(42);
            order.addItem(new LineItem(p, 1));
            final boolean[] called = {false};
            PaymentStrategy fake = o -> called[0] = true;
            order.pay(fake);
            
            if (called[0]) {
                System.out.println("✓ Payment strategy delegation test PASSED");
                return true;
            } else {
                System.out.println("✗ Payment strategy delegation test FAILED");
                return false;
            }
        } catch (Exception e) {
            System.out.println("✗ Payment strategy delegation test FAILED with exception: " + e.getMessage());
            return false;
        }
    }
    
    private static boolean testCashPaymentWorks() {
        try {
            var p = new SimpleProduct("TEST", "Test Product", Money.of(10.00));
            var order = new Order(1);
            order.addItem(new LineItem(p, 1));
            order.pay(new CashPayment());
            System.out.println("✓ Cash payment test PASSED");
            return true;
        } catch (Exception e) {
            System.out.println("✗ Cash payment test FAILED with exception: " + e.getMessage());
            return false;
        }
    }
    
    private static boolean testCardPaymentWorks() {
        try {
            var p = new SimpleProduct("TEST", "Test Product", Money.of(10.00));
            var order = new Order(1);
            order.addItem(new LineItem(p, 1));
            order.pay(new CardPayment("1234567812341234"));
            System.out.println("✓ Card payment test PASSED");
            return true;
        } catch (Exception e) {
            System.out.println("✗ Card payment test FAILED with exception: " + e.getMessage());
            return false;
        }
    }
    
    private static boolean testWalletPaymentWorks() {
        try {
            var p = new SimpleProduct("TEST", "Test Product", Money.of(10.00));
            var order = new Order(1);
            order.addItem(new LineItem(p, 1));
            order.pay(new WalletPayment("test-wallet-01"));
            System.out.println("✓ Wallet payment test PASSED");
            return true;
        } catch (Exception e) {
            System.out.println("✗ Wallet payment test FAILED with exception: " + e.getMessage());
            return false;
        }
    }
    
    private static boolean testCardPaymentValidation() {
        try {
            // Test null card number
            try {
                new CardPayment(null);
                System.out.println("✗ Card payment validation test FAILED - should throw exception for null");
                return false;
            } catch (IllegalArgumentException e) {
                // Expected
            }
            
            // Test short card number
            try {
                new CardPayment("123");
                System.out.println("✗ Card payment validation test FAILED - should throw exception for short number");
                return false;
            } catch (IllegalArgumentException e) {
                // Expected
            }
            
            // Test empty card number
            try {
                new CardPayment("");
                System.out.println("✗ Card payment validation test FAILED - should throw exception for empty");
                return false;
            } catch (IllegalArgumentException e) {
                // Expected
            }
            
            System.out.println("✓ Card payment validation test PASSED");
            return true;
        } catch (Exception e) {
            System.out.println("✗ Card payment validation test FAILED with exception: " + e.getMessage());
            return false;
        }
    }
    
    private static boolean testWalletPaymentValidation() {
        try {
            // Test null wallet ID
            try {
                new WalletPayment(null);
                System.out.println("✗ Wallet payment validation test FAILED - should throw exception for null");
                return false;
            } catch (IllegalArgumentException e) {
                // Expected
            }
            
            // Test empty wallet ID
            try {
                new WalletPayment("");
                System.out.println("✗ Wallet payment validation test FAILED - should throw exception for empty");
                return false;
            } catch (IllegalArgumentException e) {
                // Expected
            }
            
            System.out.println("✓ Wallet payment validation test PASSED");
            return true;
        } catch (Exception e) {
            System.out.println("✗ Wallet payment validation test FAILED with exception: " + e.getMessage());
            return false;
        }
    }
    
    private static boolean testOrderPayValidation() {
        try {
            var p = new SimpleProduct("TEST", "Test Product", Money.of(10.00));
            var order = new Order(1);
            order.addItem(new LineItem(p, 1));
            
            // Test null strategy
            try {
                order.pay(null);
                System.out.println("✗ Order pay validation test FAILED - should throw exception for null strategy");
                return false;
            } catch (IllegalArgumentException e) {
                // Expected
            }
            
            System.out.println("✓ Order pay validation test PASSED");
            return true;
        } catch (Exception e) {
            System.out.println("✗ Order pay validation test FAILED with exception: " + e.getMessage());
            return false;
        }
    }
}
