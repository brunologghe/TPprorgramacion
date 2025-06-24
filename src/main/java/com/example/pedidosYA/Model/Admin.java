package com.example.pedidosYA.Model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends Usuario{

    public Admin() {
    }

}
