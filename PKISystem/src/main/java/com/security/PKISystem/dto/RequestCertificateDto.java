package com.security.PKISystem.dto;

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

    public RequestCertificateDto(){}

    public RequestCertificateDto(CertificateDto certificateDto, String issuedToCommonName, String surname, String givenName,
                                 String organisation, String organisationalUnit, String country, String email, String keystoreName, String keystorePassword) {
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
    }

    public CertificateDto getCertificateDto() {
        return certificateDto;
    }

    public void setCertificateDto(CertificateDto certificateDto) {
        this.certificateDto = certificateDto;
    }

    public String getKeystoreName() {
        return keystoreName;
    }

    public void setKeystoreName(String keystoreName) {
        this.keystoreName = keystoreName;
    }

    public String getKeystorePassword() {
        return keystorePassword;
    }

    public void setKeystorePassword(String keystorePassword) {
        this.keystorePassword = keystorePassword;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public String getOrganisationalUnit() {
        return organisationalUnit;
    }

    public void setOrganisationalUnit(String organisationalUnit) {
        this.organisationalUnit = organisationalUnit;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIssuedToCommonName() {
        return issuedToCommonName;
    }

    public void setIssuedToCommonName(String issuedToCommonName) {
        this.issuedToCommonName = issuedToCommonName;
    }
}
