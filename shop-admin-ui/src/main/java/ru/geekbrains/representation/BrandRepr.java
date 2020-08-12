package ru.geekbrains.representation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.geekbrains.entity.Brand;
import ru.geekbrains.entity.Product;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandRepr implements Serializable {

    private Long id;

    private String name;

    private List<Product> products;

    public BrandRepr(Brand brand) {
        this.id = brand.getId();
        this.name = brand.getName();
        this.products = brand.getProducts();
    }

//    public BrandRepr() {
//    }
//
//    public BrandRepr(Long id, String name, List<Product> products) {
//        this.id = id;
//        this.name = name;
//        this.products = products;
//    }
}
