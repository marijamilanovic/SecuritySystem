package com.security.PKISystem.service.impl;

import com.security.PKISystem.domain.Certificate;
import com.security.PKISystem.domain.CertificateType;
import com.security.PKISystem.domain.State;
import com.security.PKISystem.domain.dto.RequestCertificateDto;
import com.security.PKISystem.exception.NotFoundException;
import com.security.PKISystem.keystores.KeyStoreReader;
import com.security.PKISystem.service.CertificateService;
import com.security.PKISystem.service.CertificateValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Service
public class CertificateValidationServiceServiceImpl implements CertificateValidationService {
    @Value("${rootKSPath}")
    private String rootKSPath;
    @Value("${intermediateKSPath}")
    private String intermediateKSPath;
    @Value("${endEntityKSPath}")
    private String endEntityKSPath;

    @Autowired
    private CertificateService certificateService;

    @Override
    public boolean isCertificateValid(Long serialNumber, Long issuerId) {
        Certificate certificate = certificateService.getCertificateBySerialNumber(serialNumber);
        if (certificate != null){
            if(checkDate(certificate.getValidFrom(), certificate.getValidTo()) &&
                    isNotRevoked(certificate))
                return true;
            // todo: certificate chain, provera javnog kljuca ?
            return false;
        }
        throw new NotFoundException("Certificate not found.");
    }


    @Override
    public boolean isNewCertificateValid(RequestCertificateDto requestCertificateDto) {
        Certificate issuerCertificate = certificateService.getCertificateBySerialNumber(requestCertificateDto.getCertificateDto().getIssuerSerial());
        if(checkNewCertificateDate(requestCertificateDto) && requestCertificateDto.getCertificateDto().getCertificateType() == CertificateType.ROOT){
            return true;
        }else if(checkNewCertificateDate(requestCertificateDto) && isNotRevoked(issuerCertificate) && checkNewCertificateChain(requestCertificateDto)){
            return true;
        }
        return false;
    }

    public boolean checkDate(Date validFrom, Date validTo){
        long current = new Date().getTime();
        return validFrom.getTime() >= current && validTo.getTime() >= current && validFrom.getTime() < validTo.getTime();
    }
    

    public boolean checkNewCertificateChain(RequestCertificateDto requestCertificateDto){
        CertificateType newCertificateType = requestCertificateDto.getCertificateDto().getCertificateType();
        if(newCertificateType != CertificateType.ROOT){
            Certificate issuerCertificate = certificateService.getCertificateBySerialNumber(requestCertificateDto.getCertificateDto().getIssuerSerial());
            KeyStoreReader keyStoreReader = new KeyStoreReader();
            String issuerKeyStore = getCertificateKeyStor(issuerCertificate);

            java.security.cert.Certificate certificateToCheck = keyStoreReader.readCertificate(issuerKeyStore,
                    requestCertificateDto.getKeystoreIssuerPassword(),
                    issuerCertificate.getSerialNumber().toString());
            try {
                certificateToCheck.verify(loadPublicKey(issuerCertificate.getPublicKey()));
            } catch (SignatureException e) {
                System.out.println("\nValidacija neuspesna");
                e.printStackTrace();
            } catch(GeneralSecurityException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /*public boolean checkCertificateChain(Certificate certificate){
        KeyStoreReader keyStoreReader = new KeyStoreReader();
        String certificateKeyStore = getCertificateKeyStor(certificate);
    }*/

    public String getCertificateKeyStor(Certificate certificate){
        String certificateKeyStore = "";
        if (certificate.getCertificateType() == CertificateType.ROOT){
            certificateKeyStore = rootKSPath;
        }else if(certificate.getCertificateType() == CertificateType.INTERMEDIATE){
            certificateKeyStore = intermediateKSPath;
        }else if(certificate.getCertificateType() == CertificateType.END_ENTITY){
            certificateKeyStore = endEntityKSPath;
        }
        return certificateKeyStore;
    }


    public boolean isNotRevoked(Certificate certificate){
        return certificate.getState() == State.VALID;
    }

    public boolean checkNewCertificateDate(RequestCertificateDto requestCertificateDto){
        Date validFrom = requestCertificateDto.getCertificateDto().getValidFrom();
        Date validTo = requestCertificateDto.getCertificateDto().getValidTo();
        CertificateType certificateType = requestCertificateDto.getCertificateDto().getCertificateType();
        Certificate issuerCertificate = certificateService.getCertificateBySerialNumber(requestCertificateDto.getCertificateDto().getIssuerSerial());
        if(checkDate(validFrom, validTo) && certificateType == CertificateType.ROOT){
            return true;
        }else if(checkDate(validFrom, validTo) && validFrom.after(issuerCertificate.getValidFrom()) &&
                validTo.before(issuerCertificate.getValidTo())){
            return true;
        }
        return false;
    }

    public static PublicKey loadPublicKey(String stored) throws GeneralSecurityException, IOException
    {
        byte[] data = Base64.getDecoder().decode((stored.getBytes()));
        X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        return fact.generatePublic(spec);

    }

}
