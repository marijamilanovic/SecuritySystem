package com.security.PKISystem.controller;

import com.security.PKISystem.domain.Certificate;
import com.security.PKISystem.dto.AddCertificateDto;
import com.security.PKISystem.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.cert.X509Certificate;

@RestController
public class CertificateController {
    @Autowired
    private CertificateService certificateService;

    @PostMapping
    public ResponseEntity generateCertificate(@RequestBody AddCertificateDto addCertificateDto)  {
        X509Certificate cert = this.certificateService.addCertificate(addCertificateDto);
        return ResponseEntity.ok().build();
    }
}
