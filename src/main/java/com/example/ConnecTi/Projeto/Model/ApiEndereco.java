package com.example.ConnecTi.Projeto.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class ApiEndereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long idEndereco;
    private String cep;
    private String logradouro;
    private String numero;
    private String UF;



}
