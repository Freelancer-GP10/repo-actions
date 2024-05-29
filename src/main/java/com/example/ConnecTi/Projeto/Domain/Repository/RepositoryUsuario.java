package com.example.ConnecTi.Projeto.Domain.Repository;

import com.example.ConnecTi.Projeto.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepositoryUsuario extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
}
