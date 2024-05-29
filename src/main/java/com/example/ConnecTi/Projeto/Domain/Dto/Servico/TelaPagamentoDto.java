package com.example.ConnecTi.Projeto.Domain.Dto.Servico;

import com.example.ConnecTi.Projeto.Enum.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TelaPagamentoDto
 {
     private Long id;
     private String nome;
     private Double valor;
     private Status status;
     private Date dataTransacao;
     private String tipoTransacao;
     private Date prazo;

}
