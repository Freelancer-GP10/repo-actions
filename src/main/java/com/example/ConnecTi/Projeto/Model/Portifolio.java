package com.example.ConnecTi.Projeto.Model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;
@AllArgsConstructor

@Getter
@Setter
@Entity
public class  Portifolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPortifolio;

    private File arquivo;
    private String linkRepositorio;
    @OneToOne
    @JoinColumn(name = "fkFreelancer", referencedColumnName = "idFreelancer")
    @JsonManagedReference
    private Freelancer freelancer;

    public Portifolio() {
    }
}
