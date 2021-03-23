package com.security.PKISystem.service;

import com.security.PKISystem.domain.Certificate;
import com.security.PKISystem.domain.CertificateType;
import com.security.PKISystem.dto.CertificateDto;
import com.security.PKISystem.dto.RequestCertificateDto;
import org.springframework.stereotype.Service;

import java.security.cert.X509Certificate;
import java.util.List;

@Service
public interface CertificateService {
    Certificate save(Certificate certificate);

    Certificate findCertificateByCertificateType(CertificateType certificateType);

    Certificate findCertificateById(Long id);

    Certificate getCertificateBySerialNumberAndIssuerId(Long serialNumber, Long issuerId);

    Certificate findCertificateBySerialNumberAndOwner(Long serialNumber, String owner);

    List<CertificateDto> findAll();

    List<CertificateDto> getAllIssuers();

    X509Certificate addCertificate(RequestCertificateDto requestCertificateDto);

    X509Certificate addRootCertificate(RequestCertificateDto requestCertificateDto);

    void revokeCertificateChain(Long serialNumber, Long issuerSerial);

    boolean isCertificateValid(Long serialNumber, Long issuerId);

    List<String> getStates();

    List<String> getCertificateTypes();
}
