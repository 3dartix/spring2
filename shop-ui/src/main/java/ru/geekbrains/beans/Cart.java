package ru.geekbrains.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import ru.geekbrains.entity.OrderItem;
import ru.geekbrains.entity.Product;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Data
@CommonsLog
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    private List<OrderItem> items;
    private BigDecimal price;

    @NotBlank(message = "Заполните поле телефон")
    private String phone;

    @NotBlank (message = "Заполните поле адреса доставки")
    private String address;

    @PostConstruct
    public void init() {
        items = new ArrayList<>();
    }

    public void add(Product product) {
        for (OrderItem item : items) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + 1);
                item.setPrice(new BigDecimal(item.getQuantity() * item.getProduct().getPrice().doubleValue()));
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
            log.info("removeById: " + items.get(i).getProduct().getId() + " :: " + productId);
            if (items.get(i).getProduct().getId().equals(productId)) {
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

    public void clear(){
        items.clear();
        phone = "";
        address = "";
        recalculate();
    }
}
