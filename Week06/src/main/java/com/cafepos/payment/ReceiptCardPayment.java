package com.cafepos.payment;

import com.cafepos.common.Money;

public final class ReceiptCardPayment implements ReceiptPaymentStrategy {
    private final String cardNumber;

    public ReceiptCardPayment(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4)
            throw new IllegalArgumentException("Card number must have at least 4 digits");
        this.cardNumber = cardNumber;
    }

    @Override
    public void pay(Money total) {
        String masked = "****" + cardNumber.substring(cardNumber.length() - 4);
        System.out.println("[Card] Customer paid " + total + " EUR with card " + masked);
    }
}
