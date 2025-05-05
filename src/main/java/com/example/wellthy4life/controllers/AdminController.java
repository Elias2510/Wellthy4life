package com.example.wellthy4life.controllers;

import com.example.wellthy4life.dto.UserDTO;
import com.example.wellthy4life.models.Role;
import com.example.wellthy4life.models.User;
import com.example.wellthy4life.repositories.AnalysisRepository;
import com.example.wellthy4life.repositories.RoleRepository;
import com.example.wellthy4life.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnalysisRepository analysisRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userRepository.findAll().stream().map(user -> {
            UserDTO dto = new UserDTO();
            dto.setId(user.getId()); // Adaugă asta în getAllUsers()

            dto.setFullName(user.getFullName());
            dto.setEmail(user.getEmail());
            dto.setBirthDate(user.getBirthDate());
            dto.setRequestedRole(user.getRoles().stream().findFirst().map(Role::getName).orElse("USER"));
            return dto;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/user/{id}")
    @Transactional
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/analysis/{id}")
    public ResponseEntity<Void> deleteAnalysis(@PathVariable Long id) {
        analysisRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/user/add")
    public ResponseEntity<String> addUser(@RequestBody UserDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            return ResponseEntity.badRequest().body("Emailul este deja folosit.");
        }

        User user = new User();
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setBirthDate(dto.getBirthDate());

        String roleName = dto.getRequestedRole() != null ? dto.getRequestedRole() : "USER";
        Role role = roleRepository.findByName(roleName);
        if (role == null) {
            role = roleRepository.save(new Role(roleName));
        }
        user.setRoles(Set.of(role));

        userRepository.save(user);

        return ResponseEntity.ok("Utilizator adăugat cu succes.");
    }

}
