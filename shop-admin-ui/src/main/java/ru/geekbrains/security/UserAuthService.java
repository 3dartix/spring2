package ru.geekbrains.security;

import lombok.extern.apachecommons.CommonsLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import ru.geekbrains.repo.UserRepository;

import java.util.stream.Collectors;

@CommonsLog
public class UserAuthService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserAuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername");
        return userRepository.findByUsername(username)
                //если пользователь нашелся делаем преобразование в User  import org.springframework.security.core.userdetails.UserDetails;
                .map(user -> new User(user.getUsername(),
                        user.getPassword(),
                        user.getRoles().stream()
                            .map(role -> new SimpleGrantedAuthority(role.getName()))
                            .collect(Collectors.toList())))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
