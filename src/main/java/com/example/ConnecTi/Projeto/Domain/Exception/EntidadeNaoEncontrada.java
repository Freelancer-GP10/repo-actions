package com.example.ConnecTi.Projeto.Domain.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntidadeNaoEncontrada extends RuntimeException{
    public EntidadeNaoEncontrada(String message) {
        super(message);
    }
}
