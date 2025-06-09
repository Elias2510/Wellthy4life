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

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        List<AnalysisDTO> results = new ArrayList<>();
        LocalDate date = LocalDate.now();

        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            String[] lines = text.split("\\r?\\n");

            System.out.println("[UPLOAD-PDF] Called");

            for (String line : lines) {
                if (line.toLowerCase().contains("data emiterii")) {
                    String raw = line.replaceAll(".*?([0-9]{4}-[0-9]{2}-[0-9]{2}|[0-9]{2}\\.[0-9]{2}\\.[0-9]{4}).*", "$1");
                    if (raw.matches("\\d{4}-\\d{2}-\\d{2}")) {
                        date = LocalDate.parse(raw);
                    } else if (raw.matches("\\d{2}\\.\\d{2}\\.\\d{4}")) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                        date = LocalDate.parse(raw, formatter);
                    }
                }
            }

            Pattern pattern = Pattern.compile("^(.*?)\\s+([\\d.,]+)\\s+(\\S+/\\S+|\\S+)\\s+([\\d.,]+)[-–]([\\d.,]+)$");

            for (String line : lines) {
                line = line.trim();
                if (line.length() < 10 || line.toLowerCase().contains("nume") || line.toLowerCase().contains("test")) continue;

                System.out.println("[LINE] " + line);
                Matcher matcher = pattern.matcher(line);
                if (matcher.matches()) {
                    try {
                        String testName = matcher.group(1).trim();
                        double value = Double.parseDouble(matcher.group(2).replace(",", "."));
                        String unit = matcher.group(3).trim();
                        double min = Double.parseDouble(matcher.group(4).replace(",", "."));
                        double max = Double.parseDouble(matcher.group(5).replace(",", "."));

                        Analysis a = new Analysis();
                        a.setUser(user);
                        a.setTestName(testName);
                        a.setValue(value);
                        a.setUnit(unit);
                        a.setNormalMin(min);
                        a.setNormalMax(max);
                        a.setTestDate(date);

                        analysisRepository.save(a);

                        results.add(new AnalysisDTO(
                                a.getId(), user.getId(), testName, value, unit, min, max, date
                        ));
                    } catch (Exception ex) {
                        System.out.println("[IGNORED LINE] " + line + " — Eroare: " + ex.getMessage());
                    }
                } else {
                    System.out.println("[NO MATCH] " + line);
                }
            }
        }

        if (results.isEmpty()) {
            throw new RuntimeException("Nicio analiză validă nu a fost găsită în PDF.");
        }



    }

}

