package com.security.PKISystem.dto;

import com.security.PKISystem.domain.CertificateType;
import com.security.PKISystem.domain.State;

import java.util.Date;

public class CertificateDto {
    private Long id;
    private Long serialNumber;
    private String publicKey;
    private String issuerName;
    private Long issuerId;
    private Date validFrom;
    private Date validTo;
    private CertificateType certificateType;
    private State state;

    public CertificateDto(){}

    public CertificateDto(Long id, Long serialNumber, String publicKey, String issuerName, Long issuerId, Date validFrom, Date validTo, CertificateType certificateType, State state) {
        this.id = id;
        this.serialNumber = serialNumber;
        this.publicKey = publicKey;
        this.issuerName = issuerName;
        this.issuerId = issuerId;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.certificateType = certificateType;
        this.state = state;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getIssuerName() {
        return issuerName;
    }

    public void setIssuerName(String issuerName) {
        this.issuerName = issuerName;
    }

    public Long getIssuerId() {
        return issuerId;
    }

    public void setIssuerId(Long issuerId) {
        this.issuerId = issuerId;
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

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
