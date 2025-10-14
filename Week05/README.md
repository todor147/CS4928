# Week 5 Lab — Decorator + Factory Café POS & Delivery Project

## Overview
This week extends the Café POS system by implementing the **Decorator Pattern** and **Factory Pattern** to add optional features to base products without modifying existing classes, reinforcing the **Open/Closed Principle**.

## Implementation Summary

### 1. Priced Interface
Created a `Priced` interface with a single `price()` method to standardize pricing across both simple products and decorated products. This allows `LineItem` to uniformly access the final price of any product, whether it's a base product or a decorated one.

```java
public interface Priced {
    Money price();
}
```

### 2. Decorator Pattern
Implemented the Decorator pattern with:
- **ProductDecorator** (abstract base class): Wraps a `Product` and implements both `Product` and `Priced` interfaces
- **Concrete Decorators**:
  - `ExtraShot`: Adds $0.80
  - `OatMilk`: Adds $0.50
  - `Syrup`: Adds $0.40
  - `SizeLarge`: Adds $0.70

Each decorator chains the pricing calculation by calling the wrapped product's price and adding its own surcharge.

### 3. Factory Pattern
The `ProductFactory` creates products from recipe strings (e.g., "ESP+SHOT+OAT+L"):
- First token specifies the base product (ESP, LAT, CAP)
- Subsequent tokens apply decorators in order (SHOT, OAT, SYP, L)
- Trims and uppercases all tokens for consistency
- Fails fast with clear error messages for unknown tokens

### 4. Updated Components
- **SimpleProduct**: Now implements `Priced` interface where `price()` returns `basePrice()`
- **LineItem**: Updated `lineTotal()` to use `Priced` interface when available, falling back to `basePrice()` otherwise
- **Order**: No changes required; works seamlessly with the new pricing system

## Running the Demo

### Compile
```powershell
cd Week05
javac *.java payment/*.java
```

Or use the batch file:
```powershell
compile.bat
```

### Run Demo
```powershell
java Week5Demo
```

Or use the batch file:
```powershell
run-demo.bat
```

The demo now features an **interactive CLI** with multiple options:
1. **Create Custom Order (Interactive)** - Build your order step-by-step with prompts
2. **Create Order Using Recipe Codes** - Enter recipe strings like "ESP+SHOT+OAT"
3. **Run Sample Demo** - Quick demo with pre-configured order
4. **View Recipe Code Reference** - See all available base products and add-ons
5. **Exit**

### Sample Demo Output (Option 3)
```
Order #1000
 - Espresso + Extra Shot + Oat Milk x1 = $3.80
 - Latte (Large) x2 = $7.80
Subtotal: $11.60
Tax (10%): $1.16
Total: $12.76
```

### Run Tests
```powershell
java Week5Tests
```

Or use the batch file:
```powershell
run-tests.bat
```

## Reflection

### Why the Priced Interface?
I chose to introduce the `Priced` interface to create a clear contract for obtaining the final price of any product. This design allows both `SimpleProduct` (where price equals basePrice) and decorated products (where price includes surcharges) to be treated uniformly. The alternative would have been to add conditional logic throughout the codebase to check for decorators, which would violate the Open/Closed Principle and make the code harder to maintain.

### Preserving the Open/Closed Principle

**Decorator Pattern**: The decorator implementation demonstrates OCP perfectly. When we need to add a new add-on (e.g., "Whipped Cream"), we simply create a new decorator class extending `ProductDecorator`. No existing classes need modification. The base classes (`Product`, `SimpleProduct`, `Order`, `LineItem`) remain completely untouched.

**Factory Pattern**: The factory also preserves OCP, though with a small caveat. To add a new base product (e.g., "Mocha"), we only need to add a new case to the factory's switch statement. While this technically modifies the factory class, it's a localized, minimal change that doesn't affect any other part of the system. The factory acts as a single point of extension, which is much better than having product construction logic scattered throughout the application.

