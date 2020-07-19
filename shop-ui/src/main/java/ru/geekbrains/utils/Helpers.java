package ru.geekbrains.utils;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.geekbrains.entity.Comment;
import ru.geekbrains.entity.Order;
import ru.geekbrains.entity.OrderItem;
import ru.geekbrains.entity.Product;
import ru.geekbrains.repo.OrderRepository;
import ru.geekbrains.repo.UserRepository;

import java.util.List;

@CommonsLog
@Component
public class Helpers {

    public static Helpers HELPERS;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;


    @Autowired
    public Helpers(OrderRepository orderRepository, UserRepository userRepository) {
        HELPERS = this;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    public boolean isCommentAllow(String username, Product product){
        //если комментарий уже оставлен
        for (Comment com:product.getComments()) {
            if(com.getUser().getUsername().equals(username)){
                return false;
            }
        }
        
        //если товар приобретен
        List<Order> orders = orderRepository.findOrdersByUserID(userRepository.findByUsername(username).get().getId());
        log.info("orders ::" + orders);
        if(orders != null){
            for (Order order: orders) {
                for (OrderItem item: order.getItems()) {
                    log.info(item.getProduct() + " :: " + product);
                    if (item.getProduct().equals(product)){
                        log.info("продукт был куплем пользователем");
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
