package com.security.PKISystem.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Table
@Entity
public class Certificate implements Serializable {
    private static final long serialVersionUID = -6771834675925509282L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:yy", timezone = "GMT+02:00")
    private Date validFrom;
    @Column
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:yy", timezone = "GMT+02:00")
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
}
