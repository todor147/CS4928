public final class LineItem {
    private final Product product;
    private final int quantity;

    public LineItem(Product product, int quantity) {
        if (product == null)
            throw new IllegalArgumentException("product required");
        if (quantity <= 0)
            throw new IllegalArgumentException("quantity must be > 0");
        this.product = product;
        this.quantity = quantity;
    }

    public Product product() {
        return product;
    }

    public int quantity() {
        return quantity;
    }

    public Money lineTotal() {
        Money unit = (product instanceof Priced p) ? p.price() : product.basePrice();
        return unit.multiply(quantity);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        LineItem lineItem = (LineItem) obj;
        return quantity == lineItem.quantity && product.equals(lineItem.product);
    }

    @Override
    public int hashCode() {
        return product.hashCode() + quantity;
    }

    @Override
    public String toString() {
        return "LineItem{product=" + product + ", quantity=" + quantity + "}";
    }
}

