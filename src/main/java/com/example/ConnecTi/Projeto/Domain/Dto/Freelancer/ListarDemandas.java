package com.example.ConnecTi.Projeto.Domain.Dto.Freelancer;

import java.time.LocalDateTime;

public record ListarDemandas(String tipo, String descricao, String nomeServico, String nomeEmpresa, Double valor,
                             LocalDateTime prazo,String status) {
}
