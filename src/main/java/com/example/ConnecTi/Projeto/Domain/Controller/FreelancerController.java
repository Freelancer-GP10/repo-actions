package com.example.ConnecTi.Projeto.Domain.Controller;

import com.example.ConnecTi.Projeto.Domain.Repository.RepositoryFreelancer;
import com.example.ConnecTi.Projeto.Domain.Dto.Freelancer.AtualizarFreelancerDto;
import com.example.ConnecTi.Projeto.Domain.Dto.Freelancer.CadastrarFreelaDto;
import com.example.ConnecTi.Projeto.Domain.Dto.Freelancer.ListarFreelaDto;

import com.example.ConnecTi.Projeto.Domain.Repository.RepositoryServico;
import com.example.ConnecTi.Projeto.Domain.Repository.RepositoryStatuServico;
import com.example.ConnecTi.Projeto.Domain.Repository.RepositoryUsuario;
import com.example.ConnecTi.Projeto.Domain.Security.Configuration.AutenticacaoService;
import com.example.ConnecTi.Projeto.Model.Freelancer;

import com.example.ConnecTi.Projeto.Model.Usuario;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/freelancer")
@RequiredArgsConstructor
public class FreelancerController {
    private final RepositoryFreelancer repository;
    private final RepositoryUsuario repositoryUsuario;
    private final AutenticacaoService autenticacaoService;


    @PostMapping
    public ResponseEntity<Freelancer> cadastrarFreelancer(@RequestBody @Valid CadastrarFreelaDto dto) {
        Usuario usuariologado = autenticacaoService.getUsuarioFromUsuarioDetails();

        // Verifica se o papel do usuário é "Freelancer"
      //  autenticacaoService.verificarPapelFreelancer(usuariologado);

        Freelancer freelancer = repository.findByEmail(usuariologado.getEmail());
        freelancer.setNome(dto.nome());
        freelancer.setEmail(usuariologado.getEmail());
        freelancer.setSobrenome(dto.sobrenome());
        freelancer.setTelefone(dto.telefone());
        freelancer.setSenha(usuariologado.getSenha());
        freelancer.setFormacao(dto.formacao());
        freelancer.setAreaAtuacao(dto.areaAtuacao());
        freelancer.setLinguagemDominio(dto.linguagemDominio());
        freelancer.setCpf(dto.cpf());
        freelancer.setAtivo(true);
        repository.save(freelancer);
        return ResponseEntity.ok(freelancer);

    }
    @GetMapping
    public ResponseEntity<List<ListarFreelaDto>> listarFreelancer(){
        Usuario usuariologado = autenticacaoService.getUsuarioFromUsuarioDetails();

        autenticacaoService.verificarPapelFreelancer(usuariologado);
        List<Freelancer> freelancers = repository.findAll();
        List<ListarFreelaDto> listarFreelaDtos = freelancers.stream().map(freelancer -> new ListarFreelaDto(freelancer.getIdFreelancer(),freelancer.getNome(),freelancer.getEmail(),freelancer.getCpf())).collect(Collectors.toList());

        return ResponseEntity.ok(listarFreelaDtos);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ListarFreelaDto> buscarFreelancer(@PathVariable int id){
        Freelancer freelancer = repository.findById((long) id).orElse(null);
        if(freelancer == null){
            return ResponseEntity.notFound().build();
        }
        var retornar =new ListarFreelaDto(freelancer.getIdFreelancer(),freelancer.getNome(),freelancer.getEmail(),freelancer.getCpf());
        return ResponseEntity.ok(retornar);
    }
    @PutMapping
    public ResponseEntity<Freelancer> atualizarFreelancer(@RequestBody AtualizarFreelancerDto dto){
        Usuario usuario = autenticacaoService.getUsuarioFromUsuarioDetails();
        //Optional<Usuario> usuario = repositoryUsuario.findByEmail(dto.email());

        Freelancer freelancer = repository.findByUsuarioEmail(usuario.getEmail()).orElse(null);
        if(freelancer == null){
            return ResponseEntity.notFound().build();
        }
        usuario.setEmail(dto.email());
        freelancer.setTelefone(dto.telefone());
        freelancer.setAreaAtuacao(dto.areaAtuacao());
        freelancer.setLinguagemDominio(dto.dominio());
        freelancer.setNome(dto.nome());
        freelancer.setEmail(dto.email());
        freelancer.setSenha(dto.senha());

        repository.save(freelancer);
        return ResponseEntity.ok()
                .header("Update-Email", "true")
                .body(freelancer);
    }
    @PutMapping("/deletar")
    public ResponseEntity<Freelancer> deletarFreelancer(){
        Usuario usuario = autenticacaoService.getUsuarioFromUsuarioDetails();
        Freelancer freelancer = repository.findByUsuarioEmail(usuario.getEmail()).orElse(null);
        if(freelancer == null){
            return ResponseEntity.notFound().build();
        }
        freelancer.setAtivo(false);
        repository.save(freelancer);

        return ResponseEntity.noContent().build();
    }


}
