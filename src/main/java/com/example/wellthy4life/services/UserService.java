package com.example.wellthy4life.services;

import com.example.wellthy4life.dto.UserDTO;
import com.example.wellthy4life.models.Role;
import com.example.wellthy4life.models.User;
import com.example.wellthy4life.repositories.RoleRepository;
import com.example.wellthy4life.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Transactional
    public String registerUser(UserDTO userDTO) {
        // Verificăm dacă rolul există
        Optional<Role> optionalRole = roleRepository.findByName(userDTO.getRoleName());

        if (optionalRole.isEmpty()) {
            return "Role not found: " + userDTO.getRoleName();
        }

        Role role = optionalRole.get();

        // Creăm utilizatorul
        User user = new User();
        user.setFullName(userDTO.getFullName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setRoles(Collections.singleton(role));

        userRepository.save(user);
        return "User registered successfully with role: " + role.getName();
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return new UserDTO(user.getFullName(), user.getEmail(), user.getPassword(), user.getRoles().iterator().next().getName());
    }
}
