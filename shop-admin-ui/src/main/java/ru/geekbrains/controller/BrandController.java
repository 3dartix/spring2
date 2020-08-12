package ru.geekbrains.controller;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.representation.BrandRepr;
import ru.geekbrains.service.BrandServiceImpl;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping(value = "/brand")
@CommonsLog
public class BrandController {
    private final BrandServiceImpl brandService;

    @Autowired
    public BrandController(BrandServiceImpl brandService) {
        this.brandService = brandService;
    }

    @GetMapping
    public String findAll(Model model) {
        log.info("Show all brands");
        model.addAttribute("brands", brandService.findAll());
        return "all-brands";
    }

    @GetMapping("new")
    public String createBrand(Model model) {
        log.info("Start to create new brand");
        model.addAttribute("brand", new BrandRepr());
        return "brand";
    }

    @GetMapping("edit")
    public String editBrand(Model model,
                               @RequestParam(value = "id") Optional<Long> id) {
        log.info("Start to edit brand!" + id.orElse(-1L));

        model.addAttribute("brand", brandService.findById(id.orElse(-1L)));
        return "brand";
    }

    @PostMapping
    //@RequestBody - добавил для spring test
    public String saveBrand(@Valid BrandRepr brandRepr, BindingResult bindingResult) {
        log.info("Save brand method");

        if(bindingResult.hasErrors()){
            log.info(bindingResult.getAllErrors());
            return "brand";
        }

        brandService.save(brandRepr);
        return "redirect:/brand";
    }

    @DeleteMapping
    public String deleteBrand(@RequestParam("id") Long id){
        //нужно как-то удалять каскадом все объекты где есть такая категория
        log.info("Delete brand from product:" + id);
//        brandService.deleteById(id);

        log.info("Delete brand with id:" + id);
        brandService.deleteById(id);
        return "redirect:/brand";
    }
}
