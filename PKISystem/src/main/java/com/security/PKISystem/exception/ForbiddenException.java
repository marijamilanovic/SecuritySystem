package com.security.PKISystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException{
    private static final long serialVersionUID = -6576392190102343472L;

    public ForbiddenException(String message) {
        super(message);
    }
}
