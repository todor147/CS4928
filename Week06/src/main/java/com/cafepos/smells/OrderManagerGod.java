package com.cafepos.smells;
import com.cafepos.common.Money;
import com.cafepos.factory.ProductFactory;
import com.cafepos.catalog.Product;

public class OrderManagerGod {
    // SMELL: Global/Static State - shared mutable state across all instances
    public static int TAX_PERCENT = 10;
    // SMELL: Global/Static State - shared mutable state across all instances
    public static String LAST_DISCOUNT_CODE = null;
    
    // SMELL: Long Method - this method does too many things (product creation, pricing, tax, receipt formatting, payment)
    // SMELL: God Class - this class knows too much and has too many responsibilities
    public static String process(String recipe, int qty, String paymentType, String discountCode, boolean printReceipt) {
        ProductFactory factory = new ProductFactory();
        Product product = factory.create(recipe);
        Money unitPrice;
        try {
            var priced = product instanceof com.cafepos.decorator.Priced p ? p.price() : product.basePrice();
            unitPrice = priced;
        } catch (Exception e) {
            unitPrice = product.basePrice();
        }
        if (qty <= 0) qty = 1;
        Money subtotal = unitPrice.multiply(qty);
        Money discount = Money.zero();
        if (discountCode != null) {
            // SMELL: Primitive Obsession - using raw strings instead of proper types for discount codes
            // SMELL: Shotgun Surgery risk - changing discount rules requires editing this method
            if (discountCode.equalsIgnoreCase("LOYAL5")) {
                // SMELL: Duplicated Logic - BigDecimal math repeated for percentage calculations
                // SMELL: Feature Envy - discount calculation logic embedded inline
                discount = Money.of(subtotal.asBigDecimal()
                        .multiply(java.math.BigDecimal.valueOf(5))
                        .divide(java.math.BigDecimal.valueOf(100)));
            } else if (discountCode.equalsIgnoreCase("COUPON1")) {
                // SMELL: Primitive Obsession - magic number 1.00 hardcoded
                // SMELL: Feature Envy - discount calculation logic embedded inline
                discount = Money.of(1.00);
            } else if (discountCode.equalsIgnoreCase("NONE")) {
                discount = Money.zero();
            } else {
                discount = Money.zero();
            }
            // SMELL: Global/Static State - mutating static state
            LAST_DISCOUNT_CODE = discountCode;
        }
        Money discounted = Money.of(subtotal.asBigDecimal().subtract(discount.asBigDecimal()));
        if (discounted.asBigDecimal().signum() < 0) discounted = Money.zero();
        // SMELL: Duplicated Logic - BigDecimal math repeated for tax calculation
        // SMELL: Feature Envy - tax calculation logic embedded inline
        // SMELL: Shotgun Surgery risk - changing tax rules requires editing this method
        var tax = Money.of(discounted.asBigDecimal()
                .multiply(java.math.BigDecimal.valueOf(TAX_PERCENT))
                .divide(java.math.BigDecimal.valueOf(100)));
        var total = discounted.add(tax);
        if (paymentType != null) {
            // SMELL: Primitive Obsession - using raw strings for payment types
            // SMELL: Shotgun Surgery risk - adding new payment methods requires editing this method
            if (paymentType.equalsIgnoreCase("CASH")) {
                System.out.println("[Cash] Customer paid " + total + " EUR");
            } else if (paymentType.equalsIgnoreCase("CARD")) {
                System.out.println("[Card] Customer paid " + total + " EUR with card ****1234");
            } else if (paymentType.equalsIgnoreCase("WALLET")) {
                System.out.println("[Wallet] Customer paid " + total + " EUR via wallet user-wallet-789");
            } else {
                System.out.println("[UnknownPayment] " + total);
            }
        }
        StringBuilder receipt = new StringBuilder();
        receipt.append("Order (").append(recipe).append(") x").append(qty).append("\n");
        receipt.append("Subtotal: ").append(subtotal).append("\n");
        if (discount.asBigDecimal().signum() > 0) {
            receipt.append("Discount: -").append(discount).append("\n");
        }
        receipt.append("Tax (").append(TAX_PERCENT).append("%): ").append(tax).append("\n");
        receipt.append("Total: ").append(total);
        String out = receipt.toString();
        if (printReceipt) {
            System.out.println(out);
        }
        return out;
    }
}