package com.cafepos.payment;

import com.cafepos.common.Money;

public final class CashPayment implements PaymentStrategy {
    @Override
    public void pay(Money total) {
        System.out.println("[Cash] Customer paid " + total + " EUR");
    }
}

