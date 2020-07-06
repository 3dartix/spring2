package ru.geekbrains.controller;

import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.geekbrains.entity.Picture;
import ru.geekbrains.entity.Product;
import ru.geekbrains.repo.BrandRepository;
import ru.geekbrains.repo.PictureRepository;
import ru.geekbrains.service.CategoryService;
import ru.geekbrains.service.ProductService;
import ru.geekbrains.utils.Cart;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.Random;

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
    public String indexPage(Model model){
        model.addAttribute("namePage", "home");
        model.addAttribute("products", productService.findAll());
        model.addAttribute("cart", cart);
//        Product p = new Product();
//        p.getPictures().get(getRandom(0, p.getPictures().size() - 1));
        return "index";
    }

    @GetMapping ("product")
    public String productPage(Model model){
        model.addAttribute("namePage", "shop");
        model.addAttribute("products", productService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("brands", brandRepository.findAll());
        model.addAttribute("cart", cart);
        return "shop";
    }


    @GetMapping ("details/{id}")
    public String detailsPage(Model model,
                              @PathVariable Optional<Long> id){
        model.addAttribute("cart", cart);
        model.addAttribute("product", productService.findById(id.orElse(-1L)));

        return "product-details";
    }

    @GetMapping ("cart")
    public String cartPage(Model model,
                           @RequestParam(value = "idProduct") Optional<Long> idProduct){
        model.addAttribute("namePage", "cart");
        model.addAttribute("cart", cart);
        return "cart";
    }

    @GetMapping("/cart/add/{id}")
    public void addToCart(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        cart.add(productService.findById(id).get());
        response.sendRedirect(request.getHeader("referer"));
    }

    @GetMapping("/cart/subtract/{id}")
    public void minusToCart(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        cart.subtract(productService.findById(id).get());
        response.sendRedirect(request.getHeader("referer"));
    }

    @GetMapping ("cart/confirm-purchase")
    public String confirmPurchasePage(Model model,
                           @RequestParam(value = "idProduct") Optional<Long> idProduct){
        model.addAttribute("cart", cart);
        return "confirm-purchase";
    }

    @GetMapping("/picture/{pictureId}")
    public void adminDownloadProductPicture(@PathVariable("pictureId") Long pictureId,
                                            HttpServletResponse response) throws IOException {
        log.info("Picture " + pictureId);
        Optional<Picture> picture = pictureRepository.findById(pictureId);
        if (picture.isPresent()) {

            if(!saveToDatabase) {
                log.info("Load image from disk: " + "picture.get().getContentType() " + picture.get().getContentType() +
                        "picture.get().getLocalPath()" + picture.get().getLocalPath());
                response.setContentType(picture.get().getContentType());
                response.getOutputStream().write(FileUtils.readFileToByteArray(new File(picture.get().getLocalPath())));

            } else {
                log.info("Load image from database: " + picture.get().getLocalPath());
                response.setContentType(picture.get().getContentType());
                response.getOutputStream().write(picture.get().getPictureData().getData());
            }
        }
    }

    public int getRandom (int a, int b) {
        int result = a + (int) (Math.random() * b);
        log.info("Random number: " + result);
        return result;
    }

}
