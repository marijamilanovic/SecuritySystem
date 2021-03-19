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

    public AddCertificateDto(){}

    public AddCertificateDto(Certificate certificate){
        this.serialNumber = certificate.getSerialNumber();
        this.publicKey = certificate.getPublicKey();
        this.issuedByName = certificate.getIssuedByName();
        this.issuedById = certificate.getIssuedById();
        this.validFrom = certificate.getValidFrom();
        this.validTo = certificate.getValidTo();
        this.certificateType = certificate.getCertificateType();
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
}
