package com.example.ConnecTi.Projeto.Domain.Dto.Conexao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConexaoInfoDTO {
        private Long id;

        private String statusServico;
        private Date dataPostada;
        private String nomeServico;
        private String tipoServico;
        private String nomeEmpresa;
        private String descricao;
}
