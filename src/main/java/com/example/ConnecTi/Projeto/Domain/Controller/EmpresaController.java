package com.example.ConnecTi.Projeto.Domain.Controller;

import com.example.ConnecTi.Projeto.Domain.Dto.Empresa.AtualizarEmpresaDto;
import com.example.ConnecTi.Projeto.Domain.Dto.Empresa.CadastrarEmpresaDto;
import com.example.ConnecTi.Projeto.Domain.Dto.Empresa.ListarEmpresaDto;
import com.example.ConnecTi.Projeto.Domain.Repository.RepositoryFreelancer;
import com.example.ConnecTi.Projeto.Domain.Repository.RepositoryServico;
import com.example.ConnecTi.Projeto.Domain.Repository.RepostioryEmpresa;
import com.example.ConnecTi.Projeto.Domain.Security.Configuration.AutenticacaoService;
import com.example.ConnecTi.Projeto.Model.Empresa;
import com.example.ConnecTi.Projeto.Model.Freelancer;
import com.example.ConnecTi.Projeto.Model.Usuario;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/empresa")
public class EmpresaController {

    @Autowired
    private RepostioryEmpresa repositoryEmpresa;
    @Autowired
    private AutenticacaoService autenticacaoService;
    @Autowired
    private RepositoryServico servico;
    @Autowired
    private RepositoryFreelancer repositoryFreelancer;

    @PostMapping
    public ResponseEntity<Empresa> cadastrarEmpresa(@RequestBody @Valid CadastrarEmpresaDto dto) {
        Usuario usuarioLogado = autenticacaoService.getUsuarioFromUsuarioDetails();
        // Verifica se o papel do usuário é "Empresa"
      //  autenticacaoService.verificarPapelEmpresa(usuarioLogado);

        Empresa empresa = repositoryEmpresa.findByEmail(usuarioLogado.getEmail()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não existe uma empresa com esse email"));


        System.out.println(dto.nome());
        empresa.setNome(dto.nome());
        empresa.setEmail(usuarioLogado.getEmail());
        empresa.setSenha(usuarioLogado.getSenha());
        empresa.setCnpj(dto.cnpj());
        System.out.println(dto.cnpj());
        empresa.setRamo(dto.ramo());
        empresa.setTelefone(dto.telefone());
        repositoryEmpresa.save(empresa);

        return ResponseEntity.ok(empresa);
    }
    @GetMapping
    public ResponseEntity<List<ListarEmpresaDto>> listarEmpresa(){
        Usuario usuarioLogado = autenticacaoService.getUsuarioFromUsuarioDetails();
        autenticacaoService.verificarPapelEmpresa(usuarioLogado);
        List<Empresa> empresas = repositoryEmpresa.findAll();
        List<ListarEmpresaDto> listarEmpresaDtos = empresas.stream().map(empresa -> new ListarEmpresaDto(empresa.getIdEmpresa(),empresa.getNome(),empresa.getEmail())).collect(Collectors.toList());
        System.out.println(listarEmpresaDtos);
        return ResponseEntity.ok(listarEmpresaDtos);

    }
    @GetMapping("/{id}")
    public ResponseEntity<ListarEmpresaDto> listarPorId(@PathVariable int id){
        Empresa empresa = repositoryEmpresa.findById((long) id).orElse(null);
        if(empresa == null){
            return ResponseEntity.notFound().build();
        }
        var retornar = new ListarEmpresaDto(empresa.getIdEmpresa(),empresa.getNome(),empresa.getEmail());
        return ResponseEntity.ok(retornar);
    }

    @PutMapping
    public ResponseEntity<Empresa> atualizar(@RequestBody AtualizarEmpresaDto dto){
        Usuario usuario = autenticacaoService.getUsuarioFromUsuarioDetails();
        Empresa empresa= repositoryEmpresa.findByEmail(usuario.getEmail()).orElse(null);
        if(empresa == null){
            return ResponseEntity.notFound().build();
        }
        usuario.setEmail(dto.email());
        empresa.setNome(dto.nome());
        empresa.setCnpj(dto.cnpj());
        empresa.setEmail(dto.email());
        empresa.setSenha(dto.senha());
        empresa.setRamo(dto.ramo());
        empresa.setTelefone(dto.telefone());

        repositoryEmpresa.save(empresa);
        return ResponseEntity.ok(empresa);
    }
    @DeleteMapping
    public ResponseEntity<Empresa> desativar(){
        Usuario usuario = autenticacaoService.getUsuarioFromUsuarioDetails();
        Empresa empresa = repositoryEmpresa.findByEmail(usuario.getEmail()).orElse(null);
        if(empresa == null){
            return ResponseEntity.notFound().build();
        }

        repositoryEmpresa.delete(empresa);

        return ResponseEntity.noContent().build();
    }



}
