package com.example.ConnecTi.Projeto.Domain.Repository;

import com.example.ConnecTi.Projeto.Model.Freelancer;
import com.example.ConnecTi.Projeto.Model.Portifolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RepositoryPortifolio extends JpaRepository<Portifolio,Long> {


    @Query("select p from Portifolio p where p.freelancer.idFreelancer = ?1")
  Optional<Portifolio> getPortifolioByFreelancer(Long idFreelancer);




}
