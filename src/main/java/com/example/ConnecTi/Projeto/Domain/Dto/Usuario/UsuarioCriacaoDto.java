package com.example.ConnecTi.Projeto.Domain.Dto.Usuario;

import com.example.ConnecTi.Projeto.Enum.Papeis;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioCriacaoDto {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String papel;
    @NotBlank
    private String senha;

}
