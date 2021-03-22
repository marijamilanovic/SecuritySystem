package com.security.PKISystem.service;

import com.security.PKISystem.domain.Certificate;
import com.security.PKISystem.domain.CertificateType;
import com.security.PKISystem.domain.State;
import org.springframework.stereotype.Service;

import java.util.List;
import com.security.PKISystem.dto.AddCertificateDto;
import org.springframework.stereotype.Service;

import java.security.cert.X509Certificate;

@Service
public interface CertificateService {
    Certificate save(Certificate certificate);

    List<AddCertificateDto> findAll();

    Certificate findCertificateByCertificateType(CertificateType certificateType);

    Certificate findCertificateById(Long id);

    X509Certificate addCertificate(AddCertificateDto addCertificateDto);

    void revokeCertificateChain(Long certificateId);

    boolean isCertificateValid(Long serialNumber, Long issuerId);

    Certificate getCertificateBySerialNumberAndIssuerId(Long serialNumber, Long issuerId);

    List<String> getStates();

    List<String> getCertificateTypes();
}
