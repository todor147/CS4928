package com.cafepos.decorator;

import com.cafepos.catalog.Product;

public abstract class ProductDecorator implements Product, Priced {
    protected final Product base;

    protected ProductDecorator(Product base) {
        this.base = base;
    }

    @Override
    public String id() {
        return base.id();
    }

    @Override
    public String name() {
        return base.name();
    }

    @Override
    public com.cafepos.common.Money basePrice() {
        return base.basePrice();
    }
}