### Adding a New Add-On
To add a new add-on next week (e.g., "Caramel Drizzle" for $0.60):

1. Create a new decorator class `decorator/CaramelDrizzle.java`:
```java
public final class CaramelDrizzle extends ProductDecorator {
    private static final Money SURCHARGE = Money.of(0.60);
    
    public CaramelDrizzle(Product base) { super(base); }
    
    @Override
    public String name() { return base.name() + " + Caramel Drizzle"; }
    
    @Override
    public Money price() { 
        return (base instanceof Priced p ? p.price() : base.basePrice()).add(SURCHARGE); 
    }
}
```

2. Add a token to the factory (e.g., "CAR" → `new CaramelDrizzle(p)`)

That's it! No changes to any other classes are required. This demonstrates that the design is truly open for extension but closed for modification.

### Factory vs. Manual Construction

**For application developers, I would expose the Factory approach.** Here's why:

The factory provides several advantages over manual construction:
1. **Simplicity**: Developers use simple string recipes ("ESP+SHOT+OAT") instead of verbose nested constructors
2. **Consistency**: The factory ensures decorators are applied correctly and in a consistent manner
3. **Maintainability**: If we need to change how products are constructed (e.g., adding validation, logging, or caching), we only modify the factory
4. **Error Prevention**: The factory validates tokens and provides clear error messages, preventing mistakes like forgetting to null-check or applying decorators in invalid ways

Manual construction (`new SizeLarge(new OatMilk(new ExtraShot(...)))`) is more flexible but also more error-prone and harder to read. It's better suited for internal testing or special cases, but for everyday use in an application, the factory pattern provides a much better developer experience while maintaining all the benefits of the decorator pattern under the hood.

## Design Patterns Applied

1. **Decorator Pattern**: Allows dynamic addition of responsibilities to objects
2. **Factory Pattern**: Encapsulates object creation logic
3. **Strategy Pattern** (from Week 3): Payment methods
4. **Observer Pattern** (from Week 4): Order event notifications

## Files Structure
```
Week05/
├── Core Interfaces
│   ├── Product.java (interface)
│   ├── Priced.java (interface)
│   ├── Catalog.java (interface)
│   ├── OrderObserver.java (interface)
│   └── OrderPublisher.java (interface)
├── Core Classes
│   ├── SimpleProduct.java (implements Product, Priced)
│   ├── Money.java
│   ├── LineItem.java
│   ├── Order.java
│   ├── OrderIds.java
│   └── InMemoryCatalog.java
├── Decorator Pattern
│   ├── ProductDecorator.java (abstract)
│   ├── ExtraShot.java
│   ├── OatMilk.java
│   ├── Syrup.java
│   └── SizeLarge.java
├── Factory Pattern
│   └── ProductFactory.java
├── Payment Strategy (Week 3)
│   └── payment/
│       ├── PaymentStrategy.java (interface)
│       ├── CardPayment.java
│       ├── CashPayment.java
│       └── WalletPayment.java
├── Demo & Tests
│   ├── Week5Demo.java
│   └── Week5Tests.java
├── Helper Scripts
│   ├── compile.bat
│   ├── run-demo.bat
│   └── run-tests.bat
└── Documentation
    ├── README.md
    └── QUICKSTART.txt
```

## Testing
All tests pass successfully:
- ✓ Single decorator changes price and name correctly
- ✓ Multiple decorators stack correctly
- ✓ Factory parses recipes correctly
- ✓ Orders use decorated prices
- ✓ Factory and manual construction produce identical results
- ✓ Decoration order doesn't affect final price (commutative)
- ✓ All base products work correctly
- ✓ All decorators work correctly

## Notes
- Week 5 lab session assesses Week 4 work (Observer Pattern)
- Week 5 Decorator + Factory work will be assessed in Week 6
- All code follows Open/Closed Principle
- System uses Java 21 features (pattern matching, switch expressions)

