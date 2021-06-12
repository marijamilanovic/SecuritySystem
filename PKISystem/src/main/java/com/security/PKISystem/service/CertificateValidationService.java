package com.security.PKISystem.service;

import com.security.PKISystem.domain.Certificate;
import com.security.PKISystem.domain.CertificateType;
import com.security.PKISystem.domain.dto.RequestCertificateDto;


public interface CertificateValidationService {

    boolean isCertificateValid(Long serialNumber);

    boolean isNewCertificateValid(RequestCertificateDto requestCertificateDto);

    String getCertificateKeyStore(CertificateType certType);

    boolean isNotRevoked(Certificate certificate);

}
