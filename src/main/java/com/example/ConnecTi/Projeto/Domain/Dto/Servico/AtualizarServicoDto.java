package com.example.ConnecTi.Projeto.Domain.Dto.Servico;

import java.time.LocalDateTime;
import java.util.Date;

public record AtualizarServicoDto(
        String nome,
        String descricao,
        Double valor,
        Date prazo
) {
}
