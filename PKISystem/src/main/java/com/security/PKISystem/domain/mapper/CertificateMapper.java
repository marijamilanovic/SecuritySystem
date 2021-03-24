package com.security.PKISystem.domain.mapper;

import com.security.PKISystem.domain.Certificate;
import com.security.PKISystem.domain.IssuerData;
import com.security.PKISystem.domain.SubjectData;
import com.security.PKISystem.domain.dto.CertificateDto;
import com.security.PKISystem.domain.dto.RequestCertificateDto;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;

import java.security.KeyPair;

public class CertificateMapper {

    public static CertificateDto mapCertificateToCertificateDto(Certificate certificate) {
        CertificateDto certificateDto = new CertificateDto();
        certificateDto.setId(certificate.getId());
        certificateDto.setCertificateType(certificate.getCertificateType());
        certificateDto.setIssuerSerial(certificate.getIssuerSerial());
        certificateDto.setIssuerName(certificate.getIssuerName());
        certificateDto.setState(certificate.getState());
        certificateDto.setPublicKey(certificate.getPublicKey());
        certificateDto.setSerialNumber(new Long(certificate.getSerialNumber()));
        certificateDto.setValidFrom(certificate.getValidTo());
        certificateDto.setValidTo(certificate.getValidTo());
        certificateDto.setOwner(certificate.getOwner());
        return certificateDto;
    }

    public static X500Name mapCertificateDtoToX500name(RequestCertificateDto certificateDto, Long serialNumber){
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, certificateDto.getIssuedToCommonName());
        builder.addRDN(BCStyle.SURNAME, certificateDto.getSurname());
        builder.addRDN(BCStyle.GIVENNAME, certificateDto.getGivenName());
        builder.addRDN(BCStyle.O, certificateDto.getOrganisation());
        builder.addRDN(BCStyle.OU, certificateDto.getOrganisationalUnit());
        builder.addRDN(BCStyle.C, certificateDto.getCountry());
        builder.addRDN(BCStyle.E, certificateDto.getEmail());
        builder.addRDN(BCStyle.SERIALNUMBER, serialNumber.toString());
        return builder.build();
    }
}
