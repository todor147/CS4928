package com.cafepos.menu;

import java.util.Iterator;
import java.util.Stack;

public final class CompositeIterator implements Iterator<MenuComponent> {
    private final Stack<Iterator<MenuComponent>> stack = new Stack<>();

    public CompositeIterator(Iterator<MenuComponent> iterator) {
        if (iterator == null)
            throw new IllegalArgumentException("iterator required");
        stack.push(iterator);
    }

    @Override
    public boolean hasNext() {
        if (stack.isEmpty()) {
            return false;
        }
        Iterator<MenuComponent> iterator = stack.peek();
        if (iterator.hasNext()) {
            return true;
        }
        stack.pop();
        return hasNext();
    }

    @Override
    public MenuComponent next() {
        if (!hasNext()) {
            throw new java.util.NoSuchElementException();
        }
        Iterator<MenuComponent> iterator = stack.peek();
        MenuComponent component = iterator.next();
        if (component instanceof Menu menu) {
            stack.push(menu.childrenIterator());
        }
        return component;
    }
}


