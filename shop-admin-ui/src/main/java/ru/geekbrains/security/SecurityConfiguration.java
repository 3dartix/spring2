package ru.geekbrains.security;

import lombok.extern.apachecommons.CommonsLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.geekbrains.repo.UserRepository;

import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity
@CommonsLog
public class SecurityConfiguration {


    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository){
        return new UserAuthService(userRepository);
    }
//    AuthenticationManagerBuilder - создает хранилище пользователей с одним авторизованным пользователем
    //UserDetailsService - создает пользователя UserDetails
    @Autowired
    public void authConfigure(AuthenticationManagerBuilder auth,
                              UserDetailsService userDetailsService,
                              PasswordEncoder passwordEncoder) throws Exception {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        auth.authenticationProvider(provider);

        auth.inMemoryAuthentication()
                .withUser("m_user")
                .password(passwordEncoder.encode("100"))
                .roles("SUPERADMIN");
    }

    @Configuration
//    @Order(2) // зачем он здесь
    public static class UiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                    .antMatchers("/**").permitAll()
                    .antMatchers("/css/**").permitAll()
                    .antMatchers("/js/**").permitAll()
                    .antMatchers("/static/**").permitAll()
                    .antMatchers("/images/**").permitAll()
                    .antMatchers("/blog/**").permitAll()
//                    .antMatchers("/product/**").hasAnyRole("ADMIN", "MANAGER")
//                    .antMatchers("/user/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .formLogin()
                    .and()
                    .logout().logoutSuccessUrl("/login?logout");
        }
    }
}
