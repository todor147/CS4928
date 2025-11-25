package com.cafepos.decorator;

import com.cafepos.catalog.Product;
import com.cafepos.common.Money;

public final class OatMilk extends ProductDecorator {
    public OatMilk(Product product) {
        super(product);
    }

    @Override
    public Money price() {
        return basePrice().add(Money.of(0.50));
    }

    @Override
    public String name() {
        return product.name() + " + Oat Milk";
    }
}

