import com.cafepos.smells.OrderManagerGod;

public class debug_test {
    public static void main(String[] args) {
        System.out.println("=== Debug Test ===");
        
        String receipt1 = OrderManagerGod.process("ESP+SHOT+OAT", 1, "CASH", "NONE", false);
        System.out.println("Test 1 - ESP+SHOT+OAT:");
        System.out.println(receipt1);
        System.out.println();
        
        String receipt2 = OrderManagerGod.process("LAT+L", 2, "CARD", "LOYAL5", false);
        System.out.println("Test 2 - LAT+L with LOYAL5:");
        System.out.println(receipt2);
        System.out.println();
        
        String receipt3 = OrderManagerGod.process("ESP+SHOT", 0, "WALLET", "COUPON1", false);
        System.out.println("Test 3 - ESP+SHOT with COUPON1:");
        System.out.println(receipt3);
    }
}
