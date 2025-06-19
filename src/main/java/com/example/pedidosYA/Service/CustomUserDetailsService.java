package com.example.pedidosYA.Service;

import com.example.pedidosYA.Model.Usuario;
import com.example.pedidosYA.Repository.UsuarioRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

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

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getAuthorities() // debe implementar roles como GrantedAuthority
        );
    }

    public Usuario findByEmail(String usuario) {
        return usuarioRepo.findByUsuario(usuario).orElseThrow();
    }
}


