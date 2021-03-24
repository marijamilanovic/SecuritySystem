package com.security.PKISystem.service.impl;

import com.security.PKISystem.certificates.CertificateGenerator;
import com.security.PKISystem.domain.Certificate;
import com.security.PKISystem.domain.*;
import com.security.PKISystem.domain.mapper.CertificateMapper;
import com.security.PKISystem.domain.dto.CertificateDto;
import com.security.PKISystem.domain.dto.RequestCertificateDto;
import com.security.PKISystem.keystores.KeyStoreReader;
import com.security.PKISystem.keystores.KeyStoreWriter;
import com.security.PKISystem.repository.CertificateRepository;
import com.security.PKISystem.service.CertificateService;
import com.security.PKISystem.service.CertificateStatusService;
import com.security.PKISystem.service.CertificateValidationService;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.*;

@Service
public class CertificateServiceImpl implements CertificateService {

    @Value("${rootKSPath}")
    private String rootKSPath;
    @Value("${intermediateKSPath}")
    private String intermediateKSPath;
    @Value("${endEntityKSPath}")
    private String endEntityKSPath;

    @Autowired
    private CertificateRepository certificateRepository;
    @Autowired
    private CertificateStatusService certificateStatusService;
    @Autowired
    private CertificateValidationService certificateValidationService;

    public CertificateServiceImpl(){Security.addProvider(new BouncyCastleProvider());}

    @Override
    public Certificate save(Certificate certificate) {
        return certificateRepository.save(certificate);
    }

    @Override
    public X509Certificate addCertificate(RequestCertificateDto requestCertificateDto) {

        if(!certificateValidationService.isNewCertificateValid(requestCertificateDto))
            return null;

        KeyStoreWriter keyStoreWriter = new KeyStoreWriter();
        // TODO: keystore u zavisnosti od tipa sertifikata
        keyStoreWriter.loadKeyStore(requestCertificateDto.getKeystoreName()+".jks", requestCertificateDto.getKeystorePassword().toCharArray());

        KeyPair keyPairSubject = generateKeyPair();

        //todo: ispraviti
        List<Certificate> issuerCertificates = this.certificateRepository.findCertificateByIssuerName(requestCertificateDto.getCertificateDto().getIssuerName());
        Certificate issuerCertificate = new Certificate();

        for(Certificate c: issuerCertificates){
            CertificateStatus status = this.certificateStatusService.findCertificateStatusByCertificateId(c.getId());
            if(status.getState().equals(State.VALID)){
                issuerCertificate = c;
                break;
            }
        }

        Random rand = new Random();
        Long serial = Math.abs(rand.nextLong());
        Certificate certForDatabase = new Certificate(
                serial,
                Base64.getEncoder().encodeToString(keyPairSubject.getPublic().getEncoded()),
                requestCertificateDto.getCertificateDto().getIssuerName(),
                requestCertificateDto.getIssuedToCommonName(),
                requestCertificateDto.getCertificateDto().getIssuerSerial(),
                requestCertificateDto.getCertificateDto().getValidFrom(),
                requestCertificateDto.getCertificateDto().getValidTo(),
                requestCertificateDto.getCertificateDto().getCertificateType(),
                State.VALID);
        this.certificateRepository.save(certForDatabase);

        CertificateStatus certificateStatus = new CertificateStatus();
        certificateStatus.setCertificateId(certForDatabase.getId());
        certificateStatus.setState(State.VALID);
        this.certificateStatusService.saveCertificateStatus(certificateStatus);

        KeyStoreReader keyStoreReader = new KeyStoreReader();

        IssuerData issuerData = keyStoreReader.readIssuerFromStore(requestCertificateDto.getKeystoreName(),
                issuerCertificate.getSerialNumber().toString(), requestCertificateDto.getKeystorePassword().toCharArray(),
                requestCertificateDto.getKeystorePassword().toCharArray());
        SubjectData subjectData = new SubjectData(keyPairSubject, requestCertificateDto, serial);

        CertificateGenerator certificateGenerator = new CertificateGenerator();
        X509Certificate certificate = certificateGenerator.generateCertificate(subjectData, issuerData);

        keyStoreWriter.write(serial.toString(), keyPairSubject.getPrivate(), requestCertificateDto.getKeystorePassword().toCharArray(), certificate);
        keyStoreWriter.saveKeyStore(requestCertificateDto.getKeystoreName()+".jks", requestCertificateDto.getKeystorePassword().toCharArray());

        return certificate;
    }

