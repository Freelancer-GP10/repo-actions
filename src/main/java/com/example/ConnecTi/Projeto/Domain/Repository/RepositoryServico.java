package com.example.ConnecTi.Projeto.Domain.Repository;

import com.example.ConnecTi.Projeto.Model.Freelancer;
import com.example.ConnecTi.Projeto.Model.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RepositoryServico extends JpaRepository<Servico,Long> {

//    @Query("SELECT s.valor, ss.status FROM Conexao c JOIN c.servico s JOIN s.statusServico ss WHERE c.freelancer.idFreelancer = :idFreelancerLogado")
//    Servico findServiceValueAndStatusByFreelancerId(@Param("idFreelancerLogado") Long idFreelancerLogado);

    List<Servico> findAllByOrderByDataDePostagemAsc();

    @Query("SELECT c.servico FROM Conexao c WHERE c.freelancer = :freelancer AND c.servico.status = 'PENDENTE'")
    List<Servico> findServicosPendentesByFreelancer(Freelancer freelancer);
    @Query("SELECT c.servico FROM Conexao c WHERE c.freelancer = :freelancer AND c.servico.status = 'FINALIZADO'")
    List<Servico> findServicosFinalizadosByFreelancer(Freelancer freelancer);



}
