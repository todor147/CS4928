# Week 6 Lab: Refactoring Café POS

## Overview
This lab demonstrates the refactoring of a "God Class" (`OrderManagerGod`) into a clean, maintainable design while preserving identical external behavior. The refactoring eliminates multiple code smells and applies SOLID principles.

## Code Smells Identified and Removed

### 1. God Class (OrderManagerGod)
- **Location**: `smells/OrderManagerGod.java` - entire class
- **Smell**: Single class doing too much (product creation, pricing, tax calculation, receipt formatting, payment handling)
- **Refactoring**: Extract Class → Split into focused components

### 2. Long Method (process method)
- **Location**: `OrderManagerGod.process()` - 150+ lines
- **Smell**: Method doing multiple responsibilities
- **Refactoring**: Extract Method → Split into `CheckoutService.checkout()`

### 3. Primitive Obsession
- **Location**: Discount codes as strings (`"LOYALTY10"`, `"SUMMER20"`, `"COUPON5"`)
- **Smell**: Using primitives instead of proper types
- **Refactoring**: Replace Conditional with Polymorphism → `DiscountPolicy` interface

### 4. Duplicated Logic
- **Location**: BigDecimal math operations, percentage calculations
- **Smell**: Same calculation logic repeated
- **Refactoring**: Extract Method → Centralized in `PricingService`

### 5. Feature Envy
- **Location**: Discount and tax rules embedded in main class
- **Smell**: Class using data from other classes excessively
- **Refactoring**: Move Method → `DiscountPolicy` and `TaxPolicy` classes

### 6. Shotgun Surgery
- **Location**: Adding new discount types or payment methods
- **Smell**: Changes require modifying multiple places
- **Refactoring**: Replace Conditional with Polymorphism → Strategy pattern

### 7. Global State
- **Location**: `LAST_DISCOUNT_CODE`, `TAX_PERCENT` static variables
- **Smell**: Shared mutable state across instances
- **Refactoring**: Remove Global State → Dependency Injection

## Refactoring Techniques Applied

### 1. Extract Class
- **From**: Monolithic `OrderManagerGod`
- **To**: `CheckoutService`, `PricingService`, `ReceiptPrinter`, `DiscountPolicy`, `TaxPolicy`
- **Benefit**: Single Responsibility Principle

### 2. Extract Method
- **From**: Inline product creation, pricing calculations
- **To**: Separate methods in appropriate classes
- **Benefit**: Improved readability and testability

### 3. Replace Conditional with Polymorphism
- **From**: String-based discount code switches
- **To**: `DiscountPolicy` interface with implementations
- **Benefit**: Open/Closed Principle - easy to add new discount types

### 4. Dependency Injection
- **From**: Hardcoded dependencies and global state
- **To**: Constructor injection of dependencies
- **Benefit**: Dependency Inversion Principle, testability

### 5. Remove Global State
- **From**: Static `LAST_DISCOUNT_CODE`, `TAX_PERCENT`
- **To**: Injected dependencies
- **Benefit**: Eliminates shared mutable state

## SOLID Principles Satisfied

### Single Responsibility Principle (SRP)
- `CheckoutService`: Orchestrates checkout workflow
- `PricingService`: Handles pricing calculations
- `ReceiptPrinter`: Formats receipts
- `DiscountPolicy`: Manages discount logic
- `TaxPolicy`: Handles tax calculations

### Open/Closed Principle (OCP)
- New discount types: Create new `DiscountPolicy` implementation
- New payment methods: Create new `ReceiptPaymentStrategy` implementation
- No modification of existing classes required

### Liskov Substitution Principle (LSP)
- All `DiscountPolicy` implementations are interchangeable
- All `TaxPolicy` implementations are interchangeable
- All `ReceiptPaymentStrategy` implementations are interchangeable

### Interface Segregation Principle (ISP)
- Focused interfaces: `DiscountPolicy`, `TaxPolicy`, `ReceiptPaymentStrategy`
- Clients depend only on methods they use

### Dependency Inversion Principle (DIP)
- High-level modules depend on abstractions (`DiscountPolicy`, `TaxPolicy`)
- Low-level modules implement these abstractions
- Dependencies injected through constructors

## Final Architecture

```
CheckoutService
    ↓ orchestrates
ProductFactory → builds products
    ↓
PricingService → uses DiscountPolicy + TaxPolicy
    ↓
ReceiptPrinter → formats output
    ↓
ReceiptPaymentStrategy → handles payment display
```

## Adding New Discount Types

To add a new discount type (e.g., "STUDENT15"):

1. Create new `StudentDiscount` class implementing `DiscountPolicy`
2. Add logic to `getDiscountPolicy()` method in `Week6Demo`
3. **No changes needed** to existing classes

This demonstrates the power of the Strategy pattern and Open/Closed Principle.

## Benefits Achieved

1. **Maintainability**: Each class has a single, clear responsibility
2. **Testability**: Components can be tested in isolation
3. **Extensibility**: New features can be added without modifying existing code
4. **Readability**: Code is self-documenting with clear class names
5. **Flexibility**: Dependencies can be easily swapped for different behaviors

## Verification

- **Characterization Tests**: All 12 tests pass, proving behavior preservation
- **Unit Tests**: Individual components tested in isolation
- **Demo**: Side-by-side comparison shows identical output
- **Commit History**: Incremental refactoring with meaningful messages

The refactored code eliminates all identified code smells while maintaining 100% behavioral compatibility with the original implementation.
