package ru.geekbrains.beans;

import lombok.extern.apachecommons.CommonsLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.geekbrains.entity.OrderItem;
import ru.geekbrains.helpers.ExpectedEntity;


import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@CommonsLog
public class CartTest {

    private Cart cart;
    private ExpectedEntity expectedEntity;

    @BeforeEach
    public void Init(){
        cart = new Cart();
        cart.init();
        expectedEntity = new ExpectedEntity();
    }

    @Test
    public void addProductTest(){
        //add firts product
        cart.add(expectedEntity.getExpectedProduct());
        List<OrderItem> items = cart.getItems();
        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals(1, items.get(0).getQuantity());
        assertEquals(expectedEntity.getExpectedProduct().getPrice(), cart.getPrice());

        //add second product
        cart.add(expectedEntity.getExpectedProduct());
        assertEquals(1, items.size());
        assertEquals(2, items.get(0).getQuantity());
        assertEquals(expectedEntity.getExpectedProduct().getPrice().add(expectedEntity.getExpectedProduct().getPrice()), cart.getPrice());
    }

    @Test
    public void subtractProductTest(){
        cart.add(expectedEntity.getExpectedProduct());
        cart.add(expectedEntity.getExpectedProduct());

        List<OrderItem> items = cart.getItems();
        assertNotNull(items);

        //subtract first product
        assertEquals(2, items.get(0).getQuantity());
        assertEquals(expectedEntity.getExpectedProduct().getPrice().add(expectedEntity.getExpectedProduct().getPrice()), cart.getPrice());

        cart.subtract(expectedEntity.getExpectedProduct());

        assertEquals(1, items.size());
        assertEquals(1, items.get(0).getQuantity());
        assertEquals(expectedEntity.getExpectedProduct().getPrice(), cart.getPrice());


        //subtract second product
        cart.subtract(expectedEntity.getExpectedProduct());

        assertEquals(1, items.size());
        assertEquals(0, items.get(0).getQuantity());
        assertEquals(new BigDecimal(0), cart.getPrice());
    }

    @Test
    public void clearTest(){
        cart.add(expectedEntity.getExpectedProduct());

        List<OrderItem> items = cart.getItems();
        assertNotNull(items);
        assertEquals(1, items.size());

        cart.clear();
        assertEquals(0, items.size());
    }

    @Test
    public void removeByIdTest(){
        cart.add(expectedEntity.getExpectedProduct());
        cart.add(expectedEntity.getExpectedProduct());

        List<OrderItem> items = cart.getItems();
        assertNotNull(items);
        assertEquals(1, items.size());

        cart.removeById(expectedEntity.getExpectedProduct().getId());
        assertEquals(0, items.size());
    }

}
