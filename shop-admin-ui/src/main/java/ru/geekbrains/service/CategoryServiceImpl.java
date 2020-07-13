package ru.geekbrains.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.geekbrains.entity.Category;
import ru.geekbrains.repo.CategoryRepository;
import ru.geekbrains.representation.BrandRepr;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements IService<Category> {
    private final CategoryRepository repository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository repository) {
        this.repository = repository;
    }


    @Override
    public List<Category> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Category> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Category> findByName(String name) {
        return Optional.empty();
    }

    @Override
    public void save(Category category) {
        repository.save(category);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
