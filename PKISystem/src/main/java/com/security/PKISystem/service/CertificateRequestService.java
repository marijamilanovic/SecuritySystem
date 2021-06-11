package com.security.PKISystem.service;

import com.security.PKISystem.domain.CertificateRequest;

import java.util.List;


public interface CertificateRequestService {

    CertificateRequest findRequestById(Long id);

    List<CertificateRequest> findRequestsByUserUsername(String username);

    List<CertificateRequest> findAll();

    void createRequest(CertificateRequest request);
}
