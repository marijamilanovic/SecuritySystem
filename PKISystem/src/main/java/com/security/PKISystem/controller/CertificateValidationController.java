package com.security.PKISystem.controller;

import com.security.PKISystem.service.CertificateValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/validation")
@CrossOrigin(origins = "http://localhost:4200")
public class CertificateValidationController {

    @Autowired
    private CertificateValidationService certificateValidationService;

    @GetMapping("/{serialNumber}")
    public boolean isCertificateValid(@PathVariable Long serialNumber){
        return certificateValidationService.isCertificateValid(serialNumber);
    }
}
