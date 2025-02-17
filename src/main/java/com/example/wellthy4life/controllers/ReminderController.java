package com.example.wellthy4life.controllers;

import com.example.wellthy4life.dto.ReminderDTO;
import com.example.wellthy4life.models.Reminder;
import com.example.wellthy4life.services.ReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reminders")
@CrossOrigin(origins = "http://localhost:3000")
public class ReminderController {
    @Autowired
    private ReminderService reminderService;

    @PostMapping("/add")
    public ResponseEntity<ReminderDTO> addReminder(@RequestBody ReminderDTO dto) {
        Reminder reminder = reminderService.addReminder(dto);
        ReminderDTO responseDto = new ReminderDTO(reminder.getUser().getId(), reminder.getAnalysis().getId(), reminder.getReminderDate(), reminder.getStatus());
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReminderDTO>> getUserReminders(@PathVariable Long userId) {
        return ResponseEntity.ok(reminderService.getUserReminders(userId));
    }
}
