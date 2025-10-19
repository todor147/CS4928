package com.cafepos.demo;

import com.cafepos.smells.OrderManagerGod;
import com.cafepos.factory.ProductFactory;
import com.cafepos.pricing.*;
import com.cafepos.payment.*;

import java.util.Scanner;

public final class Week6Demo {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Recipe code: ");
        String recipe = sc.nextLine();
        System.out.print("Quantity: ");
        int qty = sc.nextInt();
        sc.nextLine();

        String oldReceipt = OrderManagerGod.process(recipe, qty, "CARD", "LOYAL5", false);

        var productFactory = new ProductFactory();
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
        
        var discountPolicy = new LoyaltyPercentDiscount(5);
        var taxPolicy = new FixedRateTaxPolicy(10);
        var pricingService = new PricingService(discountPolicy, taxPolicy);
        var pricingResult = pricingService.price(subtotal);
        
        var receiptPrinter = new ReceiptPrinter();
        String newReceipt = receiptPrinter.format(recipe, qty, pricingResult, 10);
        
        System.out.println("[Card] Customer paid " + pricingResult.total() + " EUR with card ****1234");

        System.out.println("\n--- Old Receipt ---\n" + oldReceipt);
        System.out.println("\n--- New Receipt ---\n" + newReceipt);
        System.out.println("\nMatch: " + oldReceipt.equals(newReceipt));
    }
}