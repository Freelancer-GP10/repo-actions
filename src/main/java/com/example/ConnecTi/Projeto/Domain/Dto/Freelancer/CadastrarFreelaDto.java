package com.example.ConnecTi.Projeto.Domain.Dto.Freelancer;

import jakarta.validation.constraints.NotBlank;

public record CadastrarFreelaDto(
        @NotBlank
        String nome,
        @NotBlank
        String sobrenome,
        @NotBlank
        String areaAtuacao,
        @NotBlank
        String linguagemDominio,
        @NotBlank
        String formacao,
        @NotBlank
        String cpf,
        @NotBlank
        String telefone
        ) {
}
