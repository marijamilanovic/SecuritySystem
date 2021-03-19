package com.security.PKISystem.dto;

import com.security.PKISystem.domain.Certificate;
import com.security.PKISystem.domain.CertificateType;

import java.util.Date;

public class AddCertificateDto {
    private Long serialNumber;
    private String publicKey;
    private String issuedByName;
    private Long issuedById;
    private Date validFrom;
    private Date validTo;
    private CertificateType certificateType;
    private String keystoreName;
    private String keystorePassword;

    public AddCertificateDto(){}

    public AddCertificateDto(Certificate certificate){
        this.serialNumber = certificate.getSerialNumber();
        this.publicKey = certificate.getPublicKey();
        this.issuedByName = certificate.getIssuerName();
        this.issuedById = certificate.getIssuerId();
        this.validFrom = certificate.getValidFrom();
        this.validTo = certificate.getValidTo();
        this.certificateType = certificate.getCertificateType();
    }

    public AddCertificateDto(Long serialNumber, String publicKey, String issuedByName, Long issuedById, Date validFrom, Date validTo, CertificateType certificateType, String keystoreName, String keystorePassword) {
        this.serialNumber = serialNumber;
        this.publicKey = publicKey;
        this.issuedByName = issuedByName;
        this.issuedById = issuedById;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.certificateType = certificateType;
        this.keystoreName = keystoreName;
        this.keystorePassword = keystorePassword;
    }

    public Long getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Long serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getIssuedByName() {
        return issuedByName;
    }

    public void setIssuedByName(String issuedByName) {
        this.issuedByName = issuedByName;
    }

    public Long getIssuedById() {
        return issuedById;
    }

    public void setIssuedById(Long issuedById) {
        this.issuedById = issuedById;
    }

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    public Date getValidTo() {
        return validTo;
    }

    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }

    public CertificateType getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(CertificateType certificateType) {
        this.certificateType = certificateType;
    }

    public String getKeystoreName() {
        return keystoreName;
    }

    public void setKeystoreName(String keystoreName) {
        this.keystoreName = keystoreName;
    }

    public String getKeystorePassword() {
        return keystorePassword;
    }

    public void setKeystorePassword(String keystorePassword) {
        this.keystorePassword = keystorePassword;
    }
}
