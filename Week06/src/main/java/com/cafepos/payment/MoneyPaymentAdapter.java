package com.cafepos.payment;

import com.cafepos.common.Money;

public class MoneyPaymentAdapter implements PaymentStrategy {
    private final Money total;
    private final String paymentType;
    
    public MoneyPaymentAdapter(Money total, String paymentType) {
        this.total = total;
        this.paymentType = paymentType;
    }
    
    @Override
    public void pay(Order order) {
        // This won't be called since we're using Money directly
    }
    
    public void payDirectly() {
        switch (paymentType.toUpperCase()) {
            case "CASH":
                System.out.println("[Cash] Customer paid " + total + " EUR");
                break;
            case "CARD":
                System.out.println("[Card] Customer paid " + total + " EUR with card ****1234");
                break;
            case "WALLET":
                System.out.println("[Wallet] Customer paid " + total + " EUR via wallet user-wallet-789");
                break;
            default:
                System.out.println("[UnknownPayment] " + total);
                break;
        }
    }
}
