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
        Usuario user = usuarioRepo.findByUsuario(usuario);

        if(user == null)
        {
            throw new UsernameNotFoundException("No existe ese usuario");
        }

        return user;
    }


    public boolean existsByUsername(String username) {
        return usuarioRepo.findByUsuario(username) != null;
    }

    public void save(Usuario usuario) {
        usuarioRepo.save(usuario);
    }
}


