package com.cafepos.payment;

import com.cafepos.common.Money;

public interface ReceiptPaymentStrategy {
    void pay(Money total);
}
