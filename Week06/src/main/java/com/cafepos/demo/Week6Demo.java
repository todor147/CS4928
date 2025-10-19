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

        // old smelly flow
        String oldReceipt = OrderManagerGod.process(recipe, qty, "CARD", "LOYAL5", false);

        // new clean flow - manually create the components
        var productFactory = new ProductFactory();
        var product = productFactory.create(recipe);
        
        // Get unit price - use same logic as OrderManagerGod
        var unitPrice = product.basePrice();
        try {
            var priced = product instanceof com.cafepos.decorator.Priced p ? p.price() : product.basePrice();
            unitPrice = priced;
        } catch (Exception e) {
            unitPrice = product.basePrice();
        }
        if (qty <= 0) qty = 1;
        var subtotal = unitPrice.multiply(qty);
        
        // Apply discount and tax
        var discountPolicy = new LoyaltyPercentDiscount(5);
        var taxPolicy = new FixedRateTaxPolicy(10);
        var pricingService = new PricingService(discountPolicy, taxPolicy);
        var pricingResult = pricingService.price(subtotal);
        
        // Create receipt
        var receiptPrinter = new ReceiptPrinter();
        String newReceipt = receiptPrinter.format(recipe, qty, pricingResult, 10);
        
        // Handle payment (manually since we can't easily adapt the existing payment classes)
        System.out.println("[Card] Customer paid " + pricingResult.total() + " EUR with card ****1234");

        // print comparison
        System.out.println("\n--- Old Receipt ---\n" + oldReceipt);
        System.out.println("\n--- New Receipt ---\n" + newReceipt);
        System.out.println("\nMatch: " + oldReceipt.equals(newReceipt));
    }
}