package ru.geekbrains.controller;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.geekbrains.entity.Role;
import ru.geekbrains.repo.RoleRepository;
import ru.geekbrains.representation.UserRepr;
import ru.geekbrains.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@CommonsLog
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    public UserController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("new")
    public String createUser(Model model) {
        log.info("Create user form");
        //передаем в шаблонизатор html страницу user из view
        model.addAttribute("user", new UserRepr());
        model.addAttribute("roles", roleRepository.findAll());
        return "registr-form";
    }

    @PostMapping
    public String saveUser(@Valid @ModelAttribute("user") UserRepr user, BindingResult bindingResult, Model model, HttpServletRequest request) throws ServletException {
        log.info("Save user method");
        log.info(user.getRoles());

        if(bindingResult.hasErrors()){
            log.info(bindingResult.getAllErrors());
            return "registr-form";
        }

        if(!user.getPassword().equals(user.getConfirmPassword())){
            log.info(user.getPassword() + " :: " + user.getConfirmPassword());
            bindingResult.rejectValue("password", "error.user","Пароли не совпадают");
            bindingResult.rejectValue("confirmPassword", "error.user","Пароли не совпадают");
            return "registr-form";
        }

        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findRoleByName("ROLE_USER").get());
        user.setRoles(roles);

        String pass = user.getPassword();
        userService.save(user);

//        request.login(user.getUsername(), bCryptPasswordEncoder.encode(user.getPassword()));
        authWithHttpServletRequest (request, user.getUsername(), pass);
        return "redirect:/";
    }

    public void authWithHttpServletRequest(HttpServletRequest request, String username, String password) {
        try {
            log.info(username + " :: " + password);
            request.login(username, password);
        } catch (ServletException e) {
            log.error("Error while login ", e);
        }
    }

}
