package com.example.wellthy4life.services;

import com.example.wellthy4life.dto.SupportMessageDTO;
import com.example.wellthy4life.models.SupportMessage;
import com.example.wellthy4life.models.User;
import com.example.wellthy4life.repositories.SupportMessageRepository;
import com.example.wellthy4life.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupportMessageServiceImpl implements SupportMessageService {

    @Autowired
    private SupportMessageRepository supportMessageRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public SupportMessageDTO send(SupportMessageDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        SupportMessage msg = new SupportMessage();
        msg.setUser(user);
        msg.setSubject(dto.getSubject());
        msg.setContent(dto.getContent());

        supportMessageRepository.save(msg);

        dto.setId(msg.getId());
        dto.setCreatedAt(msg.getCreatedAt());
        dto.setRead(false);
        dto.setUserEmail(user.getEmail());

        return dto;
    }

    @Override
    public List<SupportMessageDTO> getMessagesForAdmin() {
        return supportMessageRepository.findAll().stream().map(this::convert).collect(Collectors.toList());
    }

    @Override
    public List<SupportMessageDTO> getMessagesByUser(Long userId) {
        return supportMessageRepository.findByUserId(userId).stream().map(this::convert).collect(Collectors.toList());
    }

    @Override
    public void markAsRead(Long id) {
        SupportMessage msg = supportMessageRepository.findById(id).orElseThrow();
        msg.setRead(true);
        supportMessageRepository.save(msg);
    }

    private SupportMessageDTO convert(SupportMessage msg) {
        SupportMessageDTO dto = new SupportMessageDTO();
        dto.setId(msg.getId());
        dto.setUserId(msg.getUser().getId());
        dto.setUserEmail(msg.getUser().getEmail());
        dto.setSubject(msg.getSubject());
        dto.setContent(msg.getContent());
        dto.setRead(msg.isRead());
        dto.setCreatedAt(msg.getCreatedAt());
        return dto;
    }
}
