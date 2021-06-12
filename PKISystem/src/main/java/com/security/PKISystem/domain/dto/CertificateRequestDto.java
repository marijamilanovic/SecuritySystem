package com.security.PKISystem.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.security.PKISystem.domain.RequestStatus;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
public class CertificateRequestDto {
    private String commonName;
    private String surname;
    private String givenName;
    private String organisation;
    private String organisationUnit;
    private String country;
    private String email;
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+02:00")
    private Date validFrom;
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+02:00")
    private Date validTo;
    private String keyUsage;
    private Long issuerSerial;
    private RequestStatus status;
    private Boolean isCA;
    private String username;
}
