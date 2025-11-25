package com.cafepos;

import com.cafepos.domain.*;
import com.cafepos.common.Money;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {
    @Test
    void testCreateOrder() {
        Order order = new Order(6001L);
        assertEquals(6001L, order.id());
        assertTrue(order.items().isEmpty());
    }

    @Test
    void testAddItem() {
        Order order = new Order(6002L);
        var product = new com.cafepos.catalog.SimpleProduct("ESP", "Espresso", Money.of(2.50));
        order.addItem(new LineItem(product, 2));
        
        assertEquals(1, order.items().size());
        assertEquals(2, order.items().get(0).quantity());
    }

    @Test
    void testSubtotalCalculation() {
        Order order = new Order(6003L);
        var product1 = new com.cafepos.catalog.SimpleProduct("ESP", "Espresso", Money.of(2.50));
        var product2 = new com.cafepos.catalog.SimpleProduct("LAT", "Latte", Money.of(3.00));
        
        order.addItem(new LineItem(product1, 1));
        order.addItem(new LineItem(product2, 2));
        
        Money subtotal = order.subtotal();
        // 2.50 + (3.00 * 2) = 8.50
        assertEquals(Money.of(8.50), subtotal);
    }

    @Test
    void testTaxCalculation() {
        Order order = new Order(6004L);
        var product = new com.cafepos.catalog.SimpleProduct("ESP", "Espresso", Money.of(10.00));
        order.addItem(new LineItem(product, 1));
        
        Money tax = order.taxAtPercent(10);
        // 10% of 10.00 = 1.00
        assertEquals(Money.of(1.00), tax);
    }

    @Test
    void testTotalWithTax() {
        Order order = new Order(6005L);
        var product = new com.cafepos.catalog.SimpleProduct("ESP", "Espresso", Money.of(10.00));
        order.addItem(new LineItem(product, 1));
        
        Money total = order.totalWithTax(10);
        // 10.00 + 1.00 = 11.00
        assertEquals(Money.of(11.00), total);
    }

    @Test
    void testRemoveLastItem() {
        Order order = new Order(6006L);
        var product1 = new com.cafepos.catalog.SimpleProduct("ESP", "Espresso", Money.of(2.50));
        var product2 = new com.cafepos.catalog.SimpleProduct("LAT", "Latte", Money.of(3.00));
        
        order.addItem(new LineItem(product1, 1));
        order.addItem(new LineItem(product2, 1));
        assertEquals(2, order.items().size());
        
        order.removeLastItem();
        assertEquals(1, order.items().size());
        assertEquals("Espresso", order.items().get(0).product().name());
    }

    @Test
    void testAddItemWithNullThrows() {
        Order order = new Order(6007L);
        assertThrows(IllegalArgumentException.class, () -> {
            order.addItem(null);
        });
    }

    @Test
    void testAddItemWithZeroQuantityThrows() {
        Order order = new Order(6008L);
        var product = new com.cafepos.catalog.SimpleProduct("ESP", "Espresso", Money.of(2.50));
        assertThrows(IllegalArgumentException.class, () -> {
            order.addItem(new LineItem(product, 0));
        });
    }
}


