import java.math.*;

public final class Money implements Comparable<Money> {
    private final BigDecimal amount;

    public static Money of(double value) {
        if (value < 0)
            throw new IllegalArgumentException("amount cannot be negative");
        return new Money(BigDecimal.valueOf(value));
    }

    public static Money zero() {
        return new Money(BigDecimal.ZERO);
    }

    private Money(BigDecimal a) {
        if (a == null)
            throw new IllegalArgumentException("amount is required");
        if (a.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("amount cannot be negative");
        this.amount = a.setScale(2, RoundingMode.HALF_UP);
    }

    public Money add(Money other) {
        if (other == null)
            throw new IllegalArgumentException("other money cannot be null");
        return new Money(this.amount.add(other.amount));
    }

    public Money multiply(int qty) {
        if (qty < 0)
            throw new IllegalArgumentException("quantity cannot be negative");
        return new Money(this.amount.multiply(BigDecimal.valueOf(qty)));
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Money money = (Money) obj;
        return amount.equals(money.amount);
    }

    @Override
    public int hashCode() {
        return amount.hashCode();
    }

    @Override
    public String toString() {
        return "$" + amount.toString();
    }

    @Override
    public int compareTo(Money other) {
        if (other == null)
            throw new IllegalArgumentException("other money cannot be null");
        return this.amount.compareTo(other.amount);
    }
}

