package com.example.ConnecTi.Projeto.Domain.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class NaoAutorizadoException extends RuntimeException{
    public NaoAutorizadoException(String message) {
        super(message);
    }
}
