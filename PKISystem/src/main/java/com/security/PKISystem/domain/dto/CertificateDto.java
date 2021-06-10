package com.security.PKISystem.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.security.PKISystem.domain.CertificateType;
import com.security.PKISystem.domain.State;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
public class CertificateDto {
    private Long id;
    private Long serialNumber;
    private String publicKey;
    private String owner;
    private String issuerName;
    private Long issuerSerial;
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+02:00")
    private Date validFrom;
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+02:00")
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
}
