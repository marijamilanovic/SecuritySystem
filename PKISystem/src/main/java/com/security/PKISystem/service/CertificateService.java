package com.security.PKISystem.service;

import com.security.PKISystem.domain.Certificate;
import org.springframework.stereotype.Service;

@Service
public interface CertificateService {
    Certificate save(Certificate certificate);
}
