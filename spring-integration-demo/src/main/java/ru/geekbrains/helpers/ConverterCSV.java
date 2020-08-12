package ru.geekbrains.helpers;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.geekbrains.entity.Brand;
import ru.geekbrains.entity.Category;
import ru.geekbrains.entity.Product;
import ru.geekbrains.repo.BrandRepository;
import ru.geekbrains.repo.CategoryRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@CommonsLog
public class ConverterCSV {
    public static ConverterCSV CONVERT;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private BrandRepository brandRepository;

    public ConverterCSV() {
        CONVERT = this;
    }

    public Product toProduct(String line, String separator){
        if(line == null || line.length() < 1){
            new ConvertProductExeption("value should not be null.");
        }

        List<String> arr = Stream.of(line.split(separator))
                .map(String::trim)
                .collect(Collectors.toList());


        Product product = new Product();

        for (int i=0; i < arr.size(); i++) {
            //имя
            if(i == 0){
                product.setName(arr.get(i));
            }
            //описание
            if(i == 1){
                product.setDescription(arr.get(i));
            }
            //цена
            if(i == 2){
                product.setPrice(new BigDecimal(arr.get(i).replace(",",".").replaceAll(" ", "")));
            }
            //категории
            if(i == 3){
                if(arr.get(i).length() > 1){
                    Stream.of(arr.get(i).split(","))
                            .map(String::trim)
                    .map(s -> {
                        Category category = findOrCreateCategory(s, product);
                        product.getCategories().add(category);
                        return null;
                    });
                }
            }
            //бренды
            if(i == 4){
                if(arr.get(i).length() > 1){
                    product.setBrand(findOrCreateBrand(arr.get(i), product));
                }
            }
        }
        return product;
    }

    private Category findOrCreateCategory(String name, Product product){
        Category category = categoryRepository.findByName(name);
        if(category == null) {
            //если нет категории то создаем
            category = new Category();
            category.setName(name);

            List<Product> products = new ArrayList<>();
            products.add(product);

            category.setProducts(products);
            categoryRepository.save(category);
            return category;
        } else {
            //если есть категория то возвращаем
            return category;
        }
    }

    private Brand findOrCreateBrand(String name, Product product){
        Brand brand = brandRepository.findBrandByName(name).orElse(null);
        if(brand == null) {
            //если нет категории то создаем
            brand = new Brand();
            brand.setName(name);

            List<Product> products = new ArrayList<>();
            products.add(product);

            brand.setProducts(products);
            brandRepository.save(brand);
            return brand;
        } else {
            //если есть категория то возвращаем
            return brand;
        }
    }

}
