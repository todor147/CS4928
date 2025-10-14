package decorator;

public abstract class ProductDecorator implements Product, Priced {
    protected final Product base;

    protected ProductDecorator(Product base) {
        if (base == null)
            throw new IllegalArgumentException("base product required");
        this.base = base;
    }

    @Override
    public String id() {
        return base.id();
    }

    @Override
    public Money basePrice() {
        return base.basePrice();
    }

    // Concrete decorators will override name() and price()
}

