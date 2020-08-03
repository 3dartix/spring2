package ru.geekbrains.repo;

import org.springframework.data.jpa.domain.Specification;
import ru.geekbrains.entity.Brand;
import ru.geekbrains.entity.Product;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;

public class ProductSpecification {
    public static Specification<Product> trueLiteral(){
        return (root, query, builder) -> builder.isTrue(builder.literal(true));
    }

    public static Specification<Product> constainCategory (String category){
        return (root, query, builder) -> {
//            query.distinct(true);
            return builder.equal(root.joinList("categories").get("name"), category);
        };
    }

    public static Specification<Product> constainBrand (String brand){
        return (Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
//            query.distinct(true);
            return builder.equal(root.get("brand").get("name"), brand);
        };
    }

    public static Specification<Product> categoryIs(String brand){
        return (root, query, builder) -> {
            //builder.isMember если мы спросим у нашего продукта brands, то этот брэнд должен быть в этом списке
//            return builder.isMember(brand, root.get("brand"));
            return builder.equal(root.get("brand").get("name"), brand);
        };
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
