package com.example.pedidosYA.Service;

import com.example.pedidosYA.DTO.AdminDTO.AdminDetailDTO;
import com.example.pedidosYA.DTO.AdminDTO.AdminRequestDTO;
import com.example.pedidosYA.Model.Admin;
import com.example.pedidosYA.Repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AdminDetailDTO crearAdmin(AdminRequestDTO req) {

        Admin a = new Admin();
        a.setUsuario(req.getUsuario());
        a.setContrasenia(passwordEncoder.encode(req.getContrasenia()));

        Admin adminGuardado = adminRepository.save(a);

        return new AdminDetailDTO(
                adminGuardado.getId(), adminGuardado.getUsuario(), adminGuardado.getContrasenia()
        );
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        Admin admin = adminRepository.findByUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return new org.springframework.security.core.userdetails.User(
                admin.getUsuario(),
                admin.getContrasenia(),
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
    }

}
