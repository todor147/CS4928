package com.cafepos.menu;

import com.cafepos.common.Money;
import java.util.Iterator;

public abstract class MenuComponent {
    public String name() {
        throw new UnsupportedOperationException();
    }

    public Money price() {
        throw new UnsupportedOperationException();
    }

    public boolean vegetarian() {
        throw new UnsupportedOperationException();
    }

    public void add(MenuComponent component) {
        throw new UnsupportedOperationException();
    }

    public void remove(MenuComponent component) {
        throw new UnsupportedOperationException();
    }

    public MenuComponent getChild(int index) {
        throw new UnsupportedOperationException();
    }

    public Iterator<MenuComponent> iterator() {
        throw new UnsupportedOperationException();
    }

    public Iterator<MenuComponent> childrenIterator() {
        throw new UnsupportedOperationException();
    }

    public void print() {
        throw new UnsupportedOperationException();
    }
}
