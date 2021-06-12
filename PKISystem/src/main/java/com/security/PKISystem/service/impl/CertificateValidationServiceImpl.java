package com.security.PKISystem.service.impl;

import com.security.PKISystem.domain.Certificate;
import com.security.PKISystem.domain.CertificateType;
import com.security.PKISystem.domain.State;
import com.security.PKISystem.domain.dto.RequestCertificateDto;
import com.security.PKISystem.exception.NotFoundException;
import com.security.PKISystem.keystores.KeyStoreReader;
import com.security.PKISystem.service.CertificateService;
import com.security.PKISystem.service.CertificateValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Service
public class CertificateValidationServiceImpl implements CertificateValidationService {
    static String keystore = "ftn";

    @Value("${rootKSPath}")
    private String rootKSPath;
    @Value("${intermediateKSPath}")
    private String intermediateKSPath;
    @Value("${endEntityKSPath}")
    private String endEntityKSPath;

    @Autowired
    private CertificateService certificateService;

    @Override
    public boolean isCertificateValid(Long serialNumber) {
        Certificate certificate = certificateService.getCertificateBySerialNumber(serialNumber);
        if (certificate != null){
            if(checkDate(certificate.getValidFrom(), certificate.getValidTo(), false) && isNotRevoked(certificate) && checkCertificateChain(certificate)){
                return true;
            }
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

    public boolean checkDate(Date validFrom, Date validTo, boolean isNew){
        long current = new Date().getTime();
        if(isNew){
            if(validFrom.getTime() >= current-60000 && validTo.getTime() >= current && validFrom.getTime() < validTo.getTime()){
                log.info("Date for new certificate is valid.");
                return true;
            }
            log.error("Date for new certificate isn't valid.");
            return false;
        }
        if(validFrom.getTime() <= current && validTo.getTime() >= current && validFrom.getTime() < validTo.getTime()){
            log.info("Date for certificate is valid.");
            return true;
        }
        log.error("Date for certificate isn't valid.");
        return false;
    }

    public boolean checkNewCertificateChain(RequestCertificateDto requestCertificateDto){
        CertificateType newCertificateType = requestCertificateDto.getCertificateDto().getCertificateType();
        if(newCertificateType != CertificateType.ROOT){
            Certificate issuerCertificate = certificateService.getCertificateBySerialNumber(requestCertificateDto.getCertificateDto().getIssuerSerial());
            if(checkCertificateChain(issuerCertificate))
                return true;
            else
                return false;
        }
        return true;
    }

    public boolean checkCertificateChain(Certificate certificate){
        Certificate currCertificate = certificate;
        while(true) {
            if(!checker(currCertificate))
                return false;
            if(currCertificate.getCertificateType() == CertificateType.ROOT)
                return true;
            currCertificate = certificateService.getCertificateBySerialNumber(currCertificate.getIssuerSerial());
        }
    }

    public boolean checker(Certificate certificate){
        KeyStoreReader keyStoreReader = new KeyStoreReader();
        String certificateKeyStore = getCertificateKeyStore(certificate.getCertificateType());
        java.security.cert.Certificate certificateToCheck = keyStoreReader.readCertificate(certificateKeyStore, keystore, certificate.getSerialNumber().toString());
        try {
            certificateToCheck.verify(loadPublicKey(certificate.getPublicKey()));
        } catch (SignatureException e) {
            System.out.println("\nValidacija neuspesna");
            e.printStackTrace();
        } catch(GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public String getCertificateKeyStore(CertificateType certType){
        String certificateKeyStore = "";
        if (certType == CertificateType.ROOT){
            certificateKeyStore = rootKSPath;
        }else if(certType == CertificateType.INTERMEDIATE){
            certificateKeyStore = intermediateKSPath;
        }else if(certType == CertificateType.END_ENTITY){
            certificateKeyStore = endEntityKSPath;
        }
        return certificateKeyStore;
    }


    @Override
    public boolean isNotRevoked(Certificate certificate){
        return certificate.getState() == State.VALID;
    }

    public boolean checkNewCertificateDate(RequestCertificateDto requestCertificateDto){
        Date validFrom = requestCertificateDto.getCertificateDto().getValidFrom();
        Date validTo = requestCertificateDto.getCertificateDto().getValidTo();
        CertificateType certificateType = requestCertificateDto.getCertificateDto().getCertificateType();
        Certificate issuerCertificate = certificateService.getCertificateBySerialNumber(requestCertificateDto.getCertificateDto().getIssuerSerial());
        if(checkDate(validFrom, validTo, true) && certificateType == CertificateType.ROOT){
            return true;
        }else if(certificateType != CertificateType.ROOT && checkDate(validFrom, validTo, true) && validFrom.after(issuerCertificate.getValidFrom()) &&
                validTo.before(issuerCertificate.getValidTo())){
            return true;
        }
        return false;
    }

    public static PublicKey loadPublicKey(String stored) throws GeneralSecurityException, IOException {
        byte[] data = Base64.getDecoder().decode((stored.getBytes()));
        X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        return fact.generatePublic(spec);

    }

}
