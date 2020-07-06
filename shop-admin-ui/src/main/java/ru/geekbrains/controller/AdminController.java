package ru.geekbrains.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
//
//    @Autowired
//    public UserRepository userRepository;

    @GetMapping("/")
    public String indexPage(){
        return "index";
    }

//    @GetMapping("/user")
//    public String user(){
//        return "user";
//    }
}
