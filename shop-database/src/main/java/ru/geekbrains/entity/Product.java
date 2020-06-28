package ru.geekbrains.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank (message = "Заполните название продукта")
    @Column(length = 150)
    private String name;

    @NotBlank (message = "Заполните описание продукта")
    @Column
    private String description;

    @NotNull (message = "Заполните цену продукта")
    @Column
    private Float price;


    @NotNull(message = "Выберите хотя бы одну категорию")
    @Size(min = 1, message = "Выберите хотя бы одну категорию")
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "product_category",
            joinColumns = @JoinColumn(name="product_id"),
            inverseJoinColumns = @JoinColumn(name="category_id"))
    private List<Category> categories;

    public boolean contains(Category category){
        if(categories != null){
            return categories.contains(category);
        }
        return false;
    }

    public String getCategoriesLine() {
        String result = new String();
        if(categories != null) {
            for (Category category : categories) {
                result = result + ", " + category.getName();
            }
        }
        result = result.replaceFirst(", ", "");
        return result;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'';
    }
}
