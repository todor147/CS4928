package com.cafepos.demo;

import com.cafepos.smells.OrderManagerGod;
import com.cafepos.factory.ProductFactory;
import com.cafepos.pricing.*;
import com.cafepos.checkout.CheckoutService;
import com.cafepos.strategy.*;

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

        // new clean flow
        var pricing = new PricingService(new LoyaltyPercentDiscount(5), new FixedRateTaxPolicy(10));
        var printer = new ReceiptPrinter();
        var payment = new CardPaymentStrategy("1234"); // can choose Cash/Wallet too
        var checkout = new CheckoutService(new ProductFactory(), pricing, printer, payment, 10);
        String newReceipt = checkout.checkout(recipe, qty);

        // print comparison
        System.out.println("\n--- Old Receipt ---\n" + oldReceipt);
        System.out.println("\n--- New Receipt ---\n" + newReceipt);
        System.out.println("\nMatch: " + oldReceipt.equals(newReceipt));
    }
}
