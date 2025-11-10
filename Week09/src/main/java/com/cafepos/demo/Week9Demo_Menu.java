package com.cafepos.demo;

import com.cafepos.menu.*;
import com.cafepos.common.Money;

public final class Week9Demo_Menu {
    public static void main(String[] args) {
        Menu rootMenu = new Menu("CAFÉ MENU");

        Menu drinksMenu = new Menu("Drinks");
        Menu coffeeMenu = new Menu("Coffee");
        coffeeMenu.add(new MenuItem("Espresso", Money.of(2.50), true));
        coffeeMenu.add(new MenuItem("Latte (Large)", Money.of(3.90), true));
        coffeeMenu.add(new MenuItem("Cappuccino", Money.of(3.00), true));
        drinksMenu.add(coffeeMenu);
        rootMenu.add(drinksMenu);

        Menu dessertsMenu = new Menu("Desserts");
        dessertsMenu.add(new MenuItem("Chocolate Cake", Money.of(4.50), true));
        dessertsMenu.add(new MenuItem("Cheesecake", Money.of(4.00), false));
        dessertsMenu.add(new MenuItem("Fruit Salad", Money.of(3.50), true));
        rootMenu.add(dessertsMenu);

        System.out.println("Full Menu");
        rootMenu.print();
        System.out.println();

        System.out.println("Vegetarian Options");
        for (MenuItem item : rootMenu.vegetarianItems()) {
            item.print();
        }
    }
}


