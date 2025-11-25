package com.cafepos;

import com.cafepos.app.CheckoutService;
import com.cafepos.domain.*;
import com.cafepos.infra.InMemoryOrderRepository;
import com.cafepos.pricing.LoyaltyPercentDiscount;
import com.cafepos.pricing.FixedRateTaxPolicy;
import com.cafepos.pricing.PricingService;
import com.cafepos.common.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheckoutServiceTest {
    private OrderRepository repo;
    private CheckoutService checkout;

    @BeforeEach
    void setUp() {
        repo = new InMemoryOrderRepository();
        var pricing = new PricingService(
            new LoyaltyPercentDiscount(5),
            new FixedRateTaxPolicy(10)
        );
        checkout = new CheckoutService(repo, pricing);
    }

    @Test
    void testCheckoutSingleItem() {
        var order = new Order(5001L);
        order.addItem(new LineItem(
            new com.cafepos.catalog.SimpleProduct("ESP", "Espresso", Money.of(2.50)),
            1
        ));
        repo.save(order);

        String receipt = checkout.checkout(5001L, 10);
        assertNotNull(receipt);
        assertTrue(receipt.contains("Order #5001"));
        assertTrue(receipt.contains("Espresso"));
    }

    @Test
    void testCheckoutCalculatesTax() {
        var order = new Order(5002L);
        order.addItem(new LineItem(
            new com.cafepos.catalog.SimpleProduct("ESP", "Espresso", Money.of(10.00)),
            1
        ));
        repo.save(order);

        String receipt = checkout.checkout(5002L, 10);
        // Tax should be 10% of subtotal (after discount)
        assertTrue(receipt.contains("Tax"));
    }

    @Test
    void testCheckoutAppliesDiscount() {
        var order = new Order(5003L);
        order.addItem(new LineItem(
            new com.cafepos.catalog.SimpleProduct("ESP", "Espresso", Money.of(10.00)),
            1
        ));
        repo.save(order);

        String receipt = checkout.checkout(5003L, 10);
        // Should show 5% loyalty discount
        assertTrue(receipt.contains("Discount") || receipt.contains("discount"));
    }

    @Test
    void testCheckoutNonExistentOrderThrows() {
        assertThrows(Exception.class, () -> {
            checkout.checkout(9999L, 10);
        });
    }
}

