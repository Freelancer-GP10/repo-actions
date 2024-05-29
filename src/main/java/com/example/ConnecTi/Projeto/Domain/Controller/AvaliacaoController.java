package com.example.ConnecTi.Projeto.Domain.Controller;

import com.example.ConnecTi.Projeto.Domain.Repository.RepositoryAvaliacao;
import com.example.ConnecTi.Projeto.Model.Avaliacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/avaliacao")
public class AvaliacaoController {
    @Autowired
    private RepositoryAvaliacao repository;

    @PostMapping
    public ResponseEntity<Avaliacao> cadastrar(@RequestBody Avaliacao avaliacao){
        Avaliacao avaliacaoSalva = this.repository.save(avaliacao);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Avaliacao>> listar(){
        List<Avaliacao> avaliacoes = this.repository.findAll();
        if (avaliacoes.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(avaliacoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Avaliacao> litarPorId(@PathVariable Long id){
        Optional<Avaliacao> avaliacaoOpt = this.repository.findById(id);

        return ResponseEntity.of(avaliacaoOpt);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Avaliacao> atualizar(@PathVariable Long id,
                                               @RequestBody Avaliacao avaliacao){
          Avaliacao avaliacaoAtualizada = this.repository.getReferenceById(id);
            if (avaliacaoAtualizada != null){
                avaliacaoAtualizada.setNivel(avaliacao.getNivel());
                this.repository.save(avaliacaoAtualizada);
                return ResponseEntity.ok().build();
            }

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Avaliacao> deletar(@PathVariable Long id){
        Optional<Avaliacao> avaliacaoOpt = this.repository.findById(id);
        if (avaliacaoOpt.isPresent()){
            this.repository.deleteById(id);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.noContent().build();
    }
}
