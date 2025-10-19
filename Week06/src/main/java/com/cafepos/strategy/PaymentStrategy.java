package com.cafepos.strategy;

import com.cafepos.common.Money;

public interface PaymentStrategy {
    void pay(Money total);
}
