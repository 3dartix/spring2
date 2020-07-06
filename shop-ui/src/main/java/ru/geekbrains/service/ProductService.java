package ru.geekbrains.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.geekbrains.entity.Product;
import ru.geekbrains.repo.PictureRepository;
import ru.geekbrains.repo.ProductRepository;

import java.util.List;
import java.util.Optional;


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
}
