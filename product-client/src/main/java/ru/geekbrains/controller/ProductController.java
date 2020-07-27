package ru.geekbrains.controller;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.geekbrains.beans.Filter;
import ru.geekbrains.entity.Product;
import ru.geekbrains.sevrice.ProductService;
import ru.geekbrains.utils.RestResponsePage;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

@CommonsLog
@RestController
public class ProductController {

    private final ProductService productService;
    public final Filter filter;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
        this.filter = new Filter();
        filter.init();
    }

    @GetMapping ("/product")
    public RestResponsePage productList(
            @RequestParam(name = "brands") Optional<List<String>> brands,
            @RequestParam(name = "category") Optional<String> category,
            @RequestParam(name = "name") Optional<String> name,
            @RequestParam(name = "minPrice") Optional<Integer> minPrice,
            @RequestParam(name = "maxPrice") Optional<Integer> maxPrice,
            @RequestParam(name = "page") Optional<Integer> numberPage,
            @RequestParam(name = "pageSize") Optional<Integer> pageSize
    ) {

        if(category.isPresent()){
            filter.setCategory(category.orElse(null));
        }

        if(brands.isPresent()){
            filter.setBrands(brands.orElse(null));
        }

        if(minPrice.isPresent()){
            filter.setMinPrice(minPrice);
        }

        if(maxPrice.isPresent()){
            filter.setMaxPrice(maxPrice);
        }

        if(name.isPresent()){
            filter.setName(name);
        }

        Page<Product> page = productService.filterByPrice(
                filter.getCategory(),
                filter.getBrands(),
                filter.getName().orElse(""),
                filter.getMinPrice().orElse(10),
                filter.getMaxPrice().orElse(5000),
                PageRequest.of(numberPage.orElse(1) - 1, pageSize.orElse(5))
        );

        RestResponsePage respPage = new RestResponsePage(page.getContent(), page.getPageable(), page.getTotalElements());

        log.info(page.getContent().size());
//        for (Product p: page.getContent()) {
//            log.info(p);
//        }

        return respPage;
    }


}
