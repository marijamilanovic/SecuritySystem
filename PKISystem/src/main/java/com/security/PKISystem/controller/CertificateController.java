package com.security.PKISystem.controller;

import com.security.PKISystem.domain.dto.CertificateDto;
import com.security.PKISystem.domain.dto.RequestCertificateDto;
import com.security.PKISystem.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public List<CertificateDto> getAllCertificates(){ return certificateService.findAll(); }

    @GetMapping("/issuers")
    public List<CertificateDto> getValidIssuers(){
        return certificateService.getValidIssuers();
    }

    @GetMapping("/chain/{serialNumber}")
    public List<CertificateDto> getCertificateChain(@PathVariable Long serialNumber){
        return certificateService.getCertificateChain(serialNumber);
    }

    @PreAuthorize("hasAuthority('GENERATE_ROOT')")
    @PostMapping("/root")
    public ResponseEntity generateRootCertificate(@RequestBody RequestCertificateDto certificate){
        return certificateService.addRootCertificate(certificate);
    }

    @PreAuthorize("hasAuthority('GENERATE_CERT')")
    @PostMapping
    public ResponseEntity<X509Certificate> generateCertificate(@RequestBody RequestCertificateDto requestCertificateDto)  {
        return certificateService.addCertificate(requestCertificateDto);
    }

    @PreAuthorize("hasAuthority('REVOKE_CERT')")
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
