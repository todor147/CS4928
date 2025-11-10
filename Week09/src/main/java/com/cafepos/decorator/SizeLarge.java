package com.cafepos.decorator;

import com.cafepos.common.Money;
import com.cafepos.catalog.Product;

public final class SizeLarge extends ProductDecorator {
    private static final Money SURCHARGE = Money.of(1.00);

    public SizeLarge(Product base) {
        super(base);
    }

    @Override
    public String name() {
        return base.name() + " + Large";
    }

    @Override
    public Money price() {
        return (base instanceof Priced p ? p.price() : base.basePrice()).add(SURCHARGE);
    }
}
