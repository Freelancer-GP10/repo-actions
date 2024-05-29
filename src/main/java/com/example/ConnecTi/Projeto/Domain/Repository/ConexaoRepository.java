package com.example.ConnecTi.Projeto.Domain.Repository;

import com.example.ConnecTi.Projeto.Model.Conexao;
import com.example.ConnecTi.Projeto.Model.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ConexaoRepository extends JpaRepository<Conexao,Long> {
    boolean existsByServico(Servico servico);

    @Query("SELECT c FROM Conexao c ORDER BY c.id DESC")
    List<Conexao> listarTodasConexoesOrdenadasPorIdDesc();
}
