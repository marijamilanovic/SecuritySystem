package com.security.PKISystem.domain.dto;

import com.security.PKISystem.domain.CertificateType;
import com.security.PKISystem.domain.State;

import java.util.Date;

public class CertificateDto {
    private Long id;
    private Long serialNumber;
    private String publicKey;
    private String owner;
    private String issuerName;
    private Long issuerSerial;
    private Date validFrom;
    private Date validTo;
    private CertificateType certificateType;
    private State state;
    private String keyUsage;

    public CertificateDto(){}

    public CertificateDto(Long id, Long serialNumber, String publicKey, String owner, String issuerName, Long issuerSerial, Date validFrom, Date validTo, CertificateType certificateType, State state, String keyUsage) {
        this.id = id;
        this.serialNumber = serialNumber;
        this.publicKey = publicKey;
        this.owner = owner;
        this.issuerName = issuerName;
        this.issuerSerial = issuerSerial;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.certificateType = certificateType;
        this.state = state;
        this.keyUsage = keyUsage;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Long getIssuerSerial() {
        return issuerSerial;
    }

    public void setIssuerSerial(Long issuerSerial) {
        this.issuerSerial = issuerSerial;
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

    public String getKeyUsage() {
        return keyUsage;
    }

    public void setKeyUsage(String keyUsage) {
        this.keyUsage = keyUsage;
    }
}
