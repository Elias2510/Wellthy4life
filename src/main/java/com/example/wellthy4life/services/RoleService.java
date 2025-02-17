package com.example.wellthy4life.services;

import com.example.wellthy4life.dto.RoleDTO;
import com.example.wellthy4life.models.Role;
import com.example.wellthy4life.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public String addRole(RoleDTO roleDTO) {
        Role role = new Role(roleDTO.getName());
        roleRepository.save(role);
        return "Role added successfully!";
    }

    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(role -> new RoleDTO(role.getName()))
                .collect(Collectors.toList());
    }
}
