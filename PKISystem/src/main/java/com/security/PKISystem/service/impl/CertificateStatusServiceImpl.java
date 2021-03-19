package com.security.PKISystem.service.impl;

import com.security.PKISystem.domain.CertificateStatus;
import com.security.PKISystem.repository.CertificateStatusRepository;
import com.security.PKISystem.service.CertificateStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CertificateStatusServiceImpl implements CertificateStatusService {
    @Autowired
    private CertificateStatusRepository certificateStatusRepository;

    @Override
    public CertificateStatus saveCertificateStatus(CertificateStatus certificateStatus) {
        return certificateStatusRepository.save(certificateStatus);
    }
}
