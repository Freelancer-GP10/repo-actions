package com.example.ConnecTi.Projeto.Domain.Dto.Usuario;

import com.example.ConnecTi.Projeto.Enum.Papeis;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioTokenDto {
    private Long userId;
    private String papel;
    private String email;
    private String token;
}
