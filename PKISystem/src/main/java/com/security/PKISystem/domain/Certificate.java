package com.security.PKISystem.domain;

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
    @Column
    private Long serialNumber;
    @Column
    private String publicKey;
    @Column
    private String issuedByName;
    @Column
    private Long issuedById;
    @Column
    private Date validFrom;
    @Column
    private Date validTo;
    @Column
    private CertificateType certificateType;
    @Column
    private State state;

    public Certificate(){}

    public Certificate(Long id, Long serialNumber, String publicKey, String issuedByName, Long issuedById, Date validFrom, Date validTo, CertificateType certificateType, State state) {
        this.id = id;
        this.serialNumber = serialNumber;
        this.publicKey = publicKey;
        this.issuedByName = issuedByName;
        this.issuedById = issuedById;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.certificateType = certificateType;
        this.state = state;
    }

    public Certificate(String publicKey, String issuedByName, Long issuedById, Date validFrom, Date validTo) {
        this.publicKey = publicKey;
        this.issuedByName = issuedByName;
        this.issuedById = issuedById;
        this.validFrom = validFrom;
        this.validTo = validTo;
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
}
