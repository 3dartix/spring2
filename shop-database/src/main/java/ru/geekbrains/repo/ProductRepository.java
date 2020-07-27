package ru.geekbrains.repo;

import ru.geekbrains.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

//А если нужна собственная реализация метода, то как ее написать правильно?
// JpaSpecificationExecutor<Product> - для Specification<Product> формирование запроса в зависимости от параметров
@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Page<Product> findProductsByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    Page<Product> findByPriceLessThanEqual(BigDecimal bigDecimal, Pageable pageable);
    Page<Product> findByPriceGreaterThanEqual(BigDecimal bigDecimal, Pageable pageable);

    @Query("select p from Product p where name like %:name%")
    Page<Product> findProductsByName(@Param("name") String name, Pageable pageable);

    @Query("select p from Product p where name like %:name% and price between :minPrice and :maxPrice")
    Page<Product> findProductsByNameAndPrice(@Param("name") String name, @Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice, Pageable pageable);

    Optional<Product> findProductsById(Long id);

    void deleteProductById(Long id);
}
