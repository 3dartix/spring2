package ru.geekbrains.controller;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.beans.Cart;
import ru.geekbrains.entity.Product;
import ru.geekbrains.mapper.CommentMapper;
import ru.geekbrains.representation.CommentRepr;
import ru.geekbrains.service.ProductService;
import ru.geekbrains.service.UserService;
import ru.geekbrains.utils.Helpers;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Optional;

@Controller
@CommonsLog
@RequestMapping("/details")
public class DetailsController {

    public final UserService userService;
    public final ProductService productService;
    public final Cart cart;

    @Autowired
    public DetailsController(UserService userService, ProductService productService, Cart cart) {
        this.userService = userService;
        this.productService = productService;
        this.cart = cart;
    }

    @PostMapping()
    public String addComment(@ModelAttribute("comment") CommentRepr commentRepr,
                             Principal principal)  {

        log.info(commentRepr);
        Product product = productService.findById(commentRepr.getProduct_id().get()).get();

        product.getComments().add(
                CommentMapper.MAPPER.toComment(
                        userService.findByUsername(principal.getName()),
                        product,
                        commentRepr
                ));

        productService.save(product);
        return "redirect:/details/" + product.getId();
    }

    @GetMapping("{id}")
    public String detailsPage(Model model,
                              @PathVariable Optional<Long> id, Principal principal,
                              HttpServletRequest request,
                              HttpServletResponse response){
        Optional<Product> product = productService.findById(id.orElse(-1L));

        model.addAttribute("cart", cart);
        model.addAttribute("comment", new CommentRepr());
        model.addAttribute("product", product);

        if(principal != null){
            model.addAttribute("is_comment_exists", Helpers.HELPERS.isCommentAllow(principal.getName(), product.get()));
        } else {
            model.addAttribute("is_comment_exists", false);
        }

        log.info(request.getServletPath());
        Cookie cookie = new Cookie("product", request.getServletPath());
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setSecure(true);
        response.addCookie(cookie);

        return "product-details";
    }
}
