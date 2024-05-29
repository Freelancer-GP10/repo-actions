package com.example.ConnecTi.Projeto.Domain.Dto.Usuario;

import com.example.ConnecTi.Projeto.Model.Usuario;

public class UsuarioMapper {

    public static Usuario of(UsuarioCriacaoDto usuarioCriacaoDto) {
        Usuario usuario = new Usuario();
        usuario.setEmail(usuarioCriacaoDto.getEmail());
        usuario.setPapel(usuarioCriacaoDto.getPapel());
        usuario.setSenha(usuarioCriacaoDto.getSenha());
        return usuario;
    }

    public static UsuarioTokenDto of(Usuario usuario, String token) {
        UsuarioTokenDto usuarioTokenDto = new UsuarioTokenDto();
        usuarioTokenDto.setUserId(usuario.getId());
        usuarioTokenDto.setEmail(usuario.getEmail());
        usuarioTokenDto.setPapel(usuario.getPapel());
        usuarioTokenDto.setToken(token);
        return usuarioTokenDto;
    }
}

