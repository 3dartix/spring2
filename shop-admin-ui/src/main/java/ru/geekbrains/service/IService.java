package ru.geekbrains.service;


import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface IService<T> {
    List<T> findAll();
    Optional<T> findById(Long id);
    Optional<T> findByName(String name);
    void save(T o);
    void deleteById(Long id);
}
