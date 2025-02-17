package com.example.wellthy4life.controllers;

import com.example.wellthy4life.dto.RoleDTO;
import com.example.wellthy4life.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @PostMapping("/add")
    public String addRole(@RequestBody RoleDTO roleDTO) {
        return roleService.addRole(roleDTO);
    }

    @GetMapping("/all")
    public List<RoleDTO> getAllRoles() {
        return roleService.getAllRoles();
    }
}
