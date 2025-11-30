package com.cafepos.payment;

import com.cafepos.common.Money;

public interface PaymentStrategy {
    void pay(Money total);
}

