package com.security.PKISystem.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table
@Entity
public class Certificate implements Serializable {
    private static final long serialVersionUID = -6771834675925509282L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long serialNumber;
    @Column(length=1000)
    private String publicKey;
    @Column
    private String owner;
    @Column
    private String issuerName;
    @Column
    private Long issuerSerial;
    @Column
    private String keyUsage;
    @Column
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+01:00")
    private Date validFrom;
    @Column
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+01:00")
    private Date validTo;
    @Column
    private CertificateType certificateType;
    @Column
    private State state;

    public Certificate(){}

    public Certificate(Long id, Long serialNumber, String publicKey, String issuerName, String owner,
                       Long issuerSerial, Date validFrom, Date validTo, CertificateType certificateType, State state) {
        this.id = id;
        this.serialNumber = serialNumber;
        this.publicKey = publicKey;
        this.issuerName = issuerName;
        this.issuerSerial = issuerSerial;
        this.owner = owner;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.certificateType = certificateType;
        this.state = state;
    }

    public Certificate(Long serialNumber, String publicKey, String issuerName, String owner,
                       Long issuerSerial, Date validFrom, Date validTo, CertificateType certificateType, State state, String keyUsage) {
        this.serialNumber = serialNumber;
        this.publicKey = publicKey;
        this.issuerName = issuerName;
        this.issuerSerial = issuerSerial;
        this.owner = owner;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.certificateType = certificateType;
        this.state = state;
        this.keyUsage = keyUsage;
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

    public Long getIssuerSerial() {
        return issuerSerial;
    }

    public void setIssuerSerial(Long issuerSerial) {
        this.issuerSerial = issuerSerial;
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

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getKeyUsage() {
        return keyUsage;
    }

    public void setKeyUsage(String keyUsage) {
        this.keyUsage = keyUsage;
    }
}
