package com.example.ConnecTi.Projeto.Domain.Dto.Conexao;

import jakarta.validation.constraints.NotNull;

public record CadastrarConexaoDto(
        boolean aceito,
        @NotNull(message = "O id do serviço não pode ser nulo")
        Long fkServico,
        @NotNull(message = "O id do freelancer não pode ser nulo")
        Long fkFreelancer,
        @NotNull(message = "O id da empresa não pode ser nulo")
        Long fkEmpresa,
        @NotNull(message = "O id da avaliação não pode ser nulo")
        Long fkAvaliacao) {
}
