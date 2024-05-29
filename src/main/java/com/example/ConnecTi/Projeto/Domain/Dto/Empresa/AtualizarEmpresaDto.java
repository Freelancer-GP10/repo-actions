package com.example.ConnecTi.Projeto.Domain.Dto.Empresa;

public record AtualizarEmpresaDto(
        String nome,
        String cnpj,
        String email,
        String senha,
        String ramo,
        String telefone) {
}
