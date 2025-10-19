package com.cafepos;

import com.cafepos.strategy.CardPaymentStrategy;
import com.cafepos.strategy.CashPaymentStrategy;
import com.cafepos.strategy.PaymentStrategy;
import com.cafepos.strategy.WalletPaymentStrategy;

public final class PaymentStrategyFactory {
    public static PaymentStrategy createPaymentStrategy(String paymentType) {
        if (paymentType == null) {
            return null;
        }
        
        switch (paymentType.toUpperCase()) {
            case "CASH":
                return new CashPaymentStrategy();
            case "CARD":
                return new CardPaymentStrategy("1234");
            case "WALLET":
                return new WalletPaymentStrategy("user-wallet-789");
            default:
                return null;
        }
    }
}
