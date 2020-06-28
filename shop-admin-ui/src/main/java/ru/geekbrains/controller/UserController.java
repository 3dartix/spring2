package ru.geekbrains.controller;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.entity.User;
import ru.geekbrains.repo.RoleRepository;
import ru.geekbrains.service.UserService;

import javax.validation.Valid;

@RequestMapping("/user")
@Controller
@CommonsLog
public class UserController {

    private final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    public UserController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public String userList(Model model) {
        log.info("Show user list");

        model.addAttribute("users", userService.findAll());
        return "all-users";
    }

    @GetMapping("new")
    public String createUser(Model model) {
        log.info("Create user form");
        //передаем в шаблонизатор html страницу user из view
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleRepository.findAll());
        return "user";
    }

    @GetMapping("edit")
    public String createUser(@RequestParam("id") Long id, Model model) {
        log.info("edit user form " + id);
        //передаем в шаблонизатор html страницу user из view
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("roles", roleRepository.findAll());
        return "user";
    }

    @PostMapping
    public String saveUser(@Valid User user, BindingResult bindingResult, Model model) {
        log.info("Save user method");

        //обработка одинаковых имен не работатет, когда мы редактируем объект
        if(hasName(user.getName())) {
            log.info("This name is already in use");
//            ObjectError error = new ObjectError("name","This name is already in use");
//            bindingResult.addError(error);
            bindingResult.rejectValue("name", "error.user","Это имя уже используется");
            model.addAttribute("roles", roleRepository.findAll());
            return "user";
        }

        if(bindingResult.hasErrors()){
            log.info(bindingResult.getAllErrors());
            model.addAttribute("roles", roleRepository.findAll());
            return "user";
        }

        userService.save(user);
        return "redirect:user";
    }

    @DeleteMapping
    public String deleteProduct(@RequestParam("id") Long id){
        log.info("Delete product with id:" + id);
        userService.deleteProductByIe(id);
        return "redirect:/user";
    }

    public boolean hasName (String name){
        if(userService.findByName(name) != null){
            return true;
        } else {
            return false;
        }
    }
}
