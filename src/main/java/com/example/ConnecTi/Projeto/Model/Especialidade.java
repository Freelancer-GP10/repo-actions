package com.example.ConnecTi.Projeto.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Especialidade {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEspecialidade;
    private String Tipo;
    private String Descricao;
}
