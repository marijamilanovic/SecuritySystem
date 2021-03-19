package com.security.PKISystem.repository;

import com.security.PKISystem.domain.CertificateStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateStatusRepository extends JpaRepository<CertificateStatus, Long> {
    CertificateStatus findCertificateStatusByCertificateId(Long id);
}
