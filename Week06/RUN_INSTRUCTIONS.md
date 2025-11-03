# Week06 Run Instructions

## Running Week6Demo

### Option 1: Using Java directly (if already compiled)

If the classes are already in the `target/classes` directory, you can run:

```bash
cd Week06

# Run the demo
java -cp "target/classes;src/main/java" com.cafepos.demo.Week6Demo

# Or on Unix/Linux/Mac:
java -cp "target/classes:src/main/java" com.cafepos.demo.Week6Demo
```

### Option 2: Compile and Run with javac

```bash
cd Week06

# Compile the demo
javac -cp "src/main/java" -d target/classes src/main/java/com/cafepos/demo/Week6Demo.java

# Run the demo
java -cp "target/classes;src/main/java" com.cafepos.demo.Week6Demo
```

### Option 3: Compile all sources and run

```bash
cd Week06

# Compile all Java files in src/main/java
find src/main/java -name "*.java" | xargs javac -cp "src/main/java" -d target/classes

# Run the demo
java -cp "target/classes" com.cafepos.demo.Week6Demo
```

### Option 4: Using Maven (if Maven is installed)

```bash
cd Week06

# Clean and compile
mvn clean compile

# Run the demo
mvn exec:java -Dexec.mainClass="com.cafepos.demo.Week6Demo" -DskipTests
```

## Week6Demo Interactive Menu

The demo provides an interactive menu with the following options:

1. **Create New Order** - Start a new order
2. **Add Product to Order** - Add products (ESP, LAT, CAP, AME)
3. **Add Extras to Order** - Add extras (SHOT, OAT, L, SYRUP)
4. **Apply Discount** - Apply discounts (LOYAL5, COUPON1, NONE)
5. **View Order** - View current order and pricing
6. **Process Payment** - Process payment (CASH, CARD, WALLET)
7. **Compare Old vs New** - Compare old smelly code vs new refactored code
8. **Exit** - Exit the demo

## Running Tests

### Using Maven
```bash
cd Week06
mvn test
```

### Using Java directly
```bash
cd Week06

# Run test runner
java -cp "target/test-classes;target/classes;path/to/junit/jar" com.cafepos.Week6TestRunner
```

## Example Usage Flow

1. Start the demo
2. Press 1 to create a new order
3. Press 2 to add products (e.g., ESP)
4. Press 3 to add extras (e.g., SHOT, OAT)
5. Press 4 to apply discount (e.g., LOYAL5)
6. Press 5 to view the order
7. Press 6 to process payment
8. Press 7 to compare old vs new (characterization test)
9. Press 8 to exit

## Available Products

- **ESP** - Espresso ($2.50)
- **LAT** - Latte ($3.20)
- **CAP** - Cappuccino ($3.00)
- **AME** - Americano ($2.80)

## Available Extras

- **SHOT** - Extra Shot (+$0.80)
- **OAT** - Oat Milk (+$0.50)
- **L** - Large Size (+$0.70)
- **SYRUP** - Syrup (+$0.30)

## Available Discounts

- **LOYAL5** - 5% Loyalty Discount
- **COUPON1** - $1.00 Fixed Coupon
- **NONE** - No Discount

## Available Payment Methods

- **CASH** - Cash Payment
- **CARD** - Card Payment (requires card number)
- **WALLET** - Wallet Payment (requires wallet ID)

## Recipe Examples

- `ESP+SHOT+OAT` - Espresso with extra shot and oat milk
- `LAT+L` - Large Latte
- `CAP+SHOT+SYRUP` - Cappuccino with extra shot and syrup





