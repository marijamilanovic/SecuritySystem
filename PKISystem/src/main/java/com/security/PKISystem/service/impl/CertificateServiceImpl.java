package com.security.PKISystem.service.impl;

import com.security.PKISystem.certificates.CertificateGenerator;
import com.security.PKISystem.domain.*;
import com.security.PKISystem.domain.Certificate;
import com.security.PKISystem.domain.CertificateType;
import com.security.PKISystem.dto.AddCertificateDto;
import com.security.PKISystem.keystores.KeyStoreReader;
import com.security.PKISystem.keystores.KeyStoreWriter;
import com.security.PKISystem.repository.CertificateRepository;
import com.security.PKISystem.service.CertificateService;
import com.security.PKISystem.service.CertificateStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;

import java.util.List;

@Service
public class CertificateServiceImpl implements CertificateService {
    @Autowired
    private CertificateRepository certificateRepository;
    @Autowired
    private CertificateStatusService certificateStatusService;

    @Override
    public Certificate save(Certificate certificate) {
        return certificateRepository.save(certificate);
    }

    @Override
    public X509Certificate addCertificate(AddCertificateDto addCertificateDto) {

        //TODO: Proveriti userove sertifikate

        KeyStoreWriter keyStoreWriter = new KeyStoreWriter();
        KeyPair keyPairSubject = generateKeyPair();

        Certificate certForDatabase = new Certificate(Base64.getEncoder().encodeToString(keyPairSubject.getPublic().getEncoded()),
                addCertificateDto.getIssuedByName(), addCertificateDto.getIssuedById(),
                addCertificateDto.getValidFrom(), addCertificateDto.getValidTo());

        certForDatabase = this.certificateRepository.save(certForDatabase);

        //TODO: Maja
        addCertificateDto.setSerialNumber(certForDatabase.getId());

        KeyStoreReader keyStoreReader = new KeyStoreReader();
        //TODO: Napraviti IssuerData
        IssuerData issuerData = new IssuerData();
        SubjectData subjectData = new SubjectData(keyPairSubject, addCertificateDto);


        CertificateStatus certificateStatus = new CertificateStatus();
        certificateStatus.setCertificateId(certForDatabase.getId());
        certificateStatus.setState(State.VALID);
        this.certificateStatusService.saveCertificateStatus(certificateStatus);


        CertificateGenerator certificateGenerator = new CertificateGenerator();
        X509Certificate certificate = certificateGenerator.generateCertificate(subjectData, issuerData);

        //TODO: Password

//        keyStoreWriter.write(addCertificateDto.getSerialNumber(), keyPairSubject.getPrivate(), Password , certificate);

//        keyStoreWriter.saveKeyStore(addCertificateDto.getKeystoreName()+".jks", addCertificateDto.getKeystorePassword().toCharArray());


        return certificate;
    }

    private KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(2048, random);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<AddCertificateDto> findAll() {
        List<Certificate> certificates = certificateRepository.findAll();
        List<AddCertificateDto> certificateDtos = new ArrayList<>();
        for(Certificate c:certificates){
            certificateDtos.add(new AddCertificateDto(c));
        }
        return certificateDtos;
    }

    @Override
    public Certificate findCertificateByCertificateType(CertificateType certificateType) { return  this.certificateRepository.findCertificateByCertificateType(certificateType); }

    @Override
    public Certificate findCertificateById(Long id) { return this.certificateRepository.findCertificateById(id); }
}
