package ru.geekbrains.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.geekbrains.entity.Order;
import ru.geekbrains.entity.OrderItem;
import ru.geekbrains.entity.User;
import ru.geekbrains.beans.Cart;

import java.util.ArrayList;
import java.util.List;

@Component
public class CartToOrderMapper {
    public static CartToOrderMapper MAPPER;

    @Autowired
    public CartToOrderMapper() {
        MAPPER = this;
    }

    public Order toOrder(User user, Cart cart){
        Order order = new Order();
        order.setPhone(cart.getPhone());
        order.setAddress(cart.getAddress());
        order.setUser(user);

        List<OrderItem> items = new ArrayList<>();

        for (OrderItem i : cart.getItems()) {
            i.setOrder(order);
            items.add(i);
        }

        order.setItems(items);
        order.setPrice(cart.getPrice());

        return order;
    }
}
