package com.security.PKISystem.service;

import com.security.PKISystem.domain.Certificate;
import com.security.PKISystem.domain.CertificateType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CertificateService {
    Certificate save(Certificate certificate);

    List<Certificate> findAll();

    Certificate findCertificateByCertificateType(CertificateType certificateType);

    Certificate findCertificateById(Long id);
}
