package ru.geekbrains.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.geekbrains.entity.Product;
import ru.geekbrains.helpers.ExpectedEntity;
import ru.geekbrains.repo.ProductRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

public class ProductServiceTest {
    private ProductService productService;
    private ProductRepository productRepository;
    private ExpectedEntity expectedEntity;

    @BeforeEach
    public void init(){
        productRepository = mock(ProductRepository.class);
        productService = new ProductService();
        productService.setProductRepository(productRepository);
        expectedEntity = new ExpectedEntity();
    }

    @Test
    public void findByIdTest(){
        when(productRepository.findProductsById(eq(expectedEntity.getExpectedProduct().getId())))
                .thenReturn(Optional.of(expectedEntity.getExpectedProduct()));

        Optional<Product> product = productService.findById(expectedEntity.getExpectedProduct().getId());
        assertTrue(product.isPresent());
        assertEquals(expectedEntity.getExpectedProduct().getId(), product.get().getId());
        assertEquals(expectedEntity.getExpectedProduct().getName(), product.get().getName());
    }
}
