package com.cafepos;

import com.cafepos.menu.Menu;
import com.cafepos.menu.MenuItem;
import com.cafepos.factory.ProductFactory;
import com.cafepos.common.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class Week9IntegrationTest {

    @Test
    public void selectMenuItemByName_createsProductViaFactory_verifiesTotals() {
        Menu root = new Menu("CAFÉ MENU");
        Menu drinks = new Menu("Drinks");
        Menu coffee = new Menu("Coffee");
        coffee.add(new MenuItem("Espresso", Money.of(2.50), true));
        drinks.add(coffee);
        root.add(drinks);

        MenuItem espressoItem = root.vegetarianItems().stream()
            .filter(item -> item.name().equals("Espresso"))
            .findFirst()
            .orElse(null);

        Assertions.assertNotNull(espressoItem);
        Assertions.assertEquals(Money.of(2.50), espressoItem.price());

        ProductFactory factory = new ProductFactory();
        var product = factory.create("ESP");
        Assertions.assertEquals("Espresso", product.name());
        Assertions.assertEquals(Money.of(2.50), product.basePrice());

        Money lineTotal = espressoItem.price().multiply(2);
        Money productTotal = product.basePrice().multiply(2);
        Assertions.assertEquals(lineTotal, productTotal);
    }
}

