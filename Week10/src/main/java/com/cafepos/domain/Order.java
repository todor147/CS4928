package com.cafepos.domain;

import java.util.*;
import java.math.BigDecimal;
import com.cafepos.common.Money;

public final class Order {
    private final long id;
    private final List<LineItem> items = new ArrayList<>();

    public Order(long id) {
        this.id = id;
    }

    public long id() {
        return id;
    }

    public List<LineItem> items() {
        return List.copyOf(items);
    }

    public void addItem(LineItem li) {
        if (li == null)
            throw new IllegalArgumentException("line item cannot be null");
        if (li.quantity() <= 0)
            throw new IllegalArgumentException("quantity must be > 0");
        items.add(li);
    }

    public void removeLastItem() {
        if (!items.isEmpty())
            items.remove(items.size() - 1);
    }

    public Money subtotal() {
        return items.stream()
                .map(LineItem::lineTotal)
                .reduce(Money.zero(), Money::add);
    }

    public Money taxAtPercent(int percent) {
        if (percent < 0)
            throw new IllegalArgumentException("tax percent cannot be negative");
        Money subtotal = subtotal();
        if (subtotal.equals(Money.zero())) {
            return Money.zero();
        }
        var bd = subtotal().asBigDecimal()
                .multiply(BigDecimal.valueOf(percent))
                .divide(BigDecimal.valueOf(100));
        return Money.of(bd);
    }

    public Money totalWithTax(int percent) {
        return subtotal().add(taxAtPercent(percent));
    }
}

