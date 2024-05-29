package com.example.ConnecTi.Projeto.Domain.Dto.Conexao;

public record ConexaoDto( Long id, boolean aceito, Long fkServico, Long fkFreelancer, Long fkEmpresa, Long fkAvaliacao) {
}
