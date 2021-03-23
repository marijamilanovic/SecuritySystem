package com.security.PKISystem.domain.mapper;

import com.security.PKISystem.domain.Certificate;
import com.security.PKISystem.dto.CertificateDto;

import java.math.BigInteger;

public class CertificateMapper {

    public static CertificateDto mapCertificateToCertificateDto(Certificate certificate) {
        CertificateDto certificateDto = new CertificateDto();
        certificateDto.setId(certificate.getId());
        certificateDto.setCertificateType(certificate.getCertificateType());
        certificateDto.setIssuerSerial(certificate.getIssuerSerial());
        certificateDto.setIssuerName(certificate.getIssuerName());
        certificateDto.setState(certificate.getState());
        certificateDto.setPublicKey(certificate.getPublicKey());
        certificateDto.setSerialNumber(new Long(certificate.getSerialNumber()));
        certificateDto.setValidFrom(certificate.getValidTo());
        certificateDto.setValidTo(certificate.getValidTo());
        return certificateDto;
    }
}
