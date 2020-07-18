package ru.geekbrains.service;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.geekbrains.entity.Product;
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
    private PictureRepository pictureRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, PictureRepository pictureRepository) {
        this.productRepository = productRepository;
        this.pictureRepository = pictureRepository;
    }

    @Transactional(readOnly = true)
    public List<Product> findAll (){
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Product> findById(Long id) {
        return productRepository.findProductsById(id);
    }

    @Transactional(readOnly = true)
    public Page<Product> filterByPrice(String category, List<String> brands, Integer minPrice, Integer maxPrice, Pageable pageable) {
        Specification<Product> specification = ProductSpecification.trueLiteral();

        log.info("minPrice:" + minPrice + " maxPrice:" + maxPrice + " category:" + category);
        if(category != null) {
            specification = specification.and(ProductSpecification.constainCategory(category));
        }

        if(brands != null) {
            for (String brand:brands) {
                specification = specification.and(ProductSpecification.constainBrand(brand));
            }
        }

        if(minPrice != null) {
            specification = specification.and(ProductSpecification.priceGreaterThanEqual(new BigDecimal(minPrice)));
        }

        if(maxPrice != null){
            specification = specification.and(ProductSpecification.priceLessThanEqual(new BigDecimal(maxPrice)));
        }

        return productRepository.findAll(specification, pageable);
    }
    
}
