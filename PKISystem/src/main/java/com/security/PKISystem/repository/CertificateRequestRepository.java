package com.security.PKISystem.repository;

import com.security.PKISystem.domain.CertificateRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CertificateRequestRepository extends JpaRepository<CertificateRequest, Long> {

    CertificateRequest findCertificateRequestById(Long id);

    List<CertificateRequest> findCertificateRequestsByUserUsername(String username);
}
