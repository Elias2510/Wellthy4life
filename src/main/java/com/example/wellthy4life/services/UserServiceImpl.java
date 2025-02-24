package com.example.wellthy4life.services;

import com.example.wellthy4life.dto.UserDTO;
import com.example.wellthy4life.models.User;
import com.example.wellthy4life.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    // Asigură-te că ai definit și injectat un bean PasswordEncoder
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String registerUser(UserDTO userDTO) {
        // Verificăm dacă emailul este deja folosit
        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            throw new RuntimeException("Email is already in use.");
        }

        User user = new User();
        user.setFullName(userDTO.getFullName());
        user.setEmail(userDTO.getEmail());
        // Criptăm parola
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setBirthDate(userDTO.getBirthDate());

        userRepository.save(user);

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
