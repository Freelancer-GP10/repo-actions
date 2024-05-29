package com.example.ConnecTi.Projeto.Model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idEmpresa")
public class Empresa {
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long idEmpresa;
      private String nome;
      private String email;
      private String senha;
      private String ramo;
      private String cnpj;
      private String telefone;

      @OneToOne
      @JoinColumn(name = "fk_Endereco")
      private ApiEndereco endereco;

      @OneToMany(mappedBy = "empresa")
      private List<Servico> servicos;

      @OneToOne
      @JoinColumn(name = "fk_Usuario")
      private Usuario usuario;
//      @ManyToOne
//      @JoinColumn(name = "id_especialidade")
//      private Especialidade especialidade;
}

