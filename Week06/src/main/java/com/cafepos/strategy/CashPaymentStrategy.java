package com.cafepos.strategy;

import com.cafepos.common.Money;

public final class CashPaymentStrategy implements PaymentStrategy {
    @Override
    public void pay(Money total) {
        System.out.println("[Cash] Customer paid " + total + " EUR");
    }
}
