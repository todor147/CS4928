package com.cafepos;

import com.cafepos.app.CheckoutService;
import com.cafepos.domain.*;
import com.cafepos.infra.InMemoryOrderRepository;
import com.cafepos.pricing.LoyaltyPercentDiscount;
import com.cafepos.pricing.FixedRateTaxPolicy;
import com.cafepos.pricing.PricingService;
import com.cafepos.ui.OrderController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderControllerTest {
    private OrderRepository repo;
    private CheckoutService checkout;
    private OrderController controller;

    @BeforeEach
    void setUp() {
        repo = new InMemoryOrderRepository();
        var pricing = new PricingService(
            new LoyaltyPercentDiscount(5),
            new FixedRateTaxPolicy(10)
        );
        checkout = new CheckoutService(repo, pricing);
        controller = new OrderController(repo, checkout);
    }

    @Test
    void testCreateOrder() {
        long orderId = controller.createOrder(1001L);
        assertEquals(1001L, orderId);
        
        var order = repo.findById(1001L);
        assertTrue(order.isPresent());
        assertEquals(1001L, order.get().id());
    }

    @Test
    void testAddItem() {
        controller.createOrder(2001L);
        controller.addItem(2001L, "ESP", 1);
        
        var order = repo.findById(2001L).orElseThrow();
        assertEquals(1, order.items().size());
        assertEquals("Espresso", order.items().getFirst().product().name());
    }

    @Test
    void testAddMultipleItems() {
        controller.createOrder(3001L);
        controller.addItem(3001L, "ESP+SHOT", 1);
        controller.addItem(3001L, "LAT", 2);
        
        var order = repo.findById(3001L).orElseThrow();
        assertEquals(2, order.items().size());
        assertEquals(1, order.items().getFirst().quantity());
        assertEquals(2, order.items().get(1).quantity());
    }

    @Test
    void testCheckoutReturnsReceipt() {
        controller.createOrder(4001L);
        controller.addItem(4001L, "ESP", 1);
        
        String receipt = controller.checkout(4001L, 10);
        assertNotNull(receipt);
        assertTrue(receipt.contains("Order #4001"));
        assertTrue(receipt.contains("Espresso"));
    }

    @Test
    void testAddItemToNonExistentOrderThrows() {
        assertThrows(Exception.class, () -> {
            controller.addItem(9999L, "ESP", 1);
        });
    }
}


