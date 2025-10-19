package com.cafepos.strategy;

import com.cafepos.common.Money;

public final class WalletPaymentStrategy implements PaymentStrategy {
    private final String walletId;

    public WalletPaymentStrategy(String walletId) {
        if (walletId == null || walletId.isEmpty())
            throw new IllegalArgumentException("walletId required");
        this.walletId = walletId;
    }

    @Override
    public void pay(Money total) {
        System.out.println("[Wallet] Customer paid " + total + " EUR via wallet " + walletId);
    }
}
