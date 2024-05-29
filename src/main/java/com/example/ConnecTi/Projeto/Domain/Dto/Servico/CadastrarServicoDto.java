package com.example.ConnecTi.Projeto.Domain.Dto.Servico;

import com.example.ConnecTi.Projeto.Model.Empresa;
import com.example.ConnecTi.Projeto.Model.Especialidade;
import com.example.ConnecTi.Projeto.Model.StatusServico;

import java.time.LocalDateTime;
import java.util.Date;

public record CadastrarServicoDto(
                                  String nome,
                                  Date prazo,
                                  Date dataInicio,
                                  Double valor,
                                  String descricao
) {
}
