package com.security.PKISystem.service.impl;

import com.security.PKISystem.domain.CertificateRequest;
import com.security.PKISystem.repository.CertificateRequestRepository;
import com.security.PKISystem.service.CertificateRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CertificateRequestServiceImpl implements CertificateRequestService {

    @Autowired
    private CertificateRequestRepository requestRepository;

    @Override
    public CertificateRequest findRequestById(Long id) {
        return requestRepository.findCertificateRequestById(id);
    }

    @Override
    public List<CertificateRequest> findRequestsByUserUsername(String username) {
        return requestRepository.findCertificateRequestsByUserUsername(username);
    }

    @Override
    public List<CertificateRequest> findAll() {
        return requestRepository.findAll();
    }

    @Override
    public void createRequest(CertificateRequest request) {
        requestRepository.save(request);
    }
}
