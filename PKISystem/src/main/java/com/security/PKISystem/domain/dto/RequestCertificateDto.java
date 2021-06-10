package com.security.PKISystem.domain.dto;

import lombok.Data;

@Data
public class RequestCertificateDto {
    private CertificateDto certificateDto;
    private String issuedToCommonName;
    private String surname;
    private String givenName;
    private String organisation;
    private String organisationalUnit;
    private String country;
    private String email;
    private String keystoreName;
    private String keystorePassword;
    private String keystoreIssuerPassword;      // for end-entity and intermediate certificate

    public RequestCertificateDto(){}

    public RequestCertificateDto(CertificateDto certificateDto, String issuedToCommonName, String surname, String givenName, String organisation, String organisationalUnit, String country, String email, String keystoreName, String keystorePassword, String keystoreIssuerPassword) {
        this.certificateDto = certificateDto;
        this.issuedToCommonName = issuedToCommonName;
        this.surname = surname;
        this.givenName = givenName;
        this.organisation = organisation;
        this.organisationalUnit = organisationalUnit;
        this.country = country;
        this.email = email;
        this.keystoreName = keystoreName;
        this.keystorePassword = keystorePassword;
        this.keystoreIssuerPassword = keystoreIssuerPassword;
    }

    public String getKeystoreIssuerPassword() {
        return keystoreIssuerPassword;
    }

    public void setKeystoreIssuerPassword(String keystoreIssuerPassword) {
        this.keystoreIssuerPassword = keystoreIssuerPassword;
    }
}
