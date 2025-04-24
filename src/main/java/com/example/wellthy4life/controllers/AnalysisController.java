package com.example.wellthy4life.controllers;

import com.example.wellthy4life.dto.AnalysisDTO;
import com.example.wellthy4life.models.Analysis;
import com.example.wellthy4life.models.User;
import com.example.wellthy4life.repositories.AnalysisRepository;
import com.example.wellthy4life.repositories.UserRepository;
import com.example.wellthy4life.services.AnalysisService;
import com.example.wellthy4life.util.JWTUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/analyses")
@CrossOrigin(origins = "http://localhost:3000")
public class AnalysisController {

    @Autowired
    private AnalysisService analysisService;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnalysisRepository analysisRepository;

    @PostMapping("/add")
    public ResponseEntity<AnalysisDTO> addAnalysis(@RequestBody AnalysisDTO dto, @RequestHeader("Authorization") String token) {
        String email = jwtUtil.getUsernameFromToken(token.replace("Bearer ", ""));
        User user = userRepository.findByEmail(email);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Analysis analysis = new Analysis();
        analysis.setUser(user);
        analysis.setTestName(dto.getTestName());
        analysis.setValue(dto.getValue());
        analysis.setUnit(dto.getUnit());
        analysis.setNormalMin(dto.getNormalMin());
        analysis.setNormalMax(dto.getNormalMax());
        analysis.setTestDate(dto.getTestDate());

        analysisRepository.save(analysis);

        return ResponseEntity.ok(new AnalysisDTO(analysis.getId(), user.getId(), analysis.getTestName(), analysis.getValue(),
                analysis.getUnit(), analysis.getNormalMin(), analysis.getNormalMax(), analysis.getTestDate()));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AnalysisDTO> updateAnalysis(@PathVariable Long id, @RequestBody AnalysisDTO dto) {
        Analysis updatedAnalysis = analysisService.updateAnalysis(id, dto);
        return ResponseEntity.ok(new AnalysisDTO(
                updatedAnalysis.getId(),
                updatedAnalysis.getUser().getId(),
                updatedAnalysis.getTestName(),
                updatedAnalysis.getValue(),
                updatedAnalysis.getUnit(),
                updatedAnalysis.getNormalMin(),
                updatedAnalysis.getNormalMax(),
                updatedAnalysis.getTestDate()));
    }

    @PostMapping("/upload-pdf")
    public ResponseEntity<String> uploadPdf(@RequestParam("file") MultipartFile file,
                                            @RequestHeader("Authorization") String token) {
        try {
            String email = jwtUtil.getUsernameFromToken(token.replace("Bearer ", ""));
            User user = userRepository.findByEmail(email);

            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            analysisService.processPdfAndSave(file, user);
            return ResponseEntity.ok("PDF procesat cu succes.");
        } catch (RuntimeException ex) {
            if (ex.getMessage().startsWith("PROCESARE_OK")) {
                String[] parts = ex.getMessage().split("\\|");
                int added = Integer.parseInt(parts[1]);
                int ignored = Integer.parseInt(parts[2]);

                return ResponseEntity.ok("PDF procesat cu succes.\n- " + added + " analize adăugate\n- " + ignored + " linii ignorate");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Eroare la procesarea PDF-ului.");
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AnalysisDTO>> getAnalysesByUser(@PathVariable Long userId) {
        List<Analysis> analyses = analysisService.getAnalysesByUser(userId);
        List<AnalysisDTO> dtoList = analyses.stream().map(a ->
                new AnalysisDTO(a.getId(), a.getUser().getId(), a.getTestName(), a.getValue(), a.getUnit(), a.getNormalMin(), a.getNormalMax(), a.getTestDate())
        ).toList();
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/user")
    public ResponseEntity<List<AnalysisDTO>> getUserAnalyses(@RequestHeader("Authorization") String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = jwtUtil.getUsernameFromToken(token.replace("Bearer ", ""));
        User user = userRepository.findByEmail(email);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Analysis> analyses = analysisRepository.findByUserId(user.getId());
        List<AnalysisDTO> analysisDTOs = analyses.stream()
                .map(a -> new AnalysisDTO(a.getId(), a.getUser().getId(), a.getTestName(), a.getValue(), a.getUnit(), a.getNormalMin(), a.getNormalMax(), a.getTestDate()))
                .toList();

        return ResponseEntity.ok(analysisDTOs);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAnalysis(@PathVariable Long id) {
        try {
            analysisService.deleteAnalysis(id);
            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnalysisDTO> getAnalysisById(@PathVariable Long id) {
        Analysis analysis = analysisRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Analiza nu a fost găsită"));

        AnalysisDTO analysisDTO = new AnalysisDTO(
                analysis.getId(),
                analysis.getUser().getId(),
                analysis.getTestName(),
                analysis.getValue(),
                analysis.getUnit(),
                analysis.getNormalMin(),
                analysis.getNormalMax(),
                analysis.getTestDate()
        );

        return ResponseEntity.ok(analysisDTO);
    }
}
