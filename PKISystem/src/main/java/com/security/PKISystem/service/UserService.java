package com.security.PKISystem.service;

import com.security.PKISystem.domain.User;
import com.security.PKISystem.domain.dto.UserRegisterDto;

import java.util.List;

public interface UserService {

    User findByUsername(String username);

    List<User> getAllUsers();

    User registerUser(UserRegisterDto registerDto);

    String login(String username, String password);
}
