package com.example.pedidosYA.Model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends Usuario{

    public Admin() {
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return this.getContrasenia();
    }

    @Override
    public String getUsername() {
        return this.getUsuario();
    }
}
