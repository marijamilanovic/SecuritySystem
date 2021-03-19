package com.security.PKISystem.service;

import com.security.PKISystem.domain.Certificate;
import com.security.PKISystem.dto.AddCertificateDto;
import org.springframework.stereotype.Service;

import java.security.cert.X509Certificate;

@Service
public interface CertificateService {
    Certificate save(Certificate certificate);
    X509Certificate addCertificate(AddCertificateDto addCertificateDto);
}
