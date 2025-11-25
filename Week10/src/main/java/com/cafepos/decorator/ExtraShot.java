package com.cafepos.decorator;

import com.cafepos.catalog.Product;
import com.cafepos.common.Money;

public final class ExtraShot extends ProductDecorator {
    public ExtraShot(Product product) {
        super(product);
    }

    @Override
    public Money price() {
        return basePrice().add(Money.of(0.80));
    }

    @Override
    public String name() {
        return product.name() + " + Extra Shot";
    }
}

