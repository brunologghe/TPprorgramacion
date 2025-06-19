package com.example.pedidosYA.Service;

import com.example.pedidosYA.Model.Usuario;
import com.example.pedidosYA.Repository.UsuarioRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepo;

    public CustomUserDetailsService(UsuarioRepository usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String usuario) throws UsernameNotFoundException {
        Usuario user = usuarioRepo.findByUsuario(usuario)
                .orElseThrow(() -> new UsernameNotFoundException("No existe el usuario"));
/*
        String rolConPrefijo = "ROLE_" + user.getRol().name();

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(rolConPrefijo));


        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getAuthorities() // debe implementar roles como GrantedAuthority
        );*/
        System.out.println("Usuario encontrado: " + user.getUsername());
        System.out.println("Rol: " + user.getRol());
        System.out.println("Authorities: " + user.getAuthorities());

        return user;
    }

    public Usuario findByEmail(String usuario) {
        return usuarioRepo.findByUsuario(usuario).orElseThrow();
    }

    public boolean existsByUsername(String username) {
        return usuarioRepo.findByUsuario(username).isPresent();
    }

    public void save(Usuario usuario) {
        usuarioRepo.save(usuario);
    }
}


