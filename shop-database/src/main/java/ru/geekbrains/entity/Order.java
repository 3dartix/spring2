package ru.geekbrains.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;

    @Column(name = "price")
    private BigDecimal price;

//    @Column(name = "address")
//    private String address;

//    public Order(User user, Cart cart) {
//        this.user = user;
//        this.price = cart.getPrice();
//        this.items = new ArrayList<>();
//        for (OrderItem i : cart.getItems()) {
//            i.setOrder(this);
//            this.items.add(i);
//        }
//        cart.clear();
//    }
}
