package com.security.PKISystem.repository;

import com.security.PKISystem.domain.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {

    Certificate save(Certificate certificate);

    Certificate findCertificateById(Long id);

    Certificate findCertificateBySerialNumberAndIssuerSerial(Long serialNumber, Long issuerSerial);

    Certificate findCertificateBySerialNumber(Long serialNumber);

    List<Certificate> findAll();

    List<Certificate> findCertificateByIssuerSerial(Long issuerSerial);
}


