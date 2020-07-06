package ru.geekbrains.specification;


import org.springframework.data.jpa.domain.Specification;
import ru.geekbrains.entity.Product;

import java.math.BigDecimal;

public class ProductSpecification {
    public static Specification<Product> trueLiteral(){
        return (root, query, builder) -> builder.isTrue(builder.literal(true));
    }
    public static Specification<Product> priceGreaterThanEqual(BigDecimal price){
        return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("price"), price);
    }
    public static Specification<Product> priceLessThanEqual(BigDecimal price){
        return (root, query, builder) -> builder.lessThanOrEqualTo(root.get("price"), price);
    }

    public static Specification<Product> productName(String name){
        return (root, query, builder) -> builder.like(root.get("name"), "%" + name + "%");
    }
}
