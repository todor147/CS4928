package com.cafepos.menu;

import com.cafepos.common.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class MenuTests {

    @Test
    public void menuItem_constructor_validatesNameNotNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new MenuItem(null, Money.of(2.50), true);
        });
    }

    @Test
    public void menuItem_constructor_validatesNameNotBlank() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new MenuItem("", Money.of(2.50), true);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new MenuItem("   ", Money.of(2.50), true);
        });
    }

    @Test
    public void menuItem_constructor_validatesPriceNotNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new MenuItem("Espresso", null, true);
        });
    }

    @Test
    public void menuItem_hasNamePriceAndVegetarian() {
        MenuItem item = new MenuItem("Espresso", Money.of(2.50), true);
        Assertions.assertEquals("Espresso", item.name());
        Assertions.assertEquals(Money.of(2.50), item.price());
        Assertions.assertTrue(item.vegetarian());

        MenuItem nonVeg = new MenuItem("Cheesecake", Money.of(4.00), false);
        Assertions.assertEquals("Cheesecake", nonVeg.name());
        Assertions.assertEquals(Money.of(4.00), nonVeg.price());
        Assertions.assertFalse(nonVeg.vegetarian());
    }

    @Test
    public void menuItem_iterator_returnsIteratorWithSelf() {
        MenuItem item = new MenuItem("Espresso", Money.of(2.50), true);
        Iterator<MenuComponent> iterator = item.iterator();
        Assertions.assertTrue(iterator.hasNext());
        Assertions.assertEquals(item, iterator.next());
        Assertions.assertFalse(iterator.hasNext());
    }

    @Test
    public void menuItem_print_outputsCorrectFormat() {
        MenuItem item = new MenuItem("Espresso", Money.of(2.50), true);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(out));

        item.print();

        System.setOut(originalOut);
        String output = out.toString();
        Assertions.assertTrue(output.contains("Espresso"));
        Assertions.assertTrue(output.contains("2.50"));
        Assertions.assertTrue(output.contains("(v)"));
    }

    @Test
    public void menuItem_print_nonVegetarianNoVegMarker() {
        MenuItem item = new MenuItem("Cheesecake", Money.of(4.00), false);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(out));

        item.print();

        System.setOut(originalOut);
        String output = out.toString();
        Assertions.assertTrue(output.contains("Cheesecake"));
        Assertions.assertFalse(output.contains("(v)"));
    }

    @Test
    public void menuItem_throwsUnsupportedOperationForAdd() {
        MenuItem item = new MenuItem("Espresso", Money.of(2.50), true);
        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            item.add(new MenuItem("Latte", Money.of(3.20), true));
        });
    }

    @Test
    public void menuItem_throwsUnsupportedOperationForRemove() {
        MenuItem item = new MenuItem("Espresso", Money.of(2.50), true);
        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            item.remove(item);
        });
    }

    @Test
    public void menuItem_throwsUnsupportedOperationForGetChild() {
        MenuItem item = new MenuItem("Espresso", Money.of(2.50), true);
        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            item.getChild(0);
        });
    }

    @Test
    public void menuItem_throwsUnsupportedOperationForChildrenIterator() {
        MenuItem item = new MenuItem("Espresso", Money.of(2.50), true);
        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            item.childrenIterator();
        });
    }

    @Test
    public void menu_constructor_validatesNameNotNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Menu(null);
        });
    }

    @Test
    public void menu_constructor_validatesNameNotBlank() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Menu("");
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Menu("   ");
        });
    }

    @Test
    public void menu_returnsName() {
        Menu menu = new Menu("Drinks");
        Assertions.assertEquals("Drinks", menu.name());
    }

    @Test
    public void menu_add_validatesComponentNotNull() {
        Menu menu = new Menu("Drinks");
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            menu.add(null);
        });
    }

    @Test
    public void menu_canAddAndGetChildren() {
        Menu menu = new Menu("Drinks");
        MenuItem item1 = new MenuItem("Espresso", Money.of(2.50), true);
        MenuItem item2 = new MenuItem("Latte", Money.of(3.20), true);
        menu.add(item1);
        menu.add(item2);
        Assertions.assertEquals(item1, menu.getChild(0));
        Assertions.assertEquals(item2, menu.getChild(1));
    }

    @Test
    public void menu_canAddMenuAsChild() {
        Menu root = new Menu("CAFÉ MENU");
        Menu drinks = new Menu("Drinks");
        root.add(drinks);
        Assertions.assertEquals(drinks, root.getChild(0));
    }

    @Test
    public void menu_remove_removesComponent() {
        Menu menu = new Menu("Drinks");
        MenuItem item1 = new MenuItem("Espresso", Money.of(2.50), true);
        MenuItem item2 = new MenuItem("Latte", Money.of(3.20), true);
        menu.add(item1);
        menu.add(item2);
        menu.remove(item1);
        Assertions.assertEquals(item2, menu.getChild(0));
        Assertions.assertEquals(1, countChildren(menu));
    }

    @Test
    public void menu_getChild_validatesIndexBounds() {
        Menu menu = new Menu("Drinks");
        menu.add(new MenuItem("Espresso", Money.of(2.50), true));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
            menu.getChild(-1);
        });
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
            menu.getChild(1);
        });
    }

    @Test
    public void menu_childrenIterator_returnsDirectChildrenIterator() {
        Menu menu = new Menu("Drinks");
        MenuItem item1 = new MenuItem("Espresso", Money.of(2.50), true);
        MenuItem item2 = new MenuItem("Latte", Money.of(3.20), true);
        menu.add(item1);
        menu.add(item2);
        Iterator<MenuComponent> iterator = menu.childrenIterator();
        Assertions.assertTrue(iterator.hasNext());
        Assertions.assertEquals(item1, iterator.next());
        Assertions.assertEquals(item2, iterator.next());
        Assertions.assertFalse(iterator.hasNext());
    }

    @Test
    public void menu_iterator_returnsCompositeIterator() {
        Menu menu = new Menu("Drinks");
        menu.add(new MenuItem("Espresso", Money.of(2.50), true));
        Iterator<MenuComponent> iterator = menu.iterator();
        Assertions.assertNotNull(iterator);
        Assertions.assertTrue(iterator instanceof CompositeIterator || iterator.hasNext());
    }

    @Test
    public void compositeIterator_traversesDepthFirst() {
        Menu root = new Menu("CAFÉ MENU");
        Menu drinks = new Menu("Drinks");
        Menu coffee = new Menu("Coffee");
        coffee.add(new MenuItem("Espresso", Money.of(2.50), true));
        coffee.add(new MenuItem("Latte", Money.of(3.20), true));
        drinks.add(coffee);
        root.add(drinks);

        List<String> names = new ArrayList<>();
        for (MenuComponent component : root) {
            names.add(component.name());
        }

        Assertions.assertEquals("Drinks", names.get(0));
        Assertions.assertEquals("Coffee", names.get(1));
        Assertions.assertEquals("Espresso", names.get(2));
        Assertions.assertEquals("Latte", names.get(3));
        Assertions.assertEquals(4, names.size());
    }

    @Test
    public void compositeIterator_traversesComplexNestedStructure() {
        Menu root = new Menu("CAFÉ MENU");
        Menu drinks = new Menu("Drinks");
        Menu coffee = new Menu("Coffee");
        Menu tea = new Menu("Tea");
        coffee.add(new MenuItem("Espresso", Money.of(2.50), true));
        coffee.add(new MenuItem("Latte", Money.of(3.20), true));
        tea.add(new MenuItem("Green Tea", Money.of(2.00), true));
        drinks.add(coffee);
        drinks.add(tea);
        Menu desserts = new Menu("Desserts");
        desserts.add(new MenuItem("Cake", Money.of(4.50), true));
        root.add(drinks);
        root.add(desserts);

        List<String> names = new ArrayList<>();
        for (MenuComponent component : root) {
            names.add(component.name());
        }

        Assertions.assertEquals("Drinks", names.get(0));
        Assertions.assertEquals("Coffee", names.get(1));
        Assertions.assertEquals("Espresso", names.get(2));
        Assertions.assertEquals("Latte", names.get(3));
        Assertions.assertEquals("Tea", names.get(4));
        Assertions.assertEquals("Green Tea", names.get(5));
        Assertions.assertEquals("Desserts", names.get(6));
        Assertions.assertEquals("Cake", names.get(7));
    }

    @Test
    public void compositeIterator_handlesEmptyMenu() {
        Menu menu = new Menu("Empty Menu");
        List<MenuComponent> components = new ArrayList<>();
        for (MenuComponent component : menu) {
            components.add(component);
        }
        Assertions.assertTrue(components.isEmpty());
    }

    @Test
    public void compositeIterator_handlesSingleItem() {
        Menu menu = new Menu("Drinks");
        MenuItem item = new MenuItem("Espresso", Money.of(2.50), true);
        menu.add(item);
        List<MenuComponent> components = new ArrayList<>();
        for (MenuComponent component : menu) {
            components.add(component);
        }
        Assertions.assertEquals(1, components.size());
        Assertions.assertEquals(item, components.get(0));
    }

    @Test
    public void compositeIterator_constructor_validatesIteratorNotNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new CompositeIterator(null);
        });
    }

    @Test
    public void compositeIterator_next_throwsWhenNoMoreElements() {
        Menu menu = new Menu("Drinks");
        Iterator<MenuComponent> iterator = menu.iterator();
        Assertions.assertFalse(iterator.hasNext());
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            iterator.next();
        });
    }

    @Test
    public void menu_print_outputsNameAndChildren() {
        Menu menu = new Menu("Drinks");
        menu.add(new MenuItem("Espresso", Money.of(2.50), true));
        menu.add(new MenuItem("Latte", Money.of(3.20), true));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(out));

        menu.print();

        System.setOut(originalOut);
        String output = out.toString();
        Assertions.assertTrue(output.contains("Drinks"));
        Assertions.assertTrue(output.contains("Espresso"));
        Assertions.assertTrue(output.contains("Latte"));
    }

    @Test
    public void menu_print_outputsNestedStructure() {
        Menu root = new Menu("CAFÉ MENU");
        Menu drinks = new Menu("Drinks");
        drinks.add(new MenuItem("Espresso", Money.of(2.50), true));
        root.add(drinks);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(out));

        root.print();

        System.setOut(originalOut);
        String output = out.toString();
        Assertions.assertTrue(output.contains("CAFÉ MENU"));
        Assertions.assertTrue(output.contains("Drinks"));
        Assertions.assertTrue(output.contains("Espresso"));
    }

    @Test
    public void menu_getAllComponents_returnsAllInTraversalOrder() {
        Menu root = new Menu("CAFÉ MENU");
        Menu drinks = new Menu("Drinks");
        drinks.add(new MenuItem("Espresso", Money.of(2.50), true));
        root.add(drinks);

        List<MenuComponent> all = root.getAllComponents();
        Assertions.assertEquals(2, all.size());
        Assertions.assertEquals("Drinks", all.get(0).name());
        Assertions.assertEquals("Espresso", all.get(1).name());
    }

    @Test
    public void menu_getAllComponents_returnsEmptyListForEmptyMenu() {
        Menu menu = new Menu("Empty Menu");
        List<MenuComponent> all = menu.getAllComponents();
        Assertions.assertTrue(all.isEmpty());
    }

    @Test
    public void menu_vegetarianItems_returnsOnlyVegetarianMenuItems() {
        Menu root = new Menu("CAFÉ MENU");
        Menu desserts = new Menu("Desserts");
        desserts.add(new MenuItem("Chocolate Cake", Money.of(4.50), true));
        desserts.add(new MenuItem("Cheesecake", Money.of(4.00), false));
        desserts.add(new MenuItem("Fruit Salad", Money.of(3.50), true));
        root.add(desserts);

        List<MenuItem> vegItems = root.vegetarianItems();
        Assertions.assertEquals(2, vegItems.size());
        Assertions.assertTrue(vegItems.stream().allMatch(MenuItem::vegetarian));
        Assertions.assertTrue(vegItems.stream().anyMatch(item -> item.name().equals("Chocolate Cake")));
        Assertions.assertTrue(vegItems.stream().anyMatch(item -> item.name().equals("Fruit Salad")));
        Assertions.assertFalse(vegItems.stream().anyMatch(item -> item.name().equals("Cheesecake")));
    }

    @Test
    public void menu_vegetarianItems_returnsEmptyListWhenNoVegetarianItems() {
        Menu menu = new Menu("Meat Menu");
        menu.add(new MenuItem("Steak", Money.of(15.00), false));
        menu.add(new MenuItem("Chicken", Money.of(12.00), false));
        List<MenuItem> vegItems = menu.vegetarianItems();
        Assertions.assertTrue(vegItems.isEmpty());
    }

    @Test
    public void menu_vegetarianItems_filtersFromNestedMenus() {
        Menu root = new Menu("CAFÉ MENU");
        Menu drinks = new Menu("Drinks");
        Menu coffee = new Menu("Coffee");
        coffee.add(new MenuItem("Espresso", Money.of(2.50), true));
        coffee.add(new MenuItem("Latte", Money.of(3.20), true));
        drinks.add(coffee);
        Menu desserts = new Menu("Desserts");
        desserts.add(new MenuItem("Cake", Money.of(4.50), true));
        desserts.add(new MenuItem("Cheesecake", Money.of(4.00), false));
        root.add(drinks);
        root.add(desserts);

        List<MenuItem> vegItems = root.vegetarianItems();
        Assertions.assertEquals(3, vegItems.size());
        Assertions.assertTrue(vegItems.stream().anyMatch(item -> item.name().equals("Espresso")));
        Assertions.assertTrue(vegItems.stream().anyMatch(item -> item.name().equals("Latte")));
        Assertions.assertTrue(vegItems.stream().anyMatch(item -> item.name().equals("Cake")));
        Assertions.assertFalse(vegItems.stream().anyMatch(item -> item.name().equals("Cheesecake")));
    }

    @Test
    public void menu_throwsUnsupportedOperationForPrice() {
        Menu menu = new Menu("Drinks");
        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            menu.price();
        });
    }

    @Test
    public void menu_throwsUnsupportedOperationForVegetarian() {
        Menu menu = new Menu("Drinks");
        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            menu.vegetarian();
        });
    }

    private int countChildren(Menu menu) {
        int count = 0;
        Iterator<MenuComponent> iterator = menu.childrenIterator();
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        return count;
    }
}
