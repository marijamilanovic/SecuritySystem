package com.security.PKISystem.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class CertificateRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String commonName;
    @Column
    private String surname;
    @Column
    private String givenName;
    @Column
    private String organisation;
    @Column
    private String organisationUnit;
    @Column
    private String country;
    @Column
    private String email;
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+02:00")
    private Date validFrom;
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+02:00")
    private Date validTo;
    @Column
    private String keyUsage;
    @Column
    private Boolean isCA;
    @Column
    private Long issuerSerial;
    @Column
    private RequestStatus status;
    @ManyToOne
    private User user;
    @OneToOne
    private Certificate cert;

}
