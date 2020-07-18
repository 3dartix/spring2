package ru.geekbrains.controller;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.beans.Cart;
import ru.geekbrains.beans.Filter;
import ru.geekbrains.repo.BrandRepository;
import ru.geekbrains.service.CategoryService;
import ru.geekbrains.service.ProductService;

import javax.validation.Valid;
import javax.ws.rs.QueryParam;
import java.math.BigDecimal;
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

    public int page = 1;
    public int pageSize = 5;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService, BrandRepository brandRepository, Cart cart, Filter filter) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.brandRepository = brandRepository;
        this.cart = cart;
        this.filter = filter;
    }

    //    @GetMapping ("product")
//    public String productPage(Model model){
//        model.addAttribute("namePage", "shop");
//        model.addAttribute("products", productService.findAll());
//        model.addAttribute("categories", categoryService.findAll());
//        model.addAttribute("brands", brandRepository.findAll());
//        model.addAttribute("cart", cart);
//        return "shop";
//    }

    @PostMapping
    public String productListByFilter(@ModelAttribute("filter") Filter filter, BindingResult bindingResult, Model model) {

        log.info(this.filter.toString());

        model.addAttribute("productsPage",
                productService.filterByPrice(
                        this.filter.getCategory(),
                        null,
                        filter.getMinPrice().orElse(null),
                        filter.getMaxPrice().orElse(null),
                        PageRequest.of(page-1, pageSize)
                ));

        model.addAttribute("minPrice", filter.getMinPrice().orElse(10));
        model.addAttribute("maxPrice", filter.getMaxPrice().orElse(1000));

        model.addAttribute("filter", this.filter);

        model.addAttribute("namePage", "shop");
        model.addAttribute("products", productService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("brands", brandRepository.findAll());
        model.addAttribute("cart", cart);
        return "shop";
    }



    @GetMapping
    public String productList(Model model,
                              @RequestParam(name = "brands") Optional<List<String>> brands,
//                              @RequestParam(name = "category") Optional<String> category,
                              @RequestParam(name = "minPrice") Optional<Integer> minPrice,
                              @RequestParam(name = "maxPrice") Optional<Integer> maxPrice
//                              @RequestParam(name = "page") Optional<Integer> page,
//                              @RequestParam(name = "pageSize") Optional<Integer> pageSize
                              ){

        log.info(brands);

//        filter.setCategory(category.orElse(null));
//        this.page = page.orElse(1);
//        this.pageSize = pageSize.orElse(5);


        model.addAttribute("productsPage",
                productService.filterByPrice(
                        filter.getCategory(),
                        null,
                        minPrice.orElse(10),
                        maxPrice.orElse(5000),
                        PageRequest.of(this.page - 1, this.pageSize)
                ));


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



//    @GetMapping
//    public String productList(Model model,
//                              @RequestParam(value = "minPrice") Optional<BigDecimal> minPrice,
//                              @RequestParam(value = "maxPrice") Optional<BigDecimal> maxPrice,
//                              @RequestParam(name = "productName") Optional<String> productName,
//                              @RequestParam(name = "productCategory") Optional<String> productCategory,
//                              @RequestParam(name = "page") Optional<Integer> page,
//                              @RequestParam(name = "pageSize") Optional<Integer> pageSize) {
//        log.info("Product list with minPrice="+minPrice+" and maxPrice="+maxPrice);
//
//        model.addAttribute("productsPage",
//                productService.filterBy(
//                        minPrice.orElse(null),
//                        maxPrice.orElse(null),
//                        productName.orElse(""),
//                        productCategory.orElse(null),
//                        PageRequest.of(page.orElse(1) - 1, pageSize.orElse(5))
//                ));
//        model.addAttribute("minPrice", minPrice.orElse(null));
//        model.addAttribute("maxPrice", maxPrice.orElse(null));
//        model.addAttribute("productName", productName.orElse(""));
////        log.info("Controller -> productList :" + productService.findById(0));
//        model.addAttribute("currentPage", page.orElse(1));
////        model.addAttribute("products", productService.findAll());
//
//
//
//        model.addAttribute("namePage", "shop");
//        model.addAttribute("products", productService.findAll());
//        model.addAttribute("categories", categoryService.findAll());
//        model.addAttribute("brands", brandRepository.findAll());
//        model.addAttribute("cart", cart);
//        return "shop";
//    }

}
