package com.example.ConnecTi.Projeto.Domain.Security.Configuration;


import com.example.ConnecTi.Projeto.Domain.Dto.Usuario.UsuarioDetalhesDto;
import com.example.ConnecTi.Projeto.Domain.Exception.NaoAutorizadoException;
import com.example.ConnecTi.Projeto.Domain.Repository.RepositoryUsuario;
import com.example.ConnecTi.Projeto.Model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private RepositoryUsuario usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(username);

        if(usuarioOpt.isEmpty()){
            throw new UsernameNotFoundException(String.format("O email %s não foi encontrado", username));
        }

        return new UsuarioDetalhesDto(usuarioOpt.get());
    }
    public Usuario getUsuarioFromUsuarioDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null)
            return null;
        UsuarioDetalhesDto usuarioDetailsDto =  (UsuarioDetalhesDto) authentication.getPrincipal();

        Optional<Usuario> usuario = usuarioRepository.findById(usuarioDetailsDto.getId());
        return usuario.orElse(null);
    }
    public void verificarPapelFreelancer(Usuario usuario) {
        if (!"Freelancer".equalsIgnoreCase(usuario.getPapel())) {
            throw new NaoAutorizadoException("Não autorizado. O usuário não é um Freelancer.");
        }
    }

    public void verificarPapelEmpresa(Usuario usuario) {
        if (!"Empresa".equalsIgnoreCase(usuario.getPapel())) {
            throw new NaoAutorizadoException("Não autorizado. O usuário não é uma Empresa.");
        }
    }
}
