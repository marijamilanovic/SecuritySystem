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
    public List<CertificateDto> getAllCertificates(){
        return certificateService.findAll();
    }

    @GetMapping("/{serialNumber}/{issuerId}")
    public Certificate getCertificateBySerialNumberAndIssuerSerial(@PathVariable Long serialNumber, @PathVariable Long issuerId){
        return certificateService.getCertificateBySerialNumberAndIssuerId(serialNumber, issuerId);
    }

    @GetMapping("/issuers")
    public List<CertificateDto> getAllIssuers(){
        return certificateService.getAllIssuers();
    }

    @GetMapping("/chain/{serialNumber}")
    public List<CertificateDto> getCertificateChain(@PathVariable Long serialNumber){
        return certificateService.getCertificateChain(serialNumber);
    }

    @PostMapping("/root")
    public ResponseEntity generateRootCertificate(@RequestBody RequestCertificateDto certificate){
        return certificateService.addRootCertificate(certificate);
    }

    @PostMapping
    public ResponseEntity<X509Certificate> generateCertificate(@RequestBody RequestCertificateDto requestCertificateDto)  {
        return new ResponseEntity(certificateService.addCertificate(requestCertificateDto), HttpStatus.OK);
    }

    @DeleteMapping("/revoke/{serialNumber}")
    public void revokeCertificate(@PathVariable Long serialNumber){
        certificateService.revokeCertificateChain(serialNumber);
    }


    @GetMapping("/states")
    public List<String> getStates(){
        return certificateService.getStates();
    }

    @GetMapping("/types")
    public List<String> getCertificateTypes() {
        return certificateService.getCertificateTypes();
    }
}
