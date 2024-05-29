package com.example.ConnecTi.Projeto.Domain.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class JaFoiAceitoException extends RuntimeException{
    public JaFoiAceitoException(String message) {
        super(message);
    }
}
