package com.security.PKISystem.service.impl;

import com.security.PKISystem.domain.User;
import com.security.PKISystem.domain.dto.UserRegisterDto;
import com.security.PKISystem.domain.mapper.UserMapper;
import com.security.PKISystem.exception.BadRequestException;
import com.security.PKISystem.exception.NotFoundException;
import com.security.PKISystem.repository.UserRepository;
import com.security.PKISystem.security.JwtService;
import com.security.PKISystem.service.RoleService;
import com.security.PKISystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private JwtService jwtService;

    @Override
    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if(user == null)
            throw new NotFoundException("User: " + username + " not found!");
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User registerUser(UserRegisterDto registerDto) {
        verifyUserInput(registerDto);
        User user = UserMapper.mapUserRegisterDtoToUser(registerDto);
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRoles(roleService.findByName("ROLE_USER"));
        return userRepository.save(user);
    }

    @Override
    public String login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if(user == null || !passwordEncoder.matches(password, user.getPassword()))
            throw new BadRequestException("Username or password is incorrect!");
        return jwtService.createToken(user.getUsername());
    }

    private void verifyUserInput(UserRegisterDto registerDto){
        if(!emailChecker(registerDto.getEmail()))
            throw new BadRequestException("Invalid email format!");
        if(!patternChecker(registerDto.getPassword()))
            throw new BadRequestException("Password is too weak");
        if(!registerDto.getPassword().equals(registerDto.getPasswordRepeat()))
            throw new BadRequestException("Repeated password is incorrect!");
        if(userRepository.findByUsername(registerDto.getUsername()) != null)
            throw new BadRequestException("Username must be unique!");
    }

    private boolean emailChecker(String email){
        Pattern patternEmail = Pattern.compile("[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$");
        return patternEmail.matcher(email).matches();
    }

    private boolean patternChecker(String password) {
        Pattern patternPass = Pattern.compile("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@*:%-_#.&;,+])(?=\\S+$).{8,}");
        return patternPass.matcher(password).matches();
    }
}
