import factory.ProductFactory;

/**
 * CheckoutService - Orchestrates the complete checkout process.
 * 
 * Refactoring Applied: Facade Pattern, Dependency Injection
 * SOLID Principles:
 *   - Single Responsibility: orchestrates checkout workflow
 *   - Dependency Inversion: depends on abstractions (interfaces)
 *   - Interface Segregation: uses focused interfaces
 * 
 * This replaces the God Class OrderManagerGod with a clean, focused service.
 */
public final class CheckoutService {
    private final ProductFactory productFactory;
    private final PricingService pricingService;
    private final ReceiptPrinter receiptPrinter;
    private final double taxPercent;
    
    public CheckoutService(ProductFactory productFactory, 
                          PricingService pricingService,
                          ReceiptPrinter receiptPrinter,
                          double taxPercent) {
        if (productFactory == null) {
            throw new IllegalArgumentException("Product factory is required");
        }
        if (pricingService == null) {
            throw new IllegalArgumentException("Pricing service is required");
        }
        if (receiptPrinter == null) {
            throw new IllegalArgumentException("Receipt printer is required");
        }
        this.productFactory = productFactory;
        this.pricingService = pricingService;
        this.receiptPrinter = receiptPrinter;
        this.taxPercent = taxPercent;
    }
    
    /**
     * Process a checkout and generate a receipt.
     * 
     * @param recipe the product recipe code
     * @param quantity the quantity to order (will be clamped to minimum 1)
     * @param paymentStrategy the payment strategy for receipt display
     * @return formatted receipt string
     */
    public String checkout(String recipe, int quantity, ReceiptPaymentStrategy paymentStrategy) {
        // Clamp quantity to at least 1
        if (quantity < 1) {
            quantity = 1;
        }
        
        // Create product using factory
        Product product = productFactory.create(recipe);
        String productName = product.name();
        
        // Determine unit price
        Money unitPrice;
        if (product instanceof Priced priced) {
            unitPrice = priced.price();
        } else {
            unitPrice = product.basePrice();
        }
        
        // Calculate subtotal
        Money subtotal = unitPrice.multiply(quantity);
        
        // Calculate pricing (discount, tax, total)
        PricingResult result = pricingService.calculate(subtotal);
        
        // Format receipt
        String receipt = receiptPrinter.format(productName, quantity, unitPrice, result, taxPercent);
        
        // Append payment method if provided
        if (paymentStrategy != null) {
            receipt = receiptPrinter.appendPayment(receipt, paymentStrategy.getDescription());
        } else {
            receipt += "===============";
        }
        
        return receipt;
    }
}

