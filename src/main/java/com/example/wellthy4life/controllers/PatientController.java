package com.example.wellthy4life.controllers;

import com.example.wellthy4life.dto.AnalysisDTO;
import com.example.wellthy4life.dto.PatientDTO;
import com.example.wellthy4life.models.Analysis;
import com.example.wellthy4life.models.User;
import com.example.wellthy4life.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "http://localhost:3000")
public class PatientController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/all")
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        List<User> allUsers = userRepository.findAll();

        List<PatientDTO> patients = allUsers.stream()
                .filter(user -> user.getRoles().stream()
                        .anyMatch(role -> role.getName().equals("USER")))
                .map(user -> new PatientDTO(user.getId(), user.getFullName(), user.getBirthDate()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(patients);
    }
    @GetMapping("/{id}/analyses")
    public ResponseEntity<List<AnalysisDTO>> getAnalysesForPatient(@PathVariable Long id) {
        List<Analysis> analyses = userRepository.findById(id)
                .map(user -> user.getAnalyses()) // presupunem că ai o relație @OneToMany în User
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<AnalysisDTO> dtoList = analyses.stream()
                .map(a -> new AnalysisDTO(
                        a.getUser().getId(),
                        a.getTestName(),
                        a.getValue(),
                        a.getUnit(),
                        a.getNormalMin(),
                        a.getNormalMax(),
                        a.getTestDate()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtoList);
    }

}