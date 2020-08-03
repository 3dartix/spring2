package ru.geekbrains.controller;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.representation.ProductRepr;
import ru.geekbrains.service.BrandServiceImpl;
import ru.geekbrains.service.CategoryServiceImpl;
import ru.geekbrains.service.ProductServiceImpl;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Optional;

@RequestMapping("/product")
@CommonsLog
@Controller
public class ProductController {

    private final ProductServiceImpl productService;
    private final CategoryServiceImpl categoryServiceImpl;
    private final BrandServiceImpl brandService;

    @Autowired
    public ProductController(ProductServiceImpl productService, CategoryServiceImpl categoryServiceImpl, BrandServiceImpl brandService) {
        this.productService = productService;
        this.categoryServiceImpl = categoryServiceImpl;
        this.brandService = brandService;
    }


    @GetMapping
    public String productList(Model model,
                              @RequestParam(value = "minPrice") Optional<BigDecimal> minPrice,
                              @RequestParam(value = "maxPrice") Optional<BigDecimal> maxPrice,
                              @RequestParam(name = "productName") Optional<String> productName,
                              @RequestParam(name = "page") Optional<Integer> page,
                              @RequestParam(name = "pageSize") Optional<Integer> pageSize) {
        log.info("Product list with minPrice="+minPrice+" and maxPrice="+maxPrice);

        model.addAttribute("productsPage",
                productService.filterByPrice(
                        minPrice.orElse(null),
                        maxPrice.orElse(null),
                        productName.orElse(""),
                        PageRequest.of(page.orElse(1) - 1, pageSize.orElse(5))
        ));
        model.addAttribute("minPrice", minPrice.orElse(null));
        model.addAttribute("maxPrice", maxPrice.orElse(null));
        model.addAttribute("productName", productName.orElse(""));
//        log.info("Controller -> productList :" + productService.findById(0));
        model.addAttribute("currentPage", page.orElse(1));
//        model.addAttribute("products", productService.findAll());
        return "all-products";
    }

    //добавляет страницу /user/new
    @GetMapping("new")
    public String createProduct(Model model) {
        log.info("Create product form");
        //передаем в шаблонизатор html страницу user из view
        model.addAttribute("product", new ProductRepr());
        model.addAttribute("categories", categoryServiceImpl.findAll());
        model.addAttribute("brands", brandService.findAll());
        return "product";
    }

    @GetMapping("edit")
    public String createProduct(Model model,
                                @RequestParam(value = "idProduct") Optional<Long> idProduct) {
        log.info("Edit product form");

        //передаем в шаблонизатор html страницу user из view
        model.addAttribute("product", productService.findById(idProduct.orElse(-1L)).get());
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("categories", categoryServiceImpl.findAll());
        return "product";
    }

    @PostMapping
    public String saveProduct(@Valid @ModelAttribute("product") ProductRepr product, BindingResult bindingResult, Model model) {
        log.info("Save product method");

        log.info(product.toString());

        if(bindingResult.hasErrors()){
            log.info(bindingResult.getAllErrors());
            model.addAttribute("categories", categoryServiceImpl.findAll());
            model.addAttribute("brands", brandService.findAll());
            return "product";
        }

        log.info("NewPictures: ");

        try {
            log.info("Save - ok!");
            productService.save(product);
        } catch (Exception e){
            log.info("Save failed. Message: "+ e.getMessage());
        }

        return "redirect:/product";
    }

    @DeleteMapping
    public String deleteProduct(@RequestParam("id") Long id){
        log.info("Delete product with id:" + id);
        productService.deleteById(id);
        return "redirect:/product";
    }


}
