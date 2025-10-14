# Week 5 Lab Deliverables Checklist

## ✅ Completed Items

### 1. Core Implementation
- [x] **Priced Interface** - Uniform pricing contract for products
- [x] **ProductDecorator** - Abstract base class for all decorators
- [x] **Concrete Decorators** - ExtraShot, OatMilk, Syrup, SizeLarge
- [x] **ProductFactory** - Recipe-based product creation
- [x] **Updated SimpleProduct** - Implements Priced interface
- [x] **Updated LineItem** - Uses Priced interface for pricing

### 2. Demonstration
- [x] **Week5Demo.java** - CLI demo showing decorator and factory in action
- [x] **Expected Output** - Correctly shows decorated names and prices:
  ```
  Order #1000
   - Espresso + Extra Shot + Oat Milk x1 = $3.80
   - Latte (Large) x2 = $7.80
  Subtotal: $11.60
  Tax (10%): $1.16
  Total: $12.76
  ```

### 3. Testing
- [x] **Week5Tests.java** - Comprehensive unit tests
- [x] **Test Coverage**:
  - Single decorator functionality
  - Multiple decorator stacking
  - Factory recipe parsing
  - Order pricing with decorators
  - Factory vs manual construction equivalence
  - Decoration order independence
  - All base products
  - All decorators individually
- [x] **All Tests Pass** ✓

### 4. Documentation
- [x] **README.md** - Complete documentation with:
  - Implementation summary
  - Running instructions
  - Reflection on design choices
  - OCP preservation explanation
  - Factory vs manual comparison
- [x] **QUICKSTART.txt** - Quick reference guide
- [x] **CLASS_DIAGRAM.txt** - Detailed class diagram and relationships
- [x] **DELIVERABLES.md** - This checklist

### 5. Helper Scripts
- [x] **compile.bat** - Easy compilation script
- [x] **run-demo.bat** - Demo execution script
- [x] **run-tests.bat** - Test execution script

## Design Patterns Implemented

### Decorator Pattern (New in Week 5)
- **Purpose**: Add optional features to products without modifying base classes
- **Implementation**: 
  - `ProductDecorator` abstract base
  - 4 concrete decorators (ExtraShot, OatMilk, Syrup, SizeLarge)
- **Benefits**: 
  - Open for extension, closed for modification
  - Flexible composition of features
  - Runtime configuration

### Factory Pattern (New in Week 5)
- **Purpose**: Encapsulate product creation logic
- **Implementation**: 
  - `ProductFactory` with recipe string parsing
  - Token-based product and decorator application
- **Benefits**:
  - Centralized creation logic
  - Consistent product construction
  - Easy to add new recipes

### Strategy Pattern (From Week 3)
- Payment methods (Card, Cash, Wallet)
- Maintained in `payment/` directory

### Observer Pattern (From Week 4)
- Order event notifications
- OrderPublisher/OrderObserver interfaces

## Key Design Decisions

### 1. Priced Interface
**Decision**: Introduce separate `Priced` interface with `price()` method

**Rationale**: 
- Separates concerns: `basePrice()` for original price, `price()` for final price
- Allows uniform treatment of simple and decorated products
- Avoids complex instanceof chains in LineItem
- Preserves existing Product interface

### 2. Pattern Matching in Decorators
**Decision**: Use pattern matching to check for Priced interface

**Code**: `base instanceof Priced p ? p.price() : base.basePrice()`

**Rationale**:
- Handles both Priced and non-Priced products gracefully
- Uses Java 21 pattern matching features
- Fails gracefully for edge cases

### 3. Factory Token Design
**Decision**: Use uppercase tokens with `+` separator

**Rationale**:
- Case-insensitive (trimmed and uppercased)
- Natural syntax for "adding" features
- Clear separation of base and decorators
- Easy to extend with new tokens

## Open/Closed Principle Preservation

### Adding a New Decorator (Open for Extension)
To add "Whipped Cream" (+$0.60):
1. Create `WhippedCream.java` extending `ProductDecorator`
2. Add token "WHIP" to factory switch
3. **No modification to existing classes needed**

### Adding a New Base Product (Minimal Change)
To add "Mocha" ($3.50):
1. Add case in factory: `case "MOC" -> new SimpleProduct(...)`
2. **No modification to other classes needed**

### Why This Works
- Product interface is abstract (dependency inversion)
- Decorators compose via Product interface
- Factory is single point of extension
- LineItem and Order work with any Product

## Coordinator Demonstration

### Quick Demo Commands (Windows)
```powershell
cd Week05
compile.bat
run-demo.bat
```

### Expected Coordinator Check
✅ Decorated names appear on each line
✅ Prices are correct (base + surcharges)
✅ Totals calculate properly
✅ Code compiles without errors
✅ All tests pass

## Files Created/Modified

### New Files (Week 5)
- `Priced.java` - Pricing interface
- `ProductDecorator.java` - Decorator base
- `ExtraShot.java` - Concrete decorator
- `OatMilk.java` - Concrete decorator
- `Syrup.java` - Concrete decorator
- `SizeLarge.java` - Concrete decorator
- `ProductFactory.java` - Factory implementation
- `Week5Demo.java` - CLI demonstration
- `Week5Tests.java` - Unit tests
- `README.md` - Complete documentation
- `QUICKSTART.txt` - Quick reference
- `CLASS_DIAGRAM.txt` - Class diagram
- `DELIVERABLES.md` - This file
- `compile.bat` - Compilation script
- `run-demo.bat` - Demo script
- `run-tests.bat` - Test script

### Modified Files (from Week 4)
- `SimpleProduct.java` - Now implements Priced
- `LineItem.java` - Uses Priced interface

### Copied Files (unchanged from Week 4)
- `Product.java`
- `Money.java`
- `Order.java`
- `OrderIds.java`
- `Catalog.java`
- `InMemoryCatalog.java`
- `OrderObserver.java`
- `OrderPublisher.java`
- `payment/PaymentStrategy.java`
- `payment/CardPayment.java`
- `payment/CashPayment.java`
- `payment/WalletPayment.java`

## Verification Steps

1. ✅ Code compiles without errors
2. ✅ Demo runs and produces expected output
3. ✅ All 8 tests pass
4. ✅ Decorator pattern correctly implemented
5. ✅ Factory pattern correctly implemented
6. ✅ OCP preserved (can add decorators without modifying existing code)
7. ✅ Factory vs manual construction produces identical results
8. ✅ Decorated prices calculated correctly
9. ✅ Documentation complete and thorough

## Notes for Week 6 Assessment

- Week 5 lab session assessed Week 4 work (Observer Pattern)
- Week 5 Decorator + Factory work will be assessed in Week 6
- All deliverables are complete and ready for assessment
- Code follows Open/Closed Principle throughout
- Comprehensive tests demonstrate all functionality
- Class diagram documents complete system architecture

---

**Status**: ✅ All deliverables complete and tested
**Ready for Assessment**: Yes
**Patterns Implemented**: Decorator, Factory, Strategy, Observer
**Java Version**: Java 21 (uses pattern matching, switch expressions)

