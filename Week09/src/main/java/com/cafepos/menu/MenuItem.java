package com.cafepos.menu;

import com.cafepos.common.Money;
import java.util.Iterator;
import java.util.Collections;

public final class MenuItem extends MenuComponent {
    private final String name;
    private final Money price;
    private final boolean vegetarian;

    public MenuItem(String name, Money price, boolean vegetarian) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("name required");
        if (price == null)
            throw new IllegalArgumentException("price required");
        this.name = name;
        this.price = price;
        this.vegetarian = vegetarian;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Money price() {
        return price;
    }

    @Override
    public boolean vegetarian() {
        return vegetarian;
    }

    @Override
    public Iterator<MenuComponent> iterator() {
        return Collections.<MenuComponent>singleton(this).iterator();
    }

    @Override
    public void print() {
        String veg = vegetarian ? " (v)" : "";
        System.out.println("  " + name + ", " + price + " EUR" + veg);
    }
}


