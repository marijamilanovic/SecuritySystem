package com.security.PKISystem.service;

import com.itextpdf.text.DocumentException;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;

@Service
public interface PdfService {
    void generatePdf(Long certificateId) throws FileNotFoundException, DocumentException;
}
