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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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


//        Page<Product> page = productService.filterByPrice(
//                filter.getCategory(),
//                filter.getBrands(),
//                filter.getName().orElse(""),
//                filter.getMinPrice().orElse(10),
//                filter.getMaxPrice().orElse(5000),
//                PageRequest.of(numberPage.orElse(1) - 1, pageSize.orElse(5))
//        );


        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(productClientPath)
//                .queryParam("brands", brands)
//                .queryParam("category", category)
//                .queryParam("name", name)
//                .queryParam("minPrice", minPrice)
//                .queryParam("maxPrice", maxPrice)
                .queryParam("page", numberPage.orElse(10))
                .queryParam("pageSize", pageSize.orElse(1000));

        log.info(builder.toUriString());

        Page<Product> page;
//        Page<Product> page = restTemplate.getForObject(builder.toUriString(), PageEntity.class).getPage();


//        ParameterizedTypeReference<RestResponsePage<Product>> type = new ParameterizedTypeReference<RestResponsePage<Product>>() {};
        ResponseEntity<Page<Product>> rateResponse =
                restTemplate.exchange(builder.toUriString(),
                        HttpMethod.GET, null, new ParameterizedTypeReference<Page<Product>>() {
                        });
        page = rateResponse.getBody();
//        ResponseEntity<RestResponsePage<Product>> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, null, type);
//        page = response.getBody();


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
