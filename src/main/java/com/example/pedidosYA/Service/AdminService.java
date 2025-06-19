package com.example.pedidosYA.Service;

import com.example.pedidosYA.DTO.AdminDTO.AdminDetailDTO;
import com.example.pedidosYA.DTO.AdminDTO.AdminRequestDTO;
import com.example.pedidosYA.Model.Admin;
import com.example.pedidosYA.Model.RolUsuario;
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
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AdminDetailDTO crearAdmin(AdminRequestDTO req) {

        Admin a = new Admin();
        a.setUsuario(req.getUsuario());
        a.setContrasenia(passwordEncoder.encode(req.getContrasenia()));
        a.setRol(RolUsuario.ADMIN);

        Admin adminGuardado = adminRepository.save(a);

        return new AdminDetailDTO(
                adminGuardado.getId(), adminGuardado.getUsuario(), adminGuardado.getContrasenia()
        );
    }

    }


