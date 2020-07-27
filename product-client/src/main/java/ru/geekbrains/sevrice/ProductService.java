package ru.geekbrains.sevrice;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.geekbrains.entity.Product;
import ru.geekbrains.repo.BrandRepository;
import ru.geekbrains.repo.PictureRepository;
import ru.geekbrains.repo.ProductRepository;
import ru.geekbrains.repo.ProductSpecification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@CommonsLog
@Service
public class ProductService {
    private ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public Page<Product> findAll (Pageable page){
        return productRepository.findAll(page);
    }

    @Transactional(readOnly = true)
    public Optional<Product> findById(Long id) {
        return productRepository.findProductsById(id);
    }

    @Transactional(readOnly = true)
    public Page<Product> filterByPrice(String category, List<String> brands, String name, Integer minPrice, Integer maxPrice, Pageable pageable) {
        log.info("начинаем сборку");
        Specification<Product> specification = ProductSpecification.trueLiteral();

        log.info("minPrice:" + minPrice + " maxPrice:" + maxPrice + " category:" + category + "brands" + brands);
        if(category != null) {
            specification = specification.and(ProductSpecification.constainCategory(category));
        }

        if(brands != null && !brands.isEmpty()) {
            log.info("Specification brands Included");
            Specification<Product> specBrand = null;
            for (String brand:brands) {
                if(specBrand == null){
                    specBrand = ProductSpecification.categoryIs(brand);
                } else {
                    specBrand = specBrand.or(ProductSpecification.categoryIs(brand));
                }
            }

            specification = specification.and(specBrand);
        }

        if(name != null && name != "") {
            specification = specification.and(ProductSpecification.productName(name));
        }

        if(minPrice != null) {
            specification = specification.and(ProductSpecification.priceGreaterThanEqual(new BigDecimal(minPrice)));
        }

        if(maxPrice != null){
            specification = specification.and(ProductSpecification.priceLessThanEqual(new BigDecimal(maxPrice)));
        }

        return productRepository.findAll(specification, pageable);
    }

    public void save(Product product) {
        productRepository.save(product);
    }
}
