package ru.geekbrains.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.geekbrains.entity.User;
import ru.geekbrains.repo.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void save (User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repository.save(user);
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(long id) {
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void deleteProductByIe(Long id){
        repository.deleteProductById(id);
    }

    @Transactional(readOnly = true)
    public User findByName(String name) {
       return repository.findUserByName(name).orElse(null);
    }
}
