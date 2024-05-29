package com.example.ConnecTi.Projeto.Domain.Dto.Usuario;

import com.example.ConnecTi.Projeto.Enum.Papeis;
import com.example.ConnecTi.Projeto.Model.Usuario;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
@Getter
@Setter
public class UsuarioDetalhesDto implements UserDetails {
    private  Long id;
    private final String email;

    private final String senha;

    private final String papel;

    public UsuarioDetalhesDto(Usuario usuario) {
        this.id=usuario.getId();
        this.email = usuario.getEmail();
        this.senha = usuario.getSenha();
        this.papel = usuario.getPapel();
    }

    public String getPapel() {
        return papel;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
