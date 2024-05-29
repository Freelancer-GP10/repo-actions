package com.example.ConnecTi.Projeto.Domain.Dto.Conexao;

import jakarta.validation.constraints.NotNull;

public record   AceitarServicoDTO(
        @NotNull(message = "O id do serviço não pode ser nulo")
        Long fkServico
) {
}
