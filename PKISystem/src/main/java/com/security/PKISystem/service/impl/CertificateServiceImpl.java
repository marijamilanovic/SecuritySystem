package com.security.PKISystem.service.impl;

import com.security.PKISystem.domain.Certificate;
import com.security.PKISystem.domain.CertificateType;
import com.security.PKISystem.repository.CertificateRepository;
import com.security.PKISystem.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CertificateServiceImpl implements CertificateService {
    @Autowired
    private CertificateRepository certificateRepository;

    @Override
    public Certificate save(Certificate certificate){
        return this.certificateRepository.save(certificate);
    }

    @Override
    public List<Certificate> findAll() { return this.certificateRepository.findAll(); }

    @Override
    public Certificate findCertificateByCertificateType(CertificateType certificateType) { return  this.certificateRepository.findCertificateByCertificateType(certificateType); }

    @Override
    public Certificate findCertificateById(Long id) { return this.certificateRepository.findCertificateById(id); }
}
