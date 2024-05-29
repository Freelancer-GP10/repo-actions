package com.example.ConnecTi.Projeto.Domain.Dto.Empresa;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CadastrarEmpresaDto(
        @NotBlank(message = "Nome nao pode ser nulo")
        String nome,
        @NotBlank(message = "cnpj nao pode ser nulo")
        @Size(min = 14,max = 14)
        String cnpj,
        @NotBlank(message = "ramo nao pode ser nulo")
        String ramo,
        @NotBlank(message = "telefone incorreto")
        @Size(min = 11,max = 11)
        String telefone
        ) {
}
