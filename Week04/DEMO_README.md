# Week 04 - Cafe POS CLI Demo

## Overview
This is an interactive command-line demonstration of the **Observer Pattern** implementation for a Cafe Point-of-Sale (POS) system.

## Features

### Observer Pattern Implementation
- **OrderPublisher**: Interface for managing observers
- **OrderObserver**: Interface for receiving order updates
- **Concrete Observers**:
  - `KitchenDisplay` - Receives notifications about items and payments
  - `DeliveryDesk` - Receives notifications when orders are ready
  - `CustomerNotifier` - Sends updates to customers about their orders

### Event Types
1. **itemAdded** - Triggered when an item is added to an order
2. **paid** - Triggered when payment is processed
3. **ready** - Triggered when order is marked as ready for delivery

### Payment Strategies
- Cash Payment
- Card Payment (with masked card number)
- Wallet Payment (available in code)

## How to Run

### Option 1: Using Batch Script (Windows)
```cmd
cd Week04
run-demo.bat
```

### Option 2: Using PowerShell Script
```powershell
cd Week04
.\run-demo.ps1
```

### Option 3: Manual Compilation and Execution
```cmd
cd Week04
javac -cp ".;payment" CafePOSDemo.java
java -cp ".;payment" CafePOSDemo
```

## Demo Menu Options

1. **Create New Order** - Creates a new order and registers all observers
2. **Add Item to Order** - Add products from the catalog to an order
3. **View Order Details** - Display order items, subtotal, tax, and total
4. **Pay for Order** - Process payment using Cash or Card
5. **Mark Order as Ready** - Signal that order is ready for delivery
6. **View All Orders** - List all orders in the system
7. **View Product Catalog** - Display available products and prices
8. **Demo: Observer Pattern** - Automated demonstration of the Observer pattern
9. **Exit** - Close the application

## Quick Start Guide

### Automated Demo (Recommended for first-time users)
1. Run the application
2. Select option **8** (Demo: Observer Pattern)
3. Watch how the Observer pattern works automatically

### Manual Demo
1. Select option **1** to create a new order (e.g., Order #1000)
2. Select option **7** to view the product catalog
3. Select option **2** to add items:
   - Enter Order ID: 1000
   - Enter Product ID: COFFEE
   - Enter quantity: 2
   - Notice the observer notifications!
4. Select option **4** to pay:
   - Enter Order ID: 1000
   - Choose payment method
   - Notice payment notifications!
5. Select option **5** to mark ready:
   - Enter Order ID: 1000
   - Notice delivery desk notification!

## Product Catalog

| ID | Product | Price |
|----|---------|-------|
| COFFEE | Coffee | $3.50 |
| TEA | Tea | $2.50 |
| LATTE | Latte | $4.50 |
| CAPPUCCINO | Cappuccino | $4.00 |
| ESPRESSO | Espresso | $2.75 |
| CROISSANT | Croissant | $3.00 |
| MUFFIN | Blueberry Muffin | $3.50 |
| SANDWICH | Ham & Cheese Sandwich | $6.50 |
| COOKIE | Chocolate Cookie | $2.00 |
| CAKE | Chocolate Cake Slice | $5.00 |

## Design Pattern Benefits

The Observer pattern provides:
- **Loose Coupling**: Order doesn't need to know implementation details of observers
- **Flexibility**: Easy to add/remove observers at runtime
- **Scalability**: New observer types can be added without modifying Order class
- **Separation of Concerns**: Each observer handles its specific responsibility

## Implementation Details

### Order Events Flow
```
Order.addItem() → notifyObservers("itemAdded")
  ↓
  → KitchenDisplay: "Order #X: item added"
  → CustomerNotifier: "Dear customer, your Order #X has been updated: itemAdded"

Order.pay() → notifyObservers("paid")
  ↓
  → KitchenDisplay: "Order #X: payment received"
  → CustomerNotifier: "Dear customer, your Order #X has been updated: paid"

Order.markReady() → notifyObservers("ready")
  ↓
  → DeliveryDesk: "Order #X is ready for delivery"
  → CustomerNotifier: "Dear customer, your Order #X has been updated: ready"
```

## Architecture

```
Order (implements OrderPublisher)
├── manages List<OrderObserver>
├── register(OrderObserver)
├── unregister(OrderObserver)
└── notifyObservers(Order, String)

OrderObserver (interface)
└── updated(Order, String eventType)

Concrete Observers:
├── KitchenDisplay
├── DeliveryDesk
└── CustomerNotifier
```

## Tax Calculation
- Default tax rate: 10%
- Applied to subtotal to calculate total

## Notes
- Order IDs are auto-generated starting from 1000
- All monetary values are displayed in $ format
- Observer notifications are printed to console in real-time


