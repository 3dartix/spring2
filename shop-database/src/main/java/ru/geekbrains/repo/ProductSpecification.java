package ru.geekbrains.repo;

import org.springframework.data.jpa.domain.Specification;
import ru.geekbrains.entity.Product;

import javax.persistence.criteria.CriteriaBuilder;
import java.math.BigDecimal;
import java.util.List;

public class ProductSpecification {
    public static Specification<Product> trueLiteral(){
        return (root, query, builder) -> builder.isTrue(builder.literal(true));
    }

    public static Specification<Product> constainCategory (String category){
        return (root, query, builder) -> builder.equal(root.joinList("categories").get("name"), category);
    }

    public static Specification<Product> constainBrand (String brand){
        return (root, query, builder) -> builder.equal(root.get("brand").get("name"), brand);
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
