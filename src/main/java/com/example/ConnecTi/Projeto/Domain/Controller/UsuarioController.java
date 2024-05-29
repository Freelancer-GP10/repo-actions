package com.example.ConnecTi.Projeto.Domain.Controller;

import com.example.ConnecTi.Projeto.Domain.Dto.Usuario.UsuarioCriacaoDto;
import com.example.ConnecTi.Projeto.Domain.Dto.Usuario.UsuarioDetalhesDto;
import com.example.ConnecTi.Projeto.Domain.Dto.Usuario.UsuarioLoginDto;
import com.example.ConnecTi.Projeto.Domain.Dto.Usuario.UsuarioTokenDto;
import com.example.ConnecTi.Projeto.Domain.Exception.EntidadeNaoEncontrada;
import com.example.ConnecTi.Projeto.Domain.Repository.RepositoryFreelancer;
import com.example.ConnecTi.Projeto.Domain.Repository.RepositoryUsuario;
import com.example.ConnecTi.Projeto.Domain.Repository.RepostioryEmpresa;
import com.example.ConnecTi.Projeto.Domain.Security.Configuration.AutenticacaoService;
import com.example.ConnecTi.Projeto.Domain.Service.UsuarioService.UsuarioService;
import com.example.ConnecTi.Projeto.Model.Empresa;
import com.example.ConnecTi.Projeto.Model.Freelancer;
import com.example.ConnecTi.Projeto.Model.Usuario;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.SecurityMarker;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final AutenticacaoService autenticacaoService;
    private final RepositoryFreelancer freelancerRepository;
    private final RepositoryUsuario usuarioRepository;
    private final RepostioryEmpresa empresaRepository;
    @PostMapping
    @SecurityRequirement(name="Bearer")
    public ResponseEntity<Usuario> criarUsuario(@RequestBody @Valid UsuarioCriacaoDto usuarioCriacaoDto){

        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuarioCriacaoDto.getEmail());
        if(usuarioExistente.isPresent()){
            throw new EntidadeNaoEncontrada("Já existe um usuário com esse email");
        }
        Usuario usuarioCriado = usuarioService.criarUsuario(usuarioCriacaoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCriado);
    }
    @PostMapping("/login")
    public ResponseEntity<UsuarioTokenDto> login(@RequestBody @Valid UsuarioLoginDto usuarioLoginDto){
        UsuarioTokenDto usuarioTokenDto = usuarioService.autenticar(usuarioLoginDto);
        return ResponseEntity.ok(usuarioTokenDto);
    }
    @GetMapping("/detalhes")
    public ResponseEntity<Freelancer> detalhes(){
        Usuario usuario = autenticacaoService.getUsuarioFromUsuarioDetails();
        Freelancer freelancer = freelancerRepository.findByEmail(usuario.getEmail());
        return ResponseEntity.ok(freelancer);
    }
    @GetMapping("/detalhes-micro")
    public ResponseEntity<Empresa> detalhesMicro(){
        Usuario usuario = autenticacaoService.getUsuarioFromUsuarioDetails();
        Empresa empresa = empresaRepository.findByEmail(usuario.getEmail()).orElseThrow(() -> new EntidadeNaoEncontrada("Não existe uma empresa com esse email"));
        return ResponseEntity.ok(empresa);
    }
    @GetMapping("/detalhes-usuario")
    public ResponseEntity<Freelancer> detalhesUsuario(){
        Usuario usuario = autenticacaoService.getUsuarioFromUsuarioDetails();
        Freelancer freelancer = freelancerRepository.findByEmail(usuario.getEmail());
        return ResponseEntity.ok(freelancer);
    }

    @GetMapping("/dados")
    public ResponseEntity<List<Usuario>> suaRota() {
        return ResponseEntity.ok(usuarioRepository.findAll());
    }


}
