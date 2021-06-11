package com.security.PKISystem.controller;

import com.security.PKISystem.domain.dto.UserDto;
import com.security.PKISystem.domain.dto.UserRegisterDto;
import com.security.PKISystem.domain.mapper.UserMapper;
import com.security.PKISystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PreAuthorize("hasAuthority('GET_USERS')")
    @GetMapping
    public List<UserDto> getAllUsers(){
        return UserMapper.mapUsersToUsersDto(userService.getAllUsers());
    }

    @PostMapping("/register")
    public void registerUser(@RequestBody UserRegisterDto registerDto){
        userService.registerUser(registerDto);
    }

    @PostMapping("/login")
    public String login(@RequestBody UserRegisterDto registerDto){
        return userService.login(registerDto.getUsername(), registerDto.getPassword());
    }
}
