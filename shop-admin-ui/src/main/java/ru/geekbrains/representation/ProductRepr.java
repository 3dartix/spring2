package ru.geekbrains.representation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import ru.geekbrains.entity.Category;
import ru.geekbrains.entity.Product;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

//@NoArgsConstructor
//@AllArgsConstructor
@Data
public class ProductRepr implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Заполните название продукта")
    private String name;

    @NotNull(message = "Заполните цену продукта")
    private BigDecimal price;

    @NotBlank (message = "Заполните описание продукта")
    private String description;

    @NotNull(message = "Выберите хотя бы одну категорию")
    @Size(min = 1, message = "Выберите хотя бы одну категорию")
    private List<Category> categories;

    @NotNull(message = "Выберите брэнд")
    private BrandRepr brand;

    private List<PictureRepr> pictures;

    private MultipartFile[] newPictures;

    public ProductRepr(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.categories = product.getCategories();
        this.brand = new BrandRepr(product.getBrand());

        this.pictures = product.getPictures().stream()
                .map(PictureRepr::new)
                .collect(Collectors.toList());
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

    public boolean containsCategory(Category category){
        if(categories != null){
            return categories.contains(category);
        }
        return false;
    }


    public boolean containsBrand(BrandRepr brand){
        if(this.brand != null){
            return this.brand.equals(brand);
        }
        return false;
    }

    @Override
    public String toString() {
        return "ProductRepr{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                '}';
    }

    public ProductRepr(String name, BigDecimal price,  String description, List<Category> categories, BrandRepr brand, List<PictureRepr> pictures, MultipartFile[] newPictures) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.categories = categories;
        this.brand = brand;
        this.pictures = pictures;
        this.newPictures = newPictures;
    }

    public ProductRepr() {
    }
}
