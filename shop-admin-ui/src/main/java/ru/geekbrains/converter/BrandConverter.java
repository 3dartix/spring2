package ru.geekbrains.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.geekbrains.entity.Brand;
import ru.geekbrains.entity.Category;
import ru.geekbrains.repo.BrandRepository;
import ru.geekbrains.repo.CategoryRepository;
import ru.geekbrains.representation.BrandRepr;

@Component
public class BrandConverter implements Converter<String, BrandRepr> {

    private final BrandRepository repository;

    @Autowired
    public BrandConverter(BrandRepository repository) {
        this.repository = repository;
    }

    @Override
    public BrandRepr convert(String s) {
        return repository.findBrandByName(s).map(BrandRepr::new).orElse(null);
    }
}
