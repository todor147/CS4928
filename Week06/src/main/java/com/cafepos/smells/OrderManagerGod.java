package com.cafepos.smells;

import com.cafepos.common.Money;
import com.cafepos.factory.ProductFactory;
import com.cafepos.catalog.Product;
import com.cafepos.DiscountPolicyFactory;
import com.cafepos.pricing.PricingService;
import com.cafepos.pricing.ReceiptPrinter;
import com.cafepos.pricing.FixedRateTaxPolicy;

public class OrderManagerGod {
    private static final int TAX_PERCENT = 10;
    
    public static String process(String recipe, int qty, String paymentType, String discountCode, boolean printReceipt) {
        ProductFactory productFactory = new ProductFactory();
        var discountPolicy = DiscountPolicyFactory.createDiscountPolicy(discountCode);
        var taxPolicy = new FixedRateTaxPolicy(TAX_PERCENT);
        var pricingService = new PricingService(discountPolicy, taxPolicy);
        var receiptPrinter = new ReceiptPrinter();
        
        var product = productFactory.create(recipe);
        var unitPrice = product.basePrice();
        try {
            var priced = product instanceof com.cafepos.decorator.Priced p ? p.price() : product.basePrice();
            unitPrice = priced;
        } catch (Exception e) {
            unitPrice = product.basePrice();
        }
        if (qty <= 0) qty = 1;
        var subtotal = unitPrice.multiply(qty);
        var pricingResult = pricingService.price(subtotal);
        
        if (paymentType != null) {
            switch (paymentType.toUpperCase()) {
                case "CASH":
                    System.out.println("[Cash] Customer paid " + pricingResult.total() + " EUR");
                    break;
                case "CARD":
                    System.out.println("[Card] Customer paid " + pricingResult.total() + " EUR with card ****1234");
                    break;
                case "WALLET":
                    System.out.println("[Wallet] Customer paid " + pricingResult.total() + " EUR via wallet user-wallet-789");
                    break;
                default:
                    System.out.println("[UnknownPayment] " + pricingResult.total());
                    break;
            }
        }
        
        String receipt = receiptPrinter.format(recipe, qty, pricingResult, TAX_PERCENT);
        
        if (printReceipt) {
            System.out.println(receipt);
        }
        
        return receipt;
    }
}