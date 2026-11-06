package br.com.agservpro.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.io.Serializable;

public record AgServProUserDetails(
        UUID id,
        String email,
        String password,
        String status,
        String papelPlataforma) implements UserDetails, Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if ("ADMIN_SAAS".equals(papelPlataforma)) {
            return List.of(new SimpleGrantedAuthority("ADMIN_SAAS"));
        }
        return List.of(new SimpleGrantedAuthority("USUARIO_PRESTADORA"));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password == null ? "" : password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !"SUSPENSO".equals(status);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return "ATIVO".equals(status);
    }
}
