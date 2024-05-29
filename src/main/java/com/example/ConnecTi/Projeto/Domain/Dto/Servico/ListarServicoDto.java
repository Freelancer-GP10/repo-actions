package com.example.ConnecTi.Projeto.Domain.Dto.Servico;

import java.time.LocalDateTime;
import java.util.Date;

public record ListarServicoDto(
                                Long id,
                                String nome,
                                Date prazo,
                                Date dataInicio,
                                Double valor,
                                String descricao
) {
}
