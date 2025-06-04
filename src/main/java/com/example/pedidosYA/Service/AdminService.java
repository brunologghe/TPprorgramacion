package com.example.pedidosYA.Service;

import com.example.pedidosYA.DTO.AdminDTO.AdminRequestDTO;
import com.example.pedidosYA.Model.Admin;
import com.example.pedidosYA.Repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    public Admin registrarAdmin(AdminRequestDTO adminRequestDTO) {

        Admin admin = new Admin();
        admin.setUsuario(adminRequestDTO.getUsuario());
        admin.setContrasenia(admin.getContrasenia());

        return adminRepository.save(admin);
    }

}
