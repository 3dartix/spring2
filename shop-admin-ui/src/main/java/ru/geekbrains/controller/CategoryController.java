package ru.geekbrains.controller;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.entity.Category;
import ru.geekbrains.service.CategoryServiceImpl;
import ru.geekbrains.service.ProductServiceImpl;

import javax.validation.Valid;
import java.util.Optional;

@RequestMapping("/category")
@Controller
@CommonsLog
public class CategoryController {

    private final CategoryServiceImpl categoryService;
    private final ProductServiceImpl productService;

    @Autowired
    public CategoryController(CategoryServiceImpl categoryService, ProductServiceImpl productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @GetMapping
    public String findAll(Model model) {
        log.info("Show all categories");
        model.addAttribute("categories", categoryService.findAll());
        return "all-categories";
    }

    @GetMapping("new")
    public String createCategory(Model model) {
        log.info("Start to create new category");
        //передаем в шаблонизатор html страницу user из view
        model.addAttribute("category", new Category());
        return "category";
    }

    @GetMapping("edit")
    public String editCategory(Model model,
                                @RequestParam(value = "idCategory") Optional<Long> idCategory) {
        log.info("Start to edit category");

        model.addAttribute("category", categoryService.findById(idCategory.orElse(-1L)));
        return "category";
    }

    @PostMapping
    public String saveCategory(@Valid Category category, BindingResult bindingResult) {
        log.info("Save category method");

        if(bindingResult.hasErrors()){
            log.info(bindingResult.getAllErrors());
            return "category";
        }

        categoryService.save(category);
        return "redirect:/category";
    }

    @DeleteMapping
    public String deleteProduct(@RequestParam("id") Long id){
        //нужно как-то удалять каскадом все объекты где есть такая категория
        log.info("Delete category from product:" + id);
        productService.deleteCategory(categoryService.findById(id).get());

        log.info("Delete category with id:" + id);
        categoryService.deleteById(id);
        return "redirect:/category";
    }
}
