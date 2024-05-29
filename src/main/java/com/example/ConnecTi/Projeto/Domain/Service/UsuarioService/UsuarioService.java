package com.example.ConnecTi.Projeto.Domain.Service.UsuarioService;

import com.example.ConnecTi.Projeto.Domain.Dto.Usuario.UsuarioCriacaoDto;
import com.example.ConnecTi.Projeto.Domain.Dto.Usuario.UsuarioLoginDto;
import com.example.ConnecTi.Projeto.Domain.Dto.Usuario.UsuarioMapper;
import com.example.ConnecTi.Projeto.Domain.Dto.Usuario.UsuarioTokenDto;
import com.example.ConnecTi.Projeto.Domain.Exception.NaoAutorizadoException;
import com.example.ConnecTi.Projeto.Domain.Repository.RepositoryFreelancer;
import com.example.ConnecTi.Projeto.Domain.Repository.RepositoryUsuario;
import com.example.ConnecTi.Projeto.Domain.Repository.RepostioryEmpresa;
import com.example.ConnecTi.Projeto.Domain.Security.Jwt.GerenciadorTokenJwt;
import com.example.ConnecTi.Projeto.Model.Empresa;
import com.example.ConnecTi.Projeto.Model.Freelancer;
import com.example.ConnecTi.Projeto.Model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private GerenciadorTokenJwt gerenciadorTokenJwt;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RepositoryUsuario repositoryUsuario;
    @Autowired
    private RepositoryUsuario usuarioRepository;

    @Autowired
    private RepositoryFreelancer freelancerRepository;
    @Autowired
    private RepostioryEmpresa empresaRepository;

//    public void criarUsuario(UsuarioCriacaoDto usuarioCriacaoDto) {
//        final Usuario novoUsuario = UsuarioMapper.of(usuarioCriacaoDto);
//        String senhaCriptografada =
//                passwordEncoder.encode(novoUsuario.getSenha());
//                novoUsuario.setSenha(senhaCriptografada);
//        repositoryUsuario.save(novoUsuario);
//    }

    public Usuario criarUsuario(UsuarioCriacaoDto usuarioCriacaoDto) {
        // Criando o usuário
        final Usuario novoUsuario = UsuarioMapper.of(usuarioCriacaoDto);
        String senhaCriptografada = passwordEncoder.encode(novoUsuario.getSenha());
        novoUsuario.setEmail(usuarioCriacaoDto.getEmail());
        novoUsuario.setSenha(senhaCriptografada);
        novoUsuario.setPapel(usuarioCriacaoDto.getPapel());
        Usuario usuarioSalvo = usuarioRepository.save(novoUsuario);

        // Verificando o papel para criar Freelancer ou Empresa
        if ("Freelancer".equalsIgnoreCase(usuarioCriacaoDto.getPapel())) {
            Freelancer freelancer = new Freelancer();
           // freelancer.setNome(usuarioSalvo.getEmail()); // ajuste conforme necessário
            freelancer.setEmail(usuarioSalvo.getEmail());
            freelancer.setSenha(usuarioSalvo.getSenha());
            freelancer.setUsuario(usuarioSalvo);
            freelancerRepository.save(freelancer);

        } else if ("Empresa".equalsIgnoreCase(usuarioCriacaoDto.getPapel())) {
            Empresa empresa = new Empresa();
        //    empresa.setNome(usuarioSalvo.getEmail()); // ajuste conforme necessário
            empresa.setEmail(usuarioSalvo.getEmail());
            empresa.setSenha(usuarioSalvo.getSenha());
            empresa.setUsuario(usuarioSalvo);
            empresaRepository.save(empresa);

        }
        return novoUsuario;
    }
    public UsuarioTokenDto autenticar(UsuarioLoginDto usuarioLoginDto){
        System.out.println(usuarioLoginDto.getEmail());
        System.out.println(usuarioLoginDto.getSenha());
        final UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(
                usuarioLoginDto.getEmail(),
                usuarioLoginDto.getSenha()
        );
        final Authentication authentication = authenticationManager.authenticate(credentials);

        Usuario usuarioAutenticado= repositoryUsuario.findByEmail(usuarioLoginDto.getEmail()).orElseThrow(()-> new RuntimeException("Usuário não encontrado"));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = gerenciadorTokenJwt.generateToken(authentication);
        return UsuarioMapper.of(usuarioAutenticado, token);
    }

}
