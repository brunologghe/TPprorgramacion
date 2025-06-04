package com.example.pedidosYA.Service;

import com.example.pedidosYA.DTO.AdminDTO.AdminDetailDTO;
import com.example.pedidosYA.DTO.AdminDTO.AdminRequestDTO;
import com.example.pedidosYA.Model.Admin;
import com.example.pedidosYA.Repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

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

}
