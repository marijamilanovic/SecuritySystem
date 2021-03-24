package com.security.PKISystem.controller;

import com.security.PKISystem.domain.Certificate;
import com.security.PKISystem.domain.dto.CertificateDto;
import com.security.PKISystem.domain.dto.RequestCertificateDto;
import com.security.PKISystem.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.cert.X509Certificate;
import java.util.List;

@RestController
@RequestMapping("/certificate")
@CrossOrigin(origins = "http://localhost:4200")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;


    @GetMapping
    public ResponseEntity<List<CertificateDto>> getCertificates(){
        return new ResponseEntity(certificateService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity generateCertificate(@RequestBody RequestCertificateDto requestCertificateDto)  {
        this.certificateService.addCertificate(requestCertificateDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{serialNumber}/{issuerId}")
    public Certificate getCertificateBySerialNumberAndIssuerSerial(@PathVariable Long serialNumber, @PathVariable Long issuerId){
        return certificateService.getCertificateBySerialNumberAndIssuerId(serialNumber, issuerId);
    }

    @GetMapping("/issuers")
    public List<CertificateDto> getAllIssuers(){
        return certificateService.getAllIssuers();
    }

    @GetMapping("/states")
    public List<String> getStates(){
        return certificateService.getStates();
    }

    @GetMapping("/types")
    public List<String> getCertificateTypes() {
        return certificateService.getCertificateTypes();
    }


    @PostMapping("/root")
    public ResponseEntity<X509Certificate> generateRootCertificate(@RequestBody RequestCertificateDto certificate){
        return new ResponseEntity(certificateService.addRootCertificate(certificate), HttpStatus.OK);
    }

    @DeleteMapping("/revoke/{serialNumber}/{issuerSerial}")
    public void revokeCertificate(@PathVariable Long serialNumber, @PathVariable Long issuerSerial){
        certificateService.revokeCertificateChain(serialNumber, issuerSerial);
    }
}
