package com.example.demo.config;


import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@Configuration()
public class UserDetailsImplementation implements UserDetailsService {
    private final UserService userService;
    public static final String EMAIL = "^[A-Za-z0-9._+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
    public static final String MOBILE_NO = "[0-9]+";

    @Autowired
    public UserDetailsImplementation(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String contact) throws UsernameNotFoundException {
        if (contact.matches(EMAIL)) {
            Optional<User> userInfo = userService.getByEmail(contact);
            User user = userInfo.get();
            GrantedAuthority authority = new SimpleGrantedAuthority("ADMIN");

            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .roles("USER")
                    .build();
            // return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), Arrays.asList(authority));
        } else if (contact.matches(MOBILE_NO)) {
            Optional<User> userInfo = userService.getByMobile(100L, contact);
            User user = userInfo.get();
            GrantedAuthority authority = new SimpleGrantedAuthority("ADMIN");
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getMobile())
                    .password(user.getPassword())
                    .roles("USER")
                    .build();
            //  return new org.springframework.security.core.userdetails.User(user.getMobile(), user.getPassword(), Arrays.asList(authority));
        } else {
            throw new UsernameNotFoundException("Invalid username");
        }
    }
}