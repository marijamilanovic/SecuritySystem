package com.security.PKISystem.service;

import com.security.PKISystem.domain.CertificateStatus;
import org.springframework.stereotype.Service;

@Service
public interface CertificateStatusService {
    CertificateStatus saveCertificateStatus(CertificateStatus certificateStatus);
}
