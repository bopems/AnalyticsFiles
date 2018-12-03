package com.bopems.analytics.entity;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class SellTest {

    @Test
    public void setItems() {

        List<Item> items = new ArrayList<>();

        Item item1 = new Item();
        item1.setId("1");
        item1.setQuantity(Float.parseFloat("10"));
        item1.setPrice(Float.parseFloat("100"));
        items.add(item1);

        Item item2 = new Item();
        item2.setId("2");
        item2.setQuantity(Float.parseFloat("30"));
        item2.setPrice(Float.parseFloat("2.50"));
        items.add(item2);

        Item item3 = new Item();
        item3.setId("3");
        item3.setQuantity(Float.parseFloat("40"));
        item3.setPrice(Float.parseFloat("3.10"));
        items.add(item3);

        Sell sell = new Sell();
        sell.setItems("[1-10-100,2-30-2.50,3-40-3.10]");

        Assert.assertEquals(sell.getItems().get(0).getId(), items.get(0).getId());
        Assert.assertEquals(Float.compare(sell.getItems().get(0).getPrice(), items.get(0).getPrice()), 0);
        Assert.assertEquals(Float.compare(sell.getItems().get(0).getQuantity(), items.get(0).getQuantity()), 0);
        Assert.assertEquals(sell.getItems().get(1).getId(), items.get(1).getId());
        Assert.assertEquals(Float.compare(sell.getItems().get(1).getPrice(), items.get(1).getPrice()), 0);
        Assert.assertEquals(Float.compare(sell.getItems().get(1).getQuantity(), items.get(1).getQuantity()), 0);
        Assert.assertEquals(sell.getItems().get(2).getId(), items.get(2).getId());
        Assert.assertEquals(Float.compare(sell.getItems().get(2).getPrice(), items.get(2).getPrice()), 0);
        Assert.assertEquals(Float.compare(sell.getItems().get(2).getQuantity(), items.get(2).getQuantity()), 0);
    }
}
