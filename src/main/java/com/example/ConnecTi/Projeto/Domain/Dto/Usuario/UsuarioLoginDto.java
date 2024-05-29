package com.example.ConnecTi.Projeto.Domain.Dto.Usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioLoginDto {
    @NotBlank
    private String email;
    @NotBlank
    private String senha;
}
