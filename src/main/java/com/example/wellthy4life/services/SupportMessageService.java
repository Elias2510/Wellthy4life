package com.example.wellthy4life.services;

import com.example.wellthy4life.dto.SupportMessageDTO;

import java.util.List;

public interface SupportMessageService {

    SupportMessageDTO send(SupportMessageDTO dto);

    List<SupportMessageDTO> getMessagesForAdmin();

    List<SupportMessageDTO> getMessagesByUser(Long userId);

    void markAsRead(Long id);

    void deleteMessage(Long id);
}
