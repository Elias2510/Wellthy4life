package com.example.wellthy4life.services;

import com.example.wellthy4life.dto.AnalysisDTO;
import com.example.wellthy4life.models.Analysis;
import com.example.wellthy4life.models.User;
import com.example.wellthy4life.repositories.AnalysisRepository;
import com.example.wellthy4life.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class AnalysisServiceImpl implements AnalysisService {

    @Autowired
    private AnalysisRepository analysisRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Analysis addAnalysis(AnalysisDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Analysis analysis = new Analysis();
        analysis.setUser(user);
        analysis.setTestName(dto.getTestName());
        analysis.setValue(dto.getValue());
        analysis.setUnit(dto.getUnit());
        analysis.setNormalMin(dto.getNormalMin());
        analysis.setNormalMax(dto.getNormalMax());
        analysis.setTestDate(dto.getTestDate());

        return analysisRepository.save(analysis);
    }

    @Override
    public Analysis updateAnalysis(Long id, AnalysisDTO dto) {
        Analysis analysis = analysisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Analysis not found"));

        analysis.setTestName(dto.getTestName());
        analysis.setValue(dto.getValue());
        analysis.setUnit(dto.getUnit());
        analysis.setNormalMin(dto.getNormalMin());
        analysis.setNormalMax(dto.getNormalMax());
        analysis.setTestDate(dto.getTestDate());

        return analysisRepository.save(analysis);
    }

    @Override
    public void deleteAnalysis(Long id) {
        if (analysisRepository.existsById(id)) {
            analysisRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Analiza nu a fost găsită.");
        }
    }

    @Override
    public List<Analysis> getAnalysesByUser(Long userId) {
        return analysisRepository.findByUserId(userId);
    }

    @Override
    public void processPdfAndSave(MultipartFile file, User user) throws IOException {
        int addedCount = 0;
        int ignoredCount = 0;
        LocalDate currentDateForBlock = LocalDate.now(); // fallback

        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            String[] lines = text.split("\\r?\\n");

            for (String line : lines) {
                // Caută dată în linie (formate uzuale)
                if (line.toLowerCase().contains("data")) {
                    try {
                        String dateStr = line.replaceAll("[^0-9.\\-]", ""); // elimină text
                        if (dateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
                            currentDateForBlock = LocalDate.parse(dateStr);
                        } else if (dateStr.matches("\\d{2}\\.\\d{2}\\.\\d{4}")) {
                            String[] parts = dateStr.split("\\.");
                            currentDateForBlock = LocalDate.of(
                                    Integer.parseInt(parts[2]),
                                    Integer.parseInt(parts[1]),
                                    Integer.parseInt(parts[0])
                            );
                        }
                    } catch (Exception ignored) {
                    }
                    continue; // skip linia dacă era doar data
                }

                if (line.contains(":") && line.contains("normal")) {
                    try {
                        String[] parts = line.split(":");
                        String testName = parts[0].trim();

                        String valuePart = parts[1].split("\\(")[0].trim();
                        String unit = valuePart.replaceAll("[0-9.\\s]", "");
                        double value = Double.parseDouble(valuePart.replaceAll("[^0-9.]", ""));

                        String normalRange = line.substring(line.indexOf("normal:") + 7).replaceAll("[^0-9.–-]", "");
                        String[] rangeParts = normalRange.split("[–-]");
                        double normalMin = Double.parseDouble(rangeParts[0]);
                        double normalMax = Double.parseDouble(rangeParts[1]);

                        Analysis analysis = new Analysis();
                        analysis.setUser(user);
                        analysis.setTestName(testName);
                        analysis.setValue(value);
                        analysis.setUnit(unit);
                        analysis.setNormalMin(normalMin);
                        analysis.setNormalMax(normalMax);
                        analysis.setTestDate(currentDateForBlock); // aici folosim data curentă din context

                        analysisRepository.save(analysis);
                        addedCount++;
                    } catch (Exception ex) {
                        ignoredCount++;
                    }
                }
            }
        }

        if (addedCount == 0) {
            throw new RuntimeException("Nicio analiză validă nu a fost găsită în PDF.");
        }

        throw new RuntimeException("PROCESARE_OK|" + addedCount + "|" + ignoredCount);
    }

}

