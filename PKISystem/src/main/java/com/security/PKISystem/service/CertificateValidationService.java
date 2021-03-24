package com.security.PKISystem.service;

import com.security.PKISystem.domain.dto.RequestCertificateDto;
import org.springframework.stereotype.Service;

@Service
public interface CertificateValidationService {

    boolean isCertificateValid(Long serialNumber, Long issuerId);

    boolean isNewCertificateValid(RequestCertificateDto requestCertificateDto);

}
