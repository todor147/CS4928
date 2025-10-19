package com.cafepos;

import com.cafepos.payment.*;

public final class PaymentStrategyFactory {
    public static PaymentStrategy createPaymentStrategy(String paymentType) {
        if (paymentType == null) {
            return null;
        }
        
        switch (paymentType.toUpperCase()) {
            case "CASH":
                return new CashPayment();
            case "CARD":
                return new CardPayment("1234");
            case "WALLET":
                return new WalletPayment("user-wallet-789");
            default:
                return null;
        }
    }
}
