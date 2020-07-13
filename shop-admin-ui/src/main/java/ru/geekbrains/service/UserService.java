package ru.geekbrains.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.geekbrains.entity.User;
import ru.geekbrains.repo.UserRepository;
import ru.geekbrains.representation.UserRepr;
import ru.geekbrains.representation.mapper.UserMapper;

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
    public void save (UserRepr userRepr){
        userRepr.setPassword(passwordEncoder.encode(userRepr.getPassword()));
        User user = UserMapper.MAPPER.toUser(userRepr);
        repository.save(user);
    }

    @Transactional(readOnly = true)
    public UserRepr findById(long id) {
//        Optional<UserRepr> optional = Optional.empty();
//        optional.of(UserMapper.MAPPER.fromUser(repository.findById(id).get()));
//        return optional;
        return UserMapper.MAPPER.fromUser(repository.findById(id).get());
    }

    @Transactional(readOnly = true)
    public List<UserRepr> findAll() {
        return UserMapper.MAPPER.FromUserList(repository.findAll());
    }

    @Transactional
    public void deleteProductByIe(Long id){
        repository.deleteProductById(id);
    }

    @Transactional(readOnly = true)
    public UserRepr findByName(String name) {
       return UserMapper.MAPPER.fromUser(repository.findByUsername(name).orElse(null));
    }
}
