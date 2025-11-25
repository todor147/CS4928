package com.cafepos.decorator;

import com.cafepos.catalog.Product;
import com.cafepos.common.Money;

public abstract class ProductDecorator implements Product, Priced {
    protected final Product product;

    protected ProductDecorator(Product product) {
        this.product = product;
    }

    @Override
    public String id() {
        return product.id();
    }

    @Override
    public String name() {
        return product.name();
    }

    @Override
    public Money basePrice() {
        return product.basePrice();
    }
}

