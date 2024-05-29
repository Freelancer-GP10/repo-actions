package com.example.ConnecTi.Projeto.Domain.Dto.Freelancer;

public record AtualizarFreelancerDto(
        String nome,
        String sobrenome,
        String email,
        String senha,
        String cpf,
        String dominio,
        String ramo,
        String telefone,
        String areaAtuacao
)  {
}
