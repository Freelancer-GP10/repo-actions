package com.example.ConnecTi.Projeto.Model;

import com.example.ConnecTi.Projeto.Enum.Papeis;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString
public class Usuario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String senha;
    private String papel;

    @OneToOne(mappedBy = "usuario")
    private Freelancer freelancer;

    @OneToOne(mappedBy = "usuario")
    private Empresa empresa;
    // getters, setters, etc.
}
