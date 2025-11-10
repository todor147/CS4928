package com.cafepos.menu;

import com.cafepos.common.Money;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class Menu extends MenuComponent implements Iterable<MenuComponent> {
    private final String name;
    private final List<MenuComponent> children = new ArrayList<>();

    public Menu(String name) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("name required");
        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public void add(MenuComponent component) {
        if (component == null)
            throw new IllegalArgumentException("component required");
        children.add(component);
    }

    @Override
    public void remove(MenuComponent component) {
        children.remove(component);
    }

    @Override
    public MenuComponent getChild(int index) {
        if (index < 0 || index >= children.size())
            throw new IndexOutOfBoundsException("index out of bounds");
        return children.get(index);
    }

    @Override
    public Iterator<MenuComponent> childrenIterator() {
        return children.iterator();
    }

    @Override
    public Iterator<MenuComponent> iterator() {
        return new CompositeIterator(children.iterator());
    }

    @Override
    public void print() {
        System.out.println(name);
        for (MenuComponent component : children) {
            component.print();
        }
    }

    public List<MenuComponent> getAllComponents() {
        List<MenuComponent> result = new ArrayList<>();
        for (MenuComponent component : this) {
            result.add(component);
        }
        return result;
    }

    public List<MenuItem> vegetarianItems() {
        List<MenuItem> result = new ArrayList<>();
        for (MenuComponent component : this) {
            if (component instanceof MenuItem item && item.vegetarian()) {
                result.add(item);
            }
        }
        return result;
    }
}


