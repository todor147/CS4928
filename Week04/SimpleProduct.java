public final class SimpleProduct implements Product {
    private final String id;
    private final String name;
    private final Money basePrice;

    public SimpleProduct(String id, String name, Money basePrice) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("id cannot be null or empty");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("name cannot be null or empty");
        }
        if (basePrice == null) {
            throw new IllegalArgumentException("basePrice cannot be null");
        }
        if (basePrice.getAmount().compareTo(Money.zero().getAmount()) < 0) {
            throw new IllegalArgumentException("basePrice cannot be negative");
        }

        this.id = id;
        this.name = name;
        this.basePrice = basePrice;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Money basePrice() {
        return basePrice;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        SimpleProduct that = (SimpleProduct) obj;
        return id.equals(that.id) && name.equals(that.name) && basePrice.equals(that.basePrice);
    }

    @Override
    public int hashCode() {
        return id.hashCode() + name.hashCode() + basePrice.hashCode();
    }

    @Override
    public String toString() {
        return "SimpleProduct{id='" + id + "', name='" + name + "', basePrice=" + basePrice + "}";
    }
}
