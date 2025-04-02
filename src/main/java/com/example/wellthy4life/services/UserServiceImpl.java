package com.example.wellthy4life.services;

import com.example.wellthy4life.dto.UserDTO;
import com.example.wellthy4life.models.*;
import com.example.wellthy4life.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String registerUser(UserDTO userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            throw new RuntimeException("Adresa de email este deja utilizată.");
        }

        User user = new User();
        user.setFullName(userDTO.getFullName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setBirthDate(userDTO.getBirthDate());

        userRepository.save(user);

        String roleToAssign = "USER";

        if ("ADMIN".equalsIgnoreCase(userDTO.getRequestedRole())) {
            if (!RoleCodes.ADMIN_CODE.equals(userDTO.getAccessCode())) {
                throw new RuntimeException("Cod invalid pentru rolul ADMIN.");
            }
            roleToAssign = "ADMIN";
        } else if ("DOCTOR".equalsIgnoreCase(userDTO.getRequestedRole())) {
            if (!RoleCodes.DOCTOR_CODE.equals(userDTO.getAccessCode())) {
                throw new RuntimeException("Cod invalid pentru rolul DOCTOR.");
            }
            roleToAssign = "DOCTOR";
        }

        Role role = roleRepository.findByName(roleToAssign);
        if (role == null) {
            throw new RuntimeException("Rolul nu există: " + roleToAssign);
        }

        userRoleRepository.save(new UserRole(user, role));

        return "User registered successfully.";
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserDTO dto = new UserDTO();
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setBirthDate(user.getBirthDate());
        return dto;
    }
}
