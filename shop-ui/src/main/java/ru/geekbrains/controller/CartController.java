package ru.geekbrains.controller;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.entity.Order;
import ru.geekbrains.mapper.CartToOrderMapper;
import ru.geekbrains.repo.OrderRepository;
import ru.geekbrains.repo.UserRepository;
import ru.geekbrains.service.ProductService;
import ru.geekbrains.beans.Cart;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

@Controller
@CommonsLog
@RequestMapping("/cart")
public class CartController {

    public final ProductService productService;
    public final OrderRepository orderRepository;
    public final UserRepository userRepository;
    public final Cart cart;

    @Autowired
    public CartController(ProductService productService, OrderRepository orderRepository, Cart cart, UserRepository userRepository) {
        this.productService = productService;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.cart = cart;
    }

    @GetMapping
    public String cartPage(Model model,
                           @RequestParam(value = "idProduct") Optional<Long> idProduct){
        model.addAttribute("namePage", "cart");
        model.addAttribute("cart", cart);
        return "cart";
    }

    @GetMapping("add/{id}")
    public void addToCart(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        cart.add(productService.findById(id).get());
        response.sendRedirect(request.getHeader("referer"));
    }

    @GetMapping("remove/{id}")
    public void removeProductFromCart(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        cart.removeById(id);
        response.sendRedirect(request.getHeader("referer"));
    }

    @GetMapping("subtract/{id}")
    public void minusToCart(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        cart.subtract(productService.findById(id).get());
        response.sendRedirect(request.getHeader("referer"));
    }


    @GetMapping("confirm-purchase")
    public String confirmPurchasePage(Model model,
                                      @RequestParam(value = "idProduct") Optional<Long> idProduct){
        model.addAttribute("cart", cart);
        model.addAttribute("cartSize", cart.getItems().size());
        return "confirm-purchase";
    }

    @PostMapping("confirm-purchase")
    public String saveOrder(@Valid @ModelAttribute("cart") Cart cart,
                            BindingResult bindingResult, Model model, Principal principal){

        cart.setItems(this.cart.getItems()); // костыль

        log.info("Equals: " + cart);
        if(bindingResult.hasErrors()){
            log.info(bindingResult.getAllErrors());
            model.addAttribute("cart", cart);
            return "confirm-purchase";
        }

        Order order;

//      сохранить заказ в базу и очистить корзину
        if(principal != null) {
            log.info("saveOrder, user = " + principal.getName());
            order = CartToOrderMapper.MAPPER.toOrder(userRepository.findByUsername(principal.getName()).get(), cart);
        } else {
            log.info("saveOrder, user = anonymous");
            order = CartToOrderMapper.MAPPER.toOrder(userRepository.findByUsername("anonymous").get(), cart);
        }

        cart.clear();

        log.info("order.getItems(): "+ order.getItems());
        orderRepository.save(order);

        return "redirect:/";
    }
}
