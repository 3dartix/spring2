package ru.geekbrains.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
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

    @Column(length = 150)
    private String name;

    @Column
    private BigDecimal price;

    @Column (columnDefinition = "TEXT")
//    @Lob
//    @Column(name="description", length=512)
    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "product_category",
            joinColumns = @JoinColumn(name="product_id"),
            inverseJoinColumns = @JoinColumn(name="category_id"))
    private List<Category> categories;

    @ManyToOne(optional = false)
    private Brand brand;

    @OneToMany(fetch = FetchType.LAZY, cascade= CascadeType.ALL)
    @JoinTable(name = "products_pictures",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "picture_id"))
    private List<Picture> pictures;

    //new
    @OneToMany(fetch = FetchType.LAZY, cascade= CascadeType.ALL)
    @JoinTable(name = "products_comments",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id"))
//    @JsonIgnore
    private List<Comment> comments;

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }

//    public Product(String name, String description, BigDecimal price, Brand brand, List<Category> categories, List<Picture> pictures) {
//        this.name = name;
//        this.description = description;
//        this.price = price;
//        this.brand = brand;
//        this.categories = categories;
//        this.pictures = pictures;
//    }
//
//    public Product() {
//    }

    public int calculateAverageRating (){
        int result = 0;
        for (Comment com : comments) {
            result += com.getRating();
        }
        if(result == 0) {
            return 1;
        } else {
            result = result/comments.size();
            return result;
        }
    }
}
