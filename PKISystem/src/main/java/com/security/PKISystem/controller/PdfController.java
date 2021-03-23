package com.security.PKISystem.controller;

import com.itextpdf.text.DocumentException;
import com.security.PKISystem.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;

@RestController
@RequestMapping("/pdf")
@CrossOrigin(origins = "http://localhost:4200")
public class PdfController {
    @Autowired
    private PdfService pdfService;

    @GetMapping("/{id}")
    public void generatePdf(@PathVariable("id") Long certificateId) throws FileNotFoundException, DocumentException {
        pdfService.generatePdf(certificateId);
    }

}
