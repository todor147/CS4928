package com.cafepos.decorator;

import com.cafepos.common.Money;
import com.cafepos.catalog.Product;

public final class OatMilk extends ProductDecorator {
    private static final Money SURCHARGE = Money.of(0.60);

    public OatMilk(Product base) {
        super(base);
    }

    @Override
    public String name() {
        return base.name() + " + Oat Milk";
    }

    @Override
    public Money price() {
        return (base instanceof Priced p ? p.price() : base.basePrice()).add(SURCHARGE);
    }
}
