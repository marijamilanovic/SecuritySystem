package com.security.PKISystem.service.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.security.PKISystem.domain.Certificate;
import com.security.PKISystem.service.CertificateService;
import com.security.PKISystem.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

@Service
public class PdfServiceImpl implements PdfService {
    @Autowired
    private CertificateService certificateService;
    @Override
    public void generatePdf(Long certificateId) throws FileNotFoundException, DocumentException {
        Document document = new Document();
        Certificate certificate = certificateService.findCertificateById(certificateId);
        PdfWriter.getInstance(document, new FileOutputStream("Certificate.pdf"));
        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
        Font font1 = FontFactory.getFont(FontFactory.COURIER_BOLD, 18, BaseColor.BLACK);
        Chunk chunk = new Chunk("Serial number:",font1);
        Paragraph paragraph = new Paragraph(chunk);
        Chunk chunk1 = new Chunk(certificate.getSerialNumber().toString(),font);
        Paragraph paragraph1 = new Paragraph(chunk1);
        Chunk chunk2 = new Chunk("Issuer:",font1);
        Paragraph paragraph2 = new Paragraph(chunk2);
        Chunk chunk3 = new Chunk(certificate.getIssuerName(), font);
        Paragraph paragraph3 = new Paragraph(chunk3);
        Chunk chunk4 = new Chunk("Owner:",font1);
        Paragraph paragraph4 = new Paragraph(chunk4);
        Chunk chunk5 = new Chunk(certificate.getOwner(),font);
        Paragraph paragraph5 = new Paragraph(chunk5);
        Chunk chunk6 = new Chunk("Valid from:", font1);
        Paragraph paragraph6 = new Paragraph(chunk6);
        Chunk chunk7 = new Chunk(certificate.getValidFrom().toString(),font);
        Paragraph paragraph7 = new Paragraph(chunk7);
        Chunk chunk8 = new Chunk("Valid to:", font1);
        Paragraph paragraph8 = new Paragraph(chunk8);
        Chunk chunk9 = new Chunk(certificate.getValidTo().toString(),font);
        Paragraph paragraph9 = new Paragraph(chunk9);
        Chunk chunk10 = new Chunk("Key usage:", font1);
        Paragraph paragraph10 = new Paragraph(chunk10);
        Chunk chunk11 = new Chunk(certificate.getKeyUsage(),font);
        Paragraph paragraph11 = new Paragraph(chunk11);


        document.add(paragraph);
        document.add(paragraph1);
        document.add(paragraph2);
        document.add(paragraph3);
        document.add(paragraph4);
        document.add(paragraph5);
        document.add(paragraph6);
        document.add(paragraph7);
        document.add(paragraph8);
        document.add(paragraph9);
        document.add(paragraph10);
        document.add(paragraph11);

        document.close();
    }
}
