package com.security.PKISystem.repository;

import com.security.PKISystem.domain.Certificate;
import com.security.PKISystem.domain.CertificateType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {

    List<Certificate> findAll();

    Certificate findCertificateByCertificateType(CertificateType certificateType);

    Certificate findCertificateById(Long id);

    List<Certificate> findCertificateByIssuerName(String issuedByName);

    List<Certificate> findCertificateByIssuerId(Long id);
}


