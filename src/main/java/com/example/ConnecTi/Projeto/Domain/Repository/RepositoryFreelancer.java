package com.example.ConnecTi.Projeto.Domain.Repository;

import com.example.ConnecTi.Projeto.Model.Freelancer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface RepositoryFreelancer extends JpaRepository<Freelancer,Long> {


    Freelancer findByEmail(String email);
    Optional<Freelancer> findByUsuarioEmail(String email);

    @Query("SELECT f FROM Freelancer f WHERE f.usuario.id = :idUsuario")
    Freelancer findByFkUsuarioId(@Param("idUsuario") Long idUsuario);


}
