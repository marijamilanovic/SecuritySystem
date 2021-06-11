package com.security.PKISystem.domain.mapper;

import com.security.PKISystem.domain.User;
import com.security.PKISystem.domain.dto.UserDto;
import com.security.PKISystem.domain.dto.UserRegisterDto;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {

    public static UserDto mapUserToUserDto(User user){
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    public static List<UserDto> mapUsersToUsersDto(List<User> users){
        List<UserDto> userDtos = new ArrayList<>();
        for(User u : users)
            userDtos.add(mapUserToUserDto(u));
        return userDtos;
    }

    public static User mapUserRegisterDtoToUser(UserRegisterDto registerDto){
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(registerDto.getPassword());
        return user;
    }
}
