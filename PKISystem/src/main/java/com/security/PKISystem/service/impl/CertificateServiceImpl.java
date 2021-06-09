package com.security.PKISystem.service.impl;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.security.PKISystem.certificates.CertificateGenerator;
import com.security.PKISystem.domain.Certificate;
import com.security.PKISystem.domain.*;
import com.security.PKISystem.domain.dto.CertificateDto;
import com.security.PKISystem.domain.dto.RequestCertificateDto;
import com.security.PKISystem.domain.mapper.CertificateMapper;
import com.security.PKISystem.exception.ForbiddenException;
import com.security.PKISystem.keystores.KeyStoreReader;
import com.security.PKISystem.keystores.KeyStoreWriter;
import com.security.PKISystem.repository.CertificateRepository;
import com.security.PKISystem.service.CertificateService;
import com.security.PKISystem.service.CertificateValidationService;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.security.*;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
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
    private CertificateValidationService certificateValidationService;

    public CertificateServiceImpl(){Security.addProvider(new BouncyCastleProvider());}


    @Override
    public X509Certificate addCertificate(RequestCertificateDto requestCertificateDto) {
        if(certificateRepository.findCertificateBySerialNumber(requestCertificateDto.getCertificateDto().getIssuerSerial()).getCertificateType() == CertificateType.END_ENTITY)
            throw new ForbiddenException("Issuer have no permission to sign certificate.");
        if(!certificateValidationService.isNewCertificateValid(requestCertificateDto))
            return null;

        KeyStoreWriter keyStoreWriter = new KeyStoreWriter();
        String keyStore = certificateValidationService.getCertificateKeyStore(requestCertificateDto.getCertificateDto().getCertificateType());

        File f = new File(keyStore);
        if(f.exists() && !f.isDirectory()) {
            keyStoreWriter.loadKeyStore(keyStore, requestCertificateDto.getKeystorePassword().toCharArray());
        }else{
            keyStoreWriter.loadKeyStore(null, requestCertificateDto.getKeystorePassword().toCharArray());
        }

        KeyPair keyPairSubject = generateKeyPair();
        Long serial = generateSerialNumber();

        Certificate certForDatabase = new Certificate(
                serial,
                Base64.getEncoder().encodeToString(keyPairSubject.getPublic().getEncoded()),
                requestCertificateDto.getCertificateDto().getIssuerName(),
                requestCertificateDto.getIssuedToCommonName(),
                requestCertificateDto.getCertificateDto().getIssuerSerial(),
                requestCertificateDto.getCertificateDto().getValidFrom(),
                requestCertificateDto.getCertificateDto().getValidTo(),
                requestCertificateDto.getCertificateDto().getCertificateType(),
                State.VALID,
                requestCertificateDto.getCertificateDto().getKeyUsage());
        this.certificateRepository.save(certForDatabase);

        KeyStoreReader keyStoreReader = new KeyStoreReader();

        // TODO: Provera da li je issuer root ili intermediate i postavljanje putanje
        String path = certificateValidationService.getCertificateKeyStore(certificateRepository.findCertificateBySerialNumber(certForDatabase.getIssuerSerial()).getCertificateType());
        IssuerData issuerData = keyStoreReader.readIssuerFromStore(path,
                requestCertificateDto.getCertificateDto().getIssuerSerial().toString(), requestCertificateDto.getKeystorePassword().toCharArray(),
                requestCertificateDto.getKeystorePassword().toCharArray());
        SubjectData subjectData = new SubjectData(keyPairSubject, requestCertificateDto, serial);

        CertificateGenerator certificateGenerator = new CertificateGenerator();
        X509Certificate certificate = certificateGenerator.generateCertificate(subjectData, issuerData);

        keyStoreWriter.write(serial.toString(), keyPairSubject.getPrivate(), requestCertificateDto.getKeystorePassword().toCharArray(), certificate);
        keyStoreWriter.saveKeyStore(keyStore, requestCertificateDto.getKeystorePassword().toCharArray());

        return certificate;
    }

    @Override
    public ResponseEntity addRootCertificate(RequestCertificateDto requestCertificateDto) {
        // TODO: Check date validity only
        long validFrom = requestCertificateDto.getCertificateDto().getValidFrom().getTime();
        if(new Date().getTime() - validFrom < 86400000 && new Date().getTime() - validFrom > 0){
            requestCertificateDto.getCertificateDto().setValidFrom(new Date());
            System.out.println(requestCertificateDto.getCertificateDto().getValidFrom());
        }
        if(!certificateValidationService.isNewCertificateValid(requestCertificateDto))
            return new ResponseEntity("Certificate didn't created because date isn't valid.", HttpStatus.OK);

        KeyStoreWriter keyStoreWriter = new KeyStoreWriter();
        File f = new File(rootKSPath);
        String pass = "ftn";
        if(f.exists() && !f.isDirectory()) {
            keyStoreWriter.loadKeyStore(rootKSPath, pass.toCharArray());
        } else {
            keyStoreWriter.loadKeyStore(null, pass.toCharArray());
        }

        KeyPair keyPairSubject = generateKeyPair();
        Long serial = generateSerialNumber();

        Certificate certificateForDatabase = new Certificate(
                serial,
                Base64.getEncoder().encodeToString(keyPairSubject.getPublic().getEncoded()),
                requestCertificateDto.getIssuedToCommonName(),
                requestCertificateDto.getIssuedToCommonName(),
                serial,
                requestCertificateDto.getCertificateDto().getValidFrom(),
                requestCertificateDto.getCertificateDto().getValidTo(),
                CertificateType.ROOT, State.VALID,
                requestCertificateDto.getCertificateDto().getKeyUsage());
        this.certificateRepository.save(certificateForDatabase);
        System.out.println(requestCertificateDto.getCertificateDto().getValidFrom());

        SubjectData subjectData = new SubjectData(keyPairSubject, requestCertificateDto, serial);
        IssuerData issuerData = new IssuerData(keyPairSubject.getPrivate(), requestCertificateDto, serial);

        CertificateGenerator certificateGenerator = new CertificateGenerator();
        X509Certificate certificate = certificateGenerator.generateCertificate(subjectData, issuerData);

        keyStoreWriter.write(serial.toString(), keyPairSubject.getPrivate(), requestCertificateDto.getKeystorePassword().toCharArray(), certificate);
        keyStoreWriter.saveKeyStore(rootKSPath, requestCertificateDto.getKeystorePassword().toCharArray());

        return new ResponseEntity(certificate, HttpStatus.OK);
    }

    @Override
    public Certificate findCertificateById(Long id) { return certificateRepository.findCertificateById(id); }

    @Override
    public Certificate getCertificateBySerialNumber(Long serialNumber){
        return certificateRepository.findCertificateBySerialNumber(serialNumber);
    }

    @Override
    public Certificate getCertificateBySerialNumberAndIssuerId(Long serialNumber, Long issuerId) {
        return certificateRepository.findCertificateBySerialNumberAndIssuerSerial(serialNumber, issuerId);
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
    public List<CertificateDto> getAllIssuers() {
        List<CertificateDto> certificateDtos = new ArrayList<>();
        for(Certificate c: certificateRepository.findAll()){
            if(c.getCertificateType() == CertificateType.INTERMEDIATE || c.getCertificateType() == CertificateType.ROOT){
                certificateDtos.add(CertificateMapper.mapCertificateToCertificateDto(c));
            }
        }
        return certificateDtos;
    }

    @Override
    public List<CertificateDto> getCertificateChain(Long serialNumber){
        Certificate currCertificate = getCertificateBySerialNumber(serialNumber);
        List<CertificateDto> certificateDtos = new ArrayList<>();
        while(true) {
            certificateDtos.add(CertificateMapper.mapCertificateToCertificateDto(currCertificate));
            if(currCertificate.getCertificateType() == CertificateType.ROOT){
                return certificateDtos;
            }
            currCertificate = getCertificateBySerialNumber(currCertificate.getIssuerSerial());
        }
    }

    @Override
    public void revokeCertificateChain(Long serialNumber) {
        revokeCertificate(serialNumber);
        if(certificateRepository.findCertificateBySerialNumber(serialNumber).getCertificateType() == CertificateType.END_ENTITY)
            return;
        for(Certificate c : certificateRepository.findCertificateByIssuerSerial(serialNumber)){
            if(c.getSerialNumber().equals(c.getIssuerSerial()))
                continue;
            revokeCertificateChain(c.getSerialNumber());
        }
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


    private void revokeCertificate(Long serialNumber){
        Certificate certificate = certificateRepository.findCertificateBySerialNumber(serialNumber);
        certificate.setState(State.REVOKED);
        certificateRepository.save(certificate);
    }

    private Long generateSerialNumber(){
        Long serialNumber;
        do{
            Random rand = new Random();
            serialNumber = Math.abs(new Long(rand.nextInt(1000000000)));
        }while(certificateRepository.findCertificateBySerialNumber(serialNumber) != null);
        return serialNumber;
    }

    // TODO: ECC algoritam
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

}
