package com.cafepos.payment;

import com.cafepos.common.Money;

public final class ReceiptWalletPayment implements ReceiptPaymentStrategy {
    private final String walletId;

    public ReceiptWalletPayment(String walletId) {
        if (walletId == null || walletId.isEmpty())
            throw new IllegalArgumentException("walletId required");
        this.walletId = walletId;
    }

    @Override
    public void pay(Money total) {
        System.out.println("[Wallet] Customer paid " + total + " EUR via wallet " + walletId);
    }
}
