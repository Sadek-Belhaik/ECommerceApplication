package com.example.demo.controllers;


import com.example.demo.model.persistence.Item;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(Parameterized.class)
public class ItemControllerParametrizedTest {

    private String input;
    private String output;

    public ItemControllerParametrizedTest(String input, String output) {
        super();
        this.input = input;
        this.output =output;
    }


    @Parameters
    public static Collection initData(){
        Item item = new Item();
        item.setName("Item name");

        Item item1 = new Item();
        item1.setName("Item 1 name");

        Item item2 = new Item();
        item2.setName("Item 2 name");

        String itemsName[][] = {{"Item name",item.getName()}, {"Item 1 name", item1.getName()}, {"Item 2 name", item2.getName()}};

        return Arrays.asList(itemsName);
    }

    @Test
    public void verify_items(){
        Assert.assertEquals(input, output);
    }

    @Test
    public void verify_get_item(){
        Item item = new Item();
        item.setId(1l);
        item.setName("Item name");
        item.setPrice(BigDecimal.valueOf(2.99));
        item.setDescription("Item description");

        assertEquals(java.util.Optional.of(1l), java.util.Optional.of(item.getId()));
        assertEquals(BigDecimal.valueOf(2.99), (item.getPrice()));
        assertNotNull("Item name", item.getName());
        assertEquals("Item description", item.getDescription());

    }
}
