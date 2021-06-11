package com.security.PKISystem.security;


import com.security.PKISystem.exception.NotFoundException;
import com.security.PKISystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = userService.findByUsername(username);
        if (user == null)
            throw new NotFoundException("User with username:" + username + " not found!");
        return user;
    }
}
