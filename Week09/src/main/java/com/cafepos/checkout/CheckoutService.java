package com.cafepos.checkout;

import com.cafepos.common.Money;
import com.cafepos.factory.ProductFactory;
import com.cafepos.pricing.PricingService;
import com.cafepos.pricing.ReceiptPrinter;
import com.cafepos.strategy.PaymentStrategy;

public final class CheckoutService {
    private final ProductFactory factory;
    private final PricingService pricing;
    private final ReceiptPrinter printer;
    private final PaymentStrategy payment;
    private final int taxPercent;

    public CheckoutService(ProductFactory factory, PricingService pricing,
                           ReceiptPrinter printer, PaymentStrategy payment,
                           int taxPercent) {
        this.factory = factory;
        this.pricing = pricing;
        this.printer = printer;
        this.payment = payment;
        this.taxPercent = taxPercent;
    }

    public String checkout(String recipe, int qty) {
        var product = factory.create(recipe);
        if (qty <= 0) qty = 1;
        
        // Get unit price - try Priced interface first, fallback to basePrice
        Money unitPrice;
        try {
            var priced = product instanceof com.cafepos.decorator.Priced p ? p.price() : product.basePrice();
            unitPrice = priced;
        } catch (Exception e) {
            unitPrice = product.basePrice();
        }
        
        Money subtotal = unitPrice.multiply(qty);
        PricingService.PricingResult pr = pricing.price(subtotal);
        String receipt = printer.format(recipe, qty, pr, taxPercent);
        payment.pay(pr.total());
        return receipt;
    }
}
