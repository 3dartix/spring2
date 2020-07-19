package ru.geekbrains.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.geekbrains.entity.Category;
import ru.geekbrains.repo.CategoryRepository;

@Component
public class CategoryConverter implements Converter<String, Category> {

    private final CategoryRepository repository;

    @Autowired
    public CategoryConverter(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Category convert(String s) {
        return repository.findRoleByName(s).orElse(null);
    }
}
