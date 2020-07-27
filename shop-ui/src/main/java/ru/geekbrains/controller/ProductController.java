package ru.geekbrains.controller;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.geekbrains.beans.Cart;
import ru.geekbrains.beans.Filter;
import ru.geekbrains.entity.Product;
import ru.geekbrains.repo.BrandRepository;
import ru.geekbrains.service.CategoryService;
import ru.geekbrains.service.ProductService;
import ru.geekbrains.utils.PageEntity;
import ru.geekbrains.utils.RestResponsePage;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

@RequestMapping("/product")
@CommonsLog
@Controller
public class ProductController {

    private final ProductService productService;
    public final CategoryService categoryService;
    public final BrandRepository brandRepository;
    public final Cart cart;
    public final Filter filter;


    @Value("${data.path.product-client}")
    public String productClientPath;

    private final RestTemplate restTemplate;

    @Autowired
    public ProductController(ProductService productService,
                             CategoryService categoryService,
                             BrandRepository brandRepository,
                             Cart cart,
                             Filter filter, RestTemplate restTemplate) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.brandRepository = brandRepository;
        this.cart = cart;
        this.filter = filter;
        this.restTemplate = restTemplate;
    }

    @GetMapping ("/reset")
    public String resetFilter(){
        filter.reset();
        return "redirect:/product";
    }


    //spring cloud
    @GetMapping
    public String productList(Model model,
                              @RequestParam(name = "brands") Optional<List<String>> brands,
                              @RequestParam(name = "category") Optional<String> category,
                              @RequestParam(name = "name") Optional<String> name,
                              @RequestParam(name = "minPrice") Optional<Integer> minPrice,
                              @RequestParam(name = "maxPrice") Optional<Integer> maxPrice,
                              @RequestParam(name = "page") Optional<Integer> numberPage,
                              @RequestParam(name = "pageSize") Optional<Integer> pageSize
    ) throws UnsupportedEncodingException {

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

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(productClientPath)
                .queryParam("name", encodeValue(filter.getName().orElse("")))
                .queryParam("minPrice", filter.getMinPrice().get())
                .queryParam("maxPrice", filter.getMaxPrice().get())
                .queryParam("page", numberPage.orElse(1))
                .queryParam("pageSize", pageSize.orElse(5));

        if(category.isPresent()){
            builder.queryParam("category", encodeValue(category.get()));
        }

        if(brands.isPresent() && brands.get().size() > 0) {
            for (String brand: brands.get()) {
                builder.queryParam("brands", encodeValue(brand));
            }
        }

        log.info(builder.toUriString());

        Page<Product> page = restTemplate.getForObject(builder.toUriString(), RestResponsePage.class);


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

    private String encodeValue(String value) throws UnsupportedEncodingException {
        log.info("decode: " + value);
        return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
    }

//    @GetMapping
//    public String productList(Model model,
//                              @RequestParam(name = "brands") Optional<List<String>> brands,
//                              @RequestParam(name = "category") Optional<String> category,
//                              @RequestParam(name = "name") Optional<String> name,
//                              @RequestParam(name = "minPrice") Optional<Integer> minPrice,
//                              @RequestParam(name = "maxPrice") Optional<Integer> maxPrice,
//                              @RequestParam(name = "page") Optional<Integer> numberPage,
//                              @RequestParam(name = "pageSize") Optional<Integer> pageSize
//                              ){
//
//        if(category.isPresent()){
//            filter.setCategory(category.orElse(null));
//        }
//
//        if(brands.isPresent()){
//            filter.setBrands(brands.orElse(null));
//        }
//
//        if(minPrice.isPresent()){
//            filter.setMinPrice(minPrice);
//        }
//
//        if(maxPrice.isPresent()){
//            filter.setMaxPrice(maxPrice);
//        }
//
//        if(name.isPresent()){
//            filter.setName(name);
//        }
//
//
//        Page<Product> page = productService.filterByPrice(
//                filter.getCategory(),
//                filter.getBrands(),
//                filter.getName().orElse(""),
//                filter.getMinPrice().orElse(10),
//                filter.getMaxPrice().orElse(5000),
//                PageRequest.of(numberPage.orElse(1) - 1, pageSize.orElse(5))
//        );
//
//        model.addAttribute("productsPage", page);
//
//        model.addAttribute("minPrice", filter.getMinPrice().orElse(10));
//        model.addAttribute("maxPrice", filter.getMaxPrice().orElse(1000));
//
//        model.addAttribute("filter", filter);
//
//        model.addAttribute("namePage", "shop");
//        model.addAttribute("products", productService.findAll());
//        model.addAttribute("categories", categoryService.findAll());
//        model.addAttribute("brands", brandRepository.findAll());
//        model.addAttribute("cart", cart);
//        return "shop";
//    }


}
