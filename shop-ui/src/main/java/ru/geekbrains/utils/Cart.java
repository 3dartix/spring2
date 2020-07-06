package ru.geekbrains.utils;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import ru.geekbrains.entity.OrderItem;
import ru.geekbrains.entity.Product;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Data
public class Cart {
    private List<OrderItem> items;
    private BigDecimal price;

    @PostConstruct
    public void init() {
        items = new ArrayList<>();
    }

    public void add(Product product) {
        for (OrderItem i : items) {
            if (i.getProduct().getId().equals(product.getId())) {
                i.setQuantity(i.getQuantity() + 1);
                i.setPrice(new BigDecimal(i.getQuantity() * i.getProduct().getPrice().doubleValue()));
                recalculate();
                return;
            }
        }
        items.add(new OrderItem(product));
        recalculate();
    }

    public void subtract(Product product) {
        for (OrderItem i : items) {
            if (i.getProduct().getId().equals(product.getId())) {
                if(i.getQuantity() > 0){
                    i.setQuantity(i.getQuantity() - 1);
                    i.setPrice(new BigDecimal(i.getQuantity() * i.getProduct().getPrice().doubleValue()));
                    recalculate();
                }
                return;
            }
        }
        recalculate();
    }

    public void removeById(Long productId) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId().equals(productId)) {
                items.remove(i);
                recalculate();
                return;
            }
        }
    }

    public void recalculate() {
        price = new BigDecimal(0.0);
        for (OrderItem i : items) {
            price = price.add(i.getPrice());
        }
    }
}
