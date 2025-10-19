# Week 6: Café POS Refactoring Lab

## Overview
This lab demonstrates the complete refactoring of a "smelly" `OrderManagerGod` class into a clean, testable, and extensible design while preserving identical external behavior. The refactoring eliminates code smells and applies SOLID principles through systematic step-by-step improvements.

## Lab Completion Status ✅

### ✅ Part 1: Characterization Tests
- **File**: `src/test/java/com/cafepos/Week6CharacterizationTests.java`
- **Purpose**: Lock in the behavior of the smelly `OrderManagerGod` before refactoring
- **Tests**: 3 characterization tests covering no discount, loyalty discount, and coupon scenarios
- **Status**: All tests pass ✅

### ✅ Part 2: Code Smell Identification
- **File**: `src/main/java/com/cafepos/smells/OrderManagerGod.java` (commit 9a34736)
- **Smells Identified**:
  - **Global/Static State**: `TAX_PERCENT` and `LAST_DISCOUNT_CODE` static fields
  - **Long Method/God Class**: `process()` method doing too many things
  - **Primitive Obsession**: Raw strings for discount codes and payment types
  - **Duplicated Logic**: Repeated BigDecimal math for discounts and taxes
  - **Feature Envy**: Discount and tax calculation logic embedded inline
  - **Shotgun Surgery Risk**: Changing rules requires editing the main method
- **Status**: All smells marked with inline comments ✅

### ✅ Part 3: Step-by-Step Refactoring
**Step 1**: Extract DiscountPolicy interface and implementations
- `DiscountPolicy`, `NoDiscount`, `LoyaltyPercentDiscount`, `FixedCouponDiscount`
- **Commit**: `Step 1: Extract DiscountPolicy interface and implementations`

**Step 2**: Extract TaxPolicy interface and implementation
- `TaxPolicy`, `FixedRateTaxPolicy`
- **Commit**: `Step 2: Extract TaxPolicy interface and FixedRateTaxPolicy`

**Step 3**: Extract ReceiptPrinter class
- `ReceiptPrinter` for receipt formatting
- **Commit**: `Step 3: Extract ReceiptPrinter class for receipt formatting`

**Step 4**: Replace payment string switch with polymorphism
- `ReceiptPaymentStrategy`, `ReceiptCashPayment`, `ReceiptCardPayment`, `ReceiptWalletPayment`
- **Commit**: `Step 4: Replace payment string switch with PaymentStrategy polymorphism`

**Step 5**: Inject dependencies via constructors
- `CheckoutService` with dependency injection
- **Commit**: `Step 5: Inject all dependencies via constructors`

**Step 6**: Remove global state
- Eliminated `TAX_PERCENT` and `LAST_DISCOUNT_CODE` static fields
- **Commit**: `Step 6: Remove global state and complete refactoring`

### ✅ Part 4: CheckoutService Orchestration
- **File**: `src/main/java/com/cafepos/checkout/CheckoutService.java`
- **Purpose**: Orchestrates all components without containing business logic
- **Dependencies**: ProductFactory, PricingService, ReceiptPrinter, PaymentStrategy
- **Status**: All tests pass ✅

### ✅ Part 5: Unit Tests for Extracted Components
- **Files**: 
  - `src/test/java/com/cafepos/pricing/DiscountPolicyTests.java`
  - `src/test/java/com/cafepos/pricing/TaxPolicyTests.java`
  - `src/test/java/com/cafepos/pricing/PricingServiceTests.java`
- **Coverage**: All discount policies, tax policy, and pricing service coordination
- **Status**: All 12 unit tests pass ✅

### ✅ Part 6: Interactive Demo
- **File**: `src/main/java/com/cafepos/demo/Week6Demo.java`
- **Purpose**: 30-second proof that old and new flows produce identical receipts
- **Status**: Demo works perfectly, shows `Match: true` ✅

## Final Architecture

### Class Responsibilities
```
CheckoutService orchestrates 
  → ProductFactory builds products
  → PricingService uses DiscountPolicy and TaxPolicy
  → ReceiptPrinter formats receipts
  → PaymentStrategy handles payment I/O
```

### SOLID Principles Applied
- **Single Responsibility**: Each class has one reason to change
- **Open/Closed**: New discount types can be added without modifying existing code
- **Liskov Substitution**: All implementations properly substitute their interfaces
- **Interface Segregation**: Focused interfaces for specific responsibilities
- **Dependency Inversion**: High-level modules depend on abstractions, not concretions

### Code Smells Eliminated
- ✅ **God Class**: Responsibilities distributed across focused classes
- ✅ **Long Method**: Logic extracted into specialized methods
- ✅ **Primitive Obsession**: Proper types replace raw strings
- ✅ **Duplicated Logic**: Common functionality extracted to reusable components
- ✅ **Feature Envy**: Business logic moved to appropriate domain classes
- ✅ **Shotgun Surgery**: Changes now isolated to specific classes
- ✅ **Global State**: All dependencies injected, no shared mutable state

## Test Results
- **Characterization Tests**: 3/3 ✅
- **Unit Tests**: 12/12 ✅
- **Total**: 15/15 tests pass ✅
- **Demo**: Perfect match between old and new receipts ✅

## Commit History
All refactoring steps are preserved in git with descriptive commit messages:
- `Step 1: Extract DiscountPolicy interface and implementations`
- `Step 2: Extract TaxPolicy interface and FixedRateTaxPolicy`
- `Step 3: Extract ReceiptPrinter class for receipt formatting`
- `Step 4: Replace payment string switch with PaymentStrategy polymorphism`
- `Step 5: Inject all dependencies via constructors`
- `Step 6: Remove global state and complete refactoring`
- `Part 4: Create proper CheckoutService for orchestration`
- `Part 5: Add comprehensive unit tests for extracted components`
- `Part 6: Create Week6Demo for 30-second proof of behavior preservation`

## How to Run
```bash
# Run all tests
java -cp "src/main/java;src/test/java" com.cafepos.Week6TestRunner

# Run interactive demo
java -cp "src/main/java" com.cafepos.demo.Week6Demo
```

## Conclusion
The refactoring successfully transformed a monolithic, hard-to-maintain class into a clean, testable, and extensible architecture while preserving identical external behavior. The new design follows SOLID principles and eliminates all identified code smells, making the codebase ready for future enhancements.
