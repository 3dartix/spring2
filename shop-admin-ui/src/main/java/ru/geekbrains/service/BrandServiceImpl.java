package ru.geekbrains.service;

import org.springframework.beans.factory.annotation.Autowired;
import ru.geekbrains.entity.Brand;
import ru.geekbrains.exceptions.NotFoundException;
import ru.geekbrains.repo.BrandRepository;
import org.springframework.stereotype.Service;
import ru.geekbrains.representation.BrandRepr;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BrandServiceImpl implements IService<BrandRepr> {
    private final BrandRepository repository;

    @Autowired
    public BrandServiceImpl(BrandRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<BrandRepr> findAll() {
        return repository.findAll().stream().map(BrandRepr::new).collect(Collectors.toList());
    }

    @Override
    public Optional<BrandRepr> findById(Long id) {
        return repository.findById(id).map(BrandRepr::new);
    }

    @Override
    public Optional<BrandRepr> findByName(String name) {
        return repository.findBrandByName(name).map(BrandRepr::new);
    }

    @Override
    public void save(BrandRepr brandRepr) {
        Brand product = (brandRepr.getId() != null) ? repository.findById(brandRepr.getId())
                .orElseThrow(() -> new NotFoundException()) : new Brand(
                brandRepr.getId(),
                brandRepr.getName(),
                brandRepr.getProducts());
        repository.save(product);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
