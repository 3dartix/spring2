package ru.geekbrains.controller;

import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.geekbrains.entity.Picture;
import ru.geekbrains.repo.BrandRepository;
import ru.geekbrains.repo.PictureRepository;
import ru.geekbrains.service.CategoryService;
import ru.geekbrains.service.ProductService;
import ru.geekbrains.beans.Cart;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@CommonsLog
@RequestMapping("/")
public class MainController {

    public final ProductService productService;
    public final CategoryService categoryService;
    public final BrandRepository brandRepository;
    public final PictureRepository pictureRepository;
    public final Cart cart;

    @Value("${data.save-to-database}")
    private boolean saveToDatabase;

    @Value("${data.path.picture-client}")
    private String pictureClientPath;

    @Autowired
    private RestTemplate restTemplate;

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Autowired
    public MainController(ProductService productService,
                          CategoryService categoryService,
                          BrandRepository brandRepository,
                          PictureRepository pictureRepository,
                          Cart cart) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.brandRepository = brandRepository;
        this.pictureRepository = pictureRepository;
        this.cart = cart;
    }

    @GetMapping
    public String indexPage(Model model, HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        for (Cookie c:cookies) {
//            if(c.getName().equals("product")){
                log.info(c.getValue());
//            }
        }
//        response.addCookie();
//        log.info(request.getContextPath());

        model.addAttribute("namePage", "home");
        model.addAttribute("products", productService.findAll());
        model.addAttribute("cart", cart);
//        Product p = new Product();
//        p.getPictures().get(getRandom(0, p.getPictures().size() - 1));
        return "index";
    }





    @GetMapping("/picture/{pictureId}")
    public void adminDownloadProductPicture(@PathVariable("pictureId") Long pictureId,
                                            HttpServletResponse response) throws IOException {
        response.getOutputStream().write(
                restTemplate.getForObject(pictureClientPath + pictureId, byte[].class)
        );

//        log.info("Picture " + pictureId);
//        Optional<Picture> picture = pictureRepository.findById(pictureId);
//        if (picture.isPresent()) {
//
//            if(!saveToDatabase) {
//                log.info("Load image from disk: " + "picture.get().getContentType() " + picture.get().getContentType() +
//                        "picture.get().getLocalPath()" + picture.get().getLocalPath());
//                response.setContentType(picture.get().getContentType());
//                response.getOutputStream().write(FileUtils.readFileToByteArray(new File(picture.get().getLocalPath())));
//
//            } else {
//                log.info("Load image from database: " + picture.get().getLocalPath());
//                response.setContentType(picture.get().getContentType());
//                response.getOutputStream().write(picture.get().getPictureData().getData());
//            }
//        }
    }

    @GetMapping("auth-form")
    public String loginPage(HttpServletRequest request, Model model){

        HttpSession session = request.getSession(false);
        String errorMessage = null;
        if (session != null) {
            AuthenticationException ex = (AuthenticationException) session
                    .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (ex != null) {
                errorMessage = ex.getMessage();
            }
        }
        model.addAttribute("errorMessage", errorMessage);

        return "auth-form";
    }

    public int getRandom (int a, int b) {
        int result = a + (int) (Math.random() * b);
        log.info("Random number: " + result);
        return result;
    }

}
