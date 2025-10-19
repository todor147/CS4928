package com.cafepos;

import com.cafepos.common.Money;
import com.cafepos.catalog.Product;
import com.cafepos.decorator.Priced;
import com.cafepos.factory.ProductFactory;
import com.cafepos.payment.ReceiptPaymentStrategy;
import com.cafepos.pricing.PricingService;
import com.cafepos.pricing.ReceiptPrinter;

public final class CheckoutService {
    private final ProductFactory productFactory;
    private final PricingService pricingService;
    private final ReceiptPrinter receiptPrinter;
    private final int taxPercent;

    public CheckoutService(ProductFactory productFactory, PricingService pricingService, 
                          ReceiptPrinter receiptPrinter, int taxPercent) {
        this.productFactory = productFactory;
        this.pricingService = pricingService;
        this.receiptPrinter = receiptPrinter;
        this.taxPercent = taxPercent;
    }

    public String checkout(String recipe, int qty, ReceiptPaymentStrategy paymentStrategy) {
        // Create product using factory
        Product product = productFactory.create(recipe);
        
        // Determine unit price
        Money unitPrice;
        try {
            var priced = product instanceof Priced p ? p.price() : product.basePrice();
            unitPrice = priced;
        } catch (Exception e) {
            unitPrice = product.basePrice();
        }
        
        // Clamp quantity to at least 1
        if (qty <= 0) qty = 1;
        
        // Compute subtotal
        Money subtotal = unitPrice.multiply(qty);
        
        // Delegate pricing to PricingService
        var pricingResult = pricingService.price(subtotal);
        
        // Handle payment
        if (paymentStrategy != null) {
            paymentStrategy.pay(pricingResult.total());
        }
        
        // Delegate printing to ReceiptPrinter
        return receiptPrinter.format(recipe, qty, pricingResult, taxPercent);
    }
}
