package com.cafepos.smells;

import com.cafepos.common.Money;
import com.cafepos.factory.ProductFactory;
import com.cafepos.catalog.Product;
import com.cafepos.CheckoutService;
import com.cafepos.DiscountPolicyFactory;
import com.cafepos.PaymentStrategyFactory;
import com.cafepos.pricing.PricingService;
import com.cafepos.pricing.ReceiptPrinter;
import com.cafepos.pricing.FixedRateTaxPolicy;

public class OrderManagerGod {
    // REFACTORED: Global state removed - dependencies now injected
    private static final int TAX_PERCENT = 10;
    
    // REFACTORED: Long Method eliminated - logic moved to CheckoutService
    // REFACTORED: God Class eliminated - responsibilities distributed
    public static String process(String recipe, int qty, String paymentType, String discountCode, boolean printReceipt) {
        // Create dependencies (in real app, these would be injected)
        ProductFactory productFactory = new ProductFactory();
        var discountPolicy = DiscountPolicyFactory.createDiscountPolicy(discountCode);
        var taxPolicy = new FixedRateTaxPolicy(TAX_PERCENT);
        var pricingService = new PricingService(discountPolicy, taxPolicy);
        var receiptPrinter = new ReceiptPrinter();
        var paymentStrategy = PaymentStrategyFactory.createPaymentStrategy(paymentType);
        
        // Create CheckoutService with injected dependencies
        var checkoutService = new CheckoutService(productFactory, pricingService, receiptPrinter, TAX_PERCENT);
        
        // Delegate to CheckoutService
        String receipt = checkoutService.checkout(recipe, qty, paymentStrategy);
        
        if (printReceipt) {
            System.out.println(receipt);
        }
        
        return receipt;
    }
}