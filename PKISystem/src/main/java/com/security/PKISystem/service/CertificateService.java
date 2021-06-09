package com.security.PKISystem.service;

import com.security.PKISystem.domain.Certificate;
import com.security.PKISystem.domain.dto.CertificateDto;
import com.security.PKISystem.domain.dto.RequestCertificateDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.cert.X509Certificate;
import java.util.List;

@Service
public interface CertificateService {

    Certificate findCertificateById(Long id);

    Certificate getCertificateBySerialNumber(Long serialNumber);

    Certificate getCertificateBySerialNumberAndIssuerId(Long serialNumber, Long issuerId);

    List<CertificateDto> findAll();

    List<CertificateDto> getAllIssuers();

    List<CertificateDto> getCertificateChain(Long serialNumber);

    ResponseEntity addCertificate(RequestCertificateDto requestCertificateDto);

    ResponseEntity addRootCertificate(RequestCertificateDto requestCertificateDto);

    void revokeCertificateChain(Long serialNumber);

    List<String> getStates();

    List<String> getCertificateTypes();
}
