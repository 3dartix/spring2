package ru.geekbrains.controller;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.beans.Cart;
import ru.geekbrains.beans.Filter;
import ru.geekbrains.entity.Product;
import ru.geekbrains.repo.BrandRepository;
import ru.geekbrains.service.CategoryService;
import ru.geekbrains.service.ProductService;
import java.util.List;
import java.util.Optional;

@RequestMapping("/product")
@CommonsLog
@Controller
public class ProductController {

    private final ProductService productService;
    public final CategoryService categoryService;
    public final BrandRepository brandRepository;
    public final Cart cart;
    public final Filter filter;


    @Autowired
    public ProductController(ProductService productService,
                             CategoryService categoryService,
                             BrandRepository brandRepository,
                             Cart cart,
                             Filter filter) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.brandRepository = brandRepository;
        this.cart = cart;
        this.filter = filter;
    }


    @GetMapping
    public String productList(Model model,
                              @RequestParam(name = "brands") Optional<List<String>> brands,
                              @RequestParam(name = "category") Optional<String> category,
                              @RequestParam(name = "name") Optional<String> name,
                              @RequestParam(name = "minPrice") Optional<Integer> minPrice,
                              @RequestParam(name = "maxPrice") Optional<Integer> maxPrice,
                              @RequestParam(name = "page") Optional<Integer> numberPage,
                              @RequestParam(name = "pageSize") Optional<Integer> pageSize
                              ){

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

        model.addAttribute("productsPage", page);

        model.addAttribute("minPrice", filter.getMinPrice().orElse(10));
        model.addAttribute("maxPrice", filter.getMaxPrice().orElse(1000));

        model.addAttribute("filter", filter);

        model.addAttribute("namePage", "shop");
        model.addAttribute("products", productService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("brands", brandRepository.findAll());
        model.addAttribute("cart", cart);
        return "shop";
    }


}
