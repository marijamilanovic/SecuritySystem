package com.security.PKISystem.service;

import com.security.PKISystem.domain.Certificate;
import com.security.PKISystem.domain.CertificateType;
import com.security.PKISystem.domain.State;
import com.security.PKISystem.dto.CertificateDto;
import org.springframework.stereotype.Service;

import java.util.List;
import com.security.PKISystem.dto.RequestCertificateDto;

import java.security.cert.X509Certificate;

@Service
public interface CertificateService {
    Certificate save(Certificate certificate);

    List<CertificateDto> findAll();

    Certificate findCertificateByCertificateType(CertificateType certificateType);

    Certificate findCertificateById(Long id);

    X509Certificate addCertificate(RequestCertificateDto requestCertificateDto);

    X509Certificate addRootCertificate(RequestCertificateDto requestCertificateDto);

    void revokeCertificateChain(Long certificateId);

    boolean isCertificateValid(Long serialNumber, Long issuerId);

    Certificate getCertificateBySerialNumberAndIssuerId(Long serialNumber, Long issuerId);

    List<String> getStates();

    List<String> getCertificateTypes();

    List<CertificateDto> getAllIssuers();

    Certificate findCertificateBySerialNumberAndOwner(Long serialNumber, String owner);
}
