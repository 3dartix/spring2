package ru.geekbrains.service;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.geekbrains.entity.Role;
import ru.geekbrains.entity.User;
import ru.geekbrains.mapper.UserMapper;
import ru.geekbrains.repo.RoleRepository;
import ru.geekbrains.repo.UserRepository;
import ru.geekbrains.representation.UserRepr;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CommonsLog
@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).get();
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername:" + username);
        User user = userRepository.findByUsername(username).orElse(null);
        log.info("USER: " + user);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    @Transactional
    public void save (UserRepr userRepr){
        userRepr.setPassword(passwordEncoder.encode(userRepr.getPassword()));
        User user = UserMapper.MAPPER.toUser(userRepr);
        userRepository.save(user);
    }

    @Transactional
    public boolean isUserExist (String username){
        Optional<User> byUsername = userRepository.findByUsername(username);
        if(byUsername.isPresent()) {
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public void delete (String username){
        userRepository.deleteByUsername(username);
    }


    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}