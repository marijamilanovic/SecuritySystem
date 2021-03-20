package com.security.PKISystem.controller;

import com.security.PKISystem.domain.Certificate;
import com.security.PKISystem.dto.AddCertificateDto;
import com.security.PKISystem.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.cert.X509Certificate;
import java.util.List;

@RestController
@RequestMapping("/certificate")
public class CertificateController {
    @Autowired
    private CertificateService certificateService;

    @PostMapping
    public ResponseEntity generateCertificate(@RequestBody AddCertificateDto addCertificateDto)  {
        X509Certificate cert = this.certificateService.addCertificate(addCertificateDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<AddCertificateDto>> getCertificates(){
        return new ResponseEntity(certificateService.findAll(), HttpStatus.OK);
    }

    @GetMapping("{serialNumber}/{issuerId}")
    public Certificate findCertificateBySerialNumberAndIssuerId(@PathVariable("serialNumber") Long serialNumber, @PathVariable("issuerId") Long issuerId){
        return certificateService.findCertificateBySerialNumberAndIssuerId(serialNumber, issuerId);
    }
}
