package com.example.wellthy4life.controllers;

import com.example.wellthy4life.dto.SupportMessageDTO;
import com.example.wellthy4life.models.User;
import com.example.wellthy4life.repositories.UserRepository;
import com.example.wellthy4life.services.SupportMessageService;
import com.example.wellthy4life.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/support")
@CrossOrigin(origins = "http://localhost:3000")
public class SupportMessageController {

    @Autowired
    private SupportMessageService supportMessageService;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/send")
    public ResponseEntity<SupportMessageDTO> send(@RequestBody SupportMessageDTO dto,
                                                  @RequestHeader("Authorization") String token) {
        String email = jwtUtil.getUsernameFromToken(token.replace("Bearer ", ""));
        User user = userRepository.findByEmail(email);
        dto.setUserId(user.getId());
        return ResponseEntity.ok(supportMessageService.send(dto));
    }

    @GetMapping("/admin/all")
    public ResponseEntity<List<SupportMessageDTO>> all() {
        return ResponseEntity.ok(supportMessageService.getMessagesForAdmin());
    }

    @GetMapping("/user")
    public ResponseEntity<List<SupportMessageDTO>> mine(@RequestHeader("Authorization") String token) {
        String email = jwtUtil.getUsernameFromToken(token.replace("Bearer ", ""));
        User user = userRepository.findByEmail(email);
        return ResponseEntity.ok(supportMessageService.getMessagesByUser(user.getId()));
    }

    @PutMapping("/admin/read/{id}")
    public ResponseEntity<Void> read(@PathVariable Long id) {
        supportMessageService.markAsRead(id);
        return ResponseEntity.noContent().build();
    }
}
