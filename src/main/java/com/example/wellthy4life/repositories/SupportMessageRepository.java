package com.example.wellthy4life.repositories;

import com.example.wellthy4life.models.SupportMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupportMessageRepository extends JpaRepository<SupportMessage, Long> {
    List<SupportMessage> findByUserId(Long userId);
}
