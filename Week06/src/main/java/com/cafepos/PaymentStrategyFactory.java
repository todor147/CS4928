package com.cafepos;

import com.cafepos.payment.ReceiptCardPayment;
import com.cafepos.payment.ReceiptCashPayment;
import com.cafepos.payment.ReceiptPaymentStrategy;
import com.cafepos.payment.ReceiptWalletPayment;

public final class PaymentStrategyFactory {
    public static ReceiptPaymentStrategy createPaymentStrategy(String paymentType) {
        if (paymentType == null) {
            return null;
        }
        
        switch (paymentType.toUpperCase()) {
            case "CASH":
                return new ReceiptCashPayment();
            case "CARD":
                return new ReceiptCardPayment("1234");
            case "WALLET":
                return new ReceiptWalletPayment("user-wallet-789");
            default:
                return null;
        }
    }
}
