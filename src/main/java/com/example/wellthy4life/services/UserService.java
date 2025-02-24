package com.example.wellthy4life.services;

import com.example.wellthy4life.dto.UserDTO;

public interface UserService {
    String registerUser(UserDTO userDTO);
    UserDTO getUserById(Long id);
}
