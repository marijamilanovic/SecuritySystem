package com.security.PKISystem.domain.dto;

import lombok.Data;

@Data
public class UserRegisterDto {
    private String username;
    private String email;
    private String password;
    private String passwordRepeat;
}