    @Override
    public X509Certificate addRootCertificate(RequestCertificateDto requestCertificateDto) {
//        if(!isNewCertificateValid(requestCertificateDto))
//            return null;

        KeyStoreWriter keyStoreWriter = new KeyStoreWriter();
        File f = new File(rootKSPath);
        if(f.exists() && !f.isDirectory())
            keyStoreWriter.loadKeyStore(rootKSPath, requestCertificateDto.getKeystorePassword().toCharArray());
        keyStoreWriter.loadKeyStore(null, requestCertificateDto.getKeystorePassword().toCharArray());

        KeyPair keyPairSubject = generateKeyPair();
        Random rand = new Random();
        Long serial = Math.abs(rand.nextLong());

        Certificate certificateForDatabase = new Certificate(
                serial,
                Base64.getEncoder().encodeToString(keyPairSubject.getPublic().getEncoded()),
                requestCertificateDto.getIssuedToCommonName(),
                requestCertificateDto.getIssuedToCommonName(),
                serial,
                requestCertificateDto.getCertificateDto().getValidFrom(),
                requestCertificateDto.getCertificateDto().getValidTo(),
                CertificateType.ROOT, State.VALID);

        this.certificateRepository.save(certificateForDatabase);
//        certificateForDatabase.setIssuerSerial(certificateForDatabase.getIssuerSerial());
//        this.certificateRepository.save(certificateForDatabase);

        CertificateStatus certificateStatus = new CertificateStatus();
        certificateStatus.setCertificateId(certificateForDatabase.getId());
        certificateStatus.setState(State.VALID);
        this.certificateStatusService.saveCertificateStatus(certificateStatus);

        SubjectData subjectData = new SubjectData(keyPairSubject, requestCertificateDto, serial);
        IssuerData issuerData = new IssuerData(keyPairSubject.getPrivate(), requestCertificateDto, serial);

        CertificateGenerator certificateGenerator = new CertificateGenerator();
        X509Certificate certificate = certificateGenerator.generateCertificate(subjectData, issuerData);

        keyStoreWriter.write(serial.toString(), keyPairSubject.getPrivate(), requestCertificateDto.getKeystorePassword().toCharArray(), certificate);
        keyStoreWriter.saveKeyStore(rootKSPath, requestCertificateDto.getKeystorePassword().toCharArray());

        return certificate;
    }


    @Override
    public Certificate getCertificateBySerialNumberAndIssuerId(Long serialNumber, Long issuerId) {
        return certificateRepository.findCertificateBySerialNumberAndIssuerSerial(serialNumber, issuerId);
    }


    @Override
    public List<String> getStates() {
        List<String> states = new ArrayList<>();
        for(State s: State.values())
            states.add(s.toString());
        return states;
    }

    @Override
    public List<String> getCertificateTypes() {
        List<String> types = new ArrayList<>();
        for(CertificateType ct: CertificateType.values())
            types.add(ct.toString());
        return types;
    }

    @Override
    public List<CertificateDto> getAllIssuers() {
        List<CertificateDto> certificateDtos = new ArrayList<>();
        for(Certificate c: certificateRepository.findAll()){
            if(c.getCertificateType() == CertificateType.INTERMEDIATE){
                certificateDtos.add(CertificateMapper.mapCertificateToCertificateDto(c));
            }
        }
        return certificateDtos;
    }

    @Override
    public Certificate findCertificateBySerialNumberAndOwner(Long serialNumber, String owner) {
        return certificateRepository.findCertificateBySerialNumberAndOwner(serialNumber, owner);
    }

    @Override
    public void revokeCertificateChain(Long serialNumber, Long issuerSerial) {
        revokeCertificate(serialNumber, issuerSerial);
        if(certificateRepository.findCertificateById(serialNumber).getCertificateType() == CertificateType.END_ENTITY)
            return;
        for(Certificate c : certificateRepository.findCertificateByIssuerSerial(serialNumber)){
            revokeCertificateChain(c.getId(), c.getIssuerSerial());
        }
    }

    private void revokeCertificate(Long serialNumber, Long issuerSerial){
        Certificate certificate = certificateRepository.findCertificateBySerialNumberAndIssuerSerial(serialNumber, issuerSerial);
        certificate.setState(State.REVOKED);
        certificateStatusService.revokeCertificate(serialNumber);
        certificateRepository.save(certificate);
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
    public List<CertificateDto> findAll() {
        List<CertificateDto> certificateDtos = new ArrayList<>();
        for(Certificate c: certificateRepository.findAll()){
            certificateDtos.add(CertificateMapper.mapCertificateToCertificateDto(c));
        }
        return certificateDtos;
    }

    @Override
    public Certificate findCertificateByCertificateType(CertificateType certificateType) { return  this.certificateRepository.findCertificateByCertificateType(certificateType); }

    @Override
    public Certificate findCertificateById(Long id) { return this.certificateRepository.findCertificateById(id); }
}
