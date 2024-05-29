package com.example.ConnecTi.Projeto.Domain.Dto.Servico.Mapper;

import com.example.ConnecTi.Projeto.Domain.Dto.Servico.ListarServicoDto;
import com.example.ConnecTi.Projeto.Model.Servico;

public class MapperServico {

    public static ListarServicoDto fromEntity(Servico servico) {
        ListarServicoDto dto = new ListarServicoDto(
                servico.getIdServico(),
                servico.getNome(),
                servico.getPrazo(),
                servico.getDataInicio(),
                servico.getValor(),
                servico.getDescricao()
        );
        return dto;
    }
}
