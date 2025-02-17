package com.example.wellthy4life.services;

import com.example.wellthy4life.dto.ReminderDTO;
import com.example.wellthy4life.models.Analysis;
import com.example.wellthy4life.models.Reminder;
import com.example.wellthy4life.models.User;
import com.example.wellthy4life.repositories.AnalysisRepository;
import com.example.wellthy4life.repositories.ReminderRepository;
import com.example.wellthy4life.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReminderService {
    @Autowired
    private ReminderRepository reminderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnalysisRepository analysisRepository;

    public Reminder addReminder(ReminderDTO dto) {
        User user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
        Analysis analysis = analysisRepository.findById(dto.getAnalysisId()).orElseThrow(() -> new RuntimeException("Analysis not found"));

        Reminder reminder = new Reminder();
        reminder.setUser(user);
        reminder.setAnalysis(analysis);
        reminder.setReminderDate(dto.getReminderDate());
        reminder.setStatus("PENDING"); // Setăm implicit statusul ca "PENDING"

        return reminderRepository.save(reminder);
    }

    public List<ReminderDTO> getUserReminders(Long userId) {
        List<Reminder> reminders = reminderRepository.findByUserId(userId);
        return reminders.stream()
                .map(r -> new ReminderDTO(r.getUser().getId(), r.getAnalysis().getId(), r.getReminderDate(), r.getStatus()))
                .collect(Collectors.toList());
    }
}
