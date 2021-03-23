package com.security.PKISystem.service.impl;

import com.security.PKISystem.certificates.CertificateGenerator;
import com.security.PKISystem.domain.Certificate;
import com.security.PKISystem.domain.*;
import com.security.PKISystem.domain.mapper.CertificateMapper;
import com.security.PKISystem.dto.CertificateDto;
import com.security.PKISystem.dto.RequestCertificateDto;
import com.security.PKISystem.exception.NotFoundException;
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
import java.util.Date;
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
    public X509Certificate addCertificate(RequestCertificateDto requestCertificateDto) {

        //TODO: Proveriti userove sertifikate

        KeyStoreWriter keyStoreWriter = new KeyStoreWriter();
        keyStoreWriter.loadKeyStore(requestCertificateDto.getKeystoreName()+".jks", requestCertificateDto.getKeystorePassword().toCharArray());

        KeyPair keyPairSubject = generateKeyPair();

        List<Certificate> issuerCertificates = this.certificateRepository.findCertificateByIssuerName(requestCertificateDto.getIssuedByName());
        Certificate issuerCertificate = new Certificate();

        for(Certificate c: issuerCertificates){
            CertificateStatus status = this.certificateStatusService.findCertificateStatusByCertificateId(c.getId());
            if(status.getState().equals(State.VALID)){
                issuerCertificate = c;
                break;
            }
        }

        Certificate certForDatabase = new Certificate(Base64.getEncoder().encodeToString(keyPairSubject.getPublic().getEncoded()),
                requestCertificateDto.getIssuedByName(), requestCertificateDto.getIssuedById(),
                requestCertificateDto.getValidFrom(), requestCertificateDto.getValidTo());

        certForDatabase = this.certificateRepository.save(certForDatabase);
        requestCertificateDto.setSerialNumber(certForDatabase.getId().toString());

        KeyStoreReader keyStoreReader = new KeyStoreReader();

        IssuerData issuerData = keyStoreReader.readIssuerFromStore(requestCertificateDto.getKeystoreName(), issuerCertificate.getSerialNumber().toString(), requestCertificateDto.getKeystorePassword().toCharArray(),
                requestCertificateDto.getKeystorePassword().toCharArray());
        SubjectData subjectData = new SubjectData(keyPairSubject, requestCertificateDto);

        CertificateStatus certificateStatus = new CertificateStatus();
        certificateStatus.setCertificateId(certForDatabase.getId());
        certificateStatus.setState(State.VALID);
        this.certificateStatusService.saveCertificateStatus(certificateStatus);


        CertificateGenerator certificateGenerator = new CertificateGenerator();
        X509Certificate certificate = certificateGenerator.generateCertificate(subjectData, issuerData);

        keyStoreWriter.write(requestCertificateDto.getSerialNumber().toString(), keyPairSubject.getPrivate(), requestCertificateDto.getKeystorePassword().toCharArray() , certificate);

        keyStoreWriter.saveKeyStore(requestCertificateDto.getKeystoreName()+".jks", requestCertificateDto.getKeystorePassword().toCharArray());

        return certificate;
    }

    @Override
    public X509Certificate addRootCertificate(RequestCertificateDto requestCertificateDto) {

        KeyStoreWriter keyStoreWriter = new KeyStoreWriter();
        keyStoreWriter.loadKeyStore(requestCertificateDto.getKeystoreName() + ".jks", requestCertificateDto.getKeystorePassword().toCharArray());

        KeyPair keyPairSubject = generateKeyPair();

        Certificate certificateForDatabase = new Certificate(Base64.getEncoder().encodeToString(keyPairSubject.getPublic().getEncoded()),
                requestCertificateDto.getIssuedByName(), requestCertificateDto.getIssuedById(),
                requestCertificateDto.getValidFrom(), requestCertificateDto.getValidTo());

        this.certificateRepository.save(certificateForDatabase);
        Long idIssuer = certificateForDatabase.getIssuerId();
        certificateForDatabase.setIssuerId(idIssuer);
        this.certificateRepository.save(certificateForDatabase);
        
        requestCertificateDto.setSerialNumber(certificateForDatabase.getId().toString());

        CertificateStatus certificateStatus = new CertificateStatus();
        certificateStatus.setCertificateId(certificateForDatabase.getId());
        certificateStatus.setState(State.VALID);
        this.certificateStatusService.saveCertificateStatus(certificateStatus);

        SubjectData subjectData = new SubjectData(keyPairSubject, requestCertificateDto);
        IssuerData issuerData = new IssuerData(keyPairSubject.getPrivate(), requestCertificateDto);

        CertificateGenerator certificateGenerator = new CertificateGenerator();
        X509Certificate certificate = certificateGenerator.generateCertificate(subjectData, issuerData);

        keyStoreWriter.write(requestCertificateDto.getSerialNumber(), keyPairSubject.getPrivate(), requestCertificateDto.getKeystorePassword().toCharArray(), certificate);

        keyStoreWriter.saveKeyStore(requestCertificateDto.getKeystoreName()+".jks", requestCertificateDto.getKeystorePassword().toCharArray());

        return certificate;
    }



    @Override
    public void revokeCertificateChain(Long certificateId) {
        revokeCertificate(certificateId);
        if(certificateRepository.findCertificateById(certificateId).getCertificateType() == CertificateType.END_ENTITY)
            return;
        for(Certificate c : certificateRepository.findCertificateByIssuerId(certificateId)){
            revokeCertificateChain(c.getId());
        }
    }

    // VALIDATION
    @Override
    public boolean isCertificateValid(Long serialNumber, Long issuerId) {
        Certificate certificate = getCertificateBySerialNumberAndIssuerId(serialNumber, issuerId);
        if (certificate != null){
            if(checkDate(certificate.getValidFrom(), certificate.getValidTo()) &&
                    isNotRevoked(certificate))
                return true;
            // todo: certificate chain, provera javnog kljuca
            return false;
        }
        throw new NotFoundException("Certificate not found.");
    }

    public boolean checkDate(Date validFrom, Date validTo){
        long current = new Date().getTime();
        return current >= validFrom.getTime() && current <= validTo.getTime();
    }

    public boolean checkCertificateChain(){
        return true;
    }

    // todo: ocsp
    public boolean isNotRevoked(Certificate certificate){
        return certificate.getState() == State.VALID;
    }

    ///////

    @Override
    public Certificate getCertificateBySerialNumberAndIssuerId(Long serialNumber, Long issuerId) {
        return certificateRepository.findCertificateBySerialNumberAndIssuerId(serialNumber, issuerId);
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
            if(c.getCertificateType().equals("INTERMEDIATE")){
                certificateDtos.add(CertificateMapper.mapCertificateToCertificateDto(c));
            }
        }
        return certificateDtos;
    }


    private void revokeCertificate(Long certificateId){
        Certificate certificate = certificateRepository.findCertificateById(certificateId);
        certificate.setState(State.REVOKED);
        certificateStatusService.revokeCertificate(certificateId);
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
    public List<RequestCertificateDto> findAll() {
        List<Certificate> certificates = certificateRepository.findAll();
        List<RequestCertificateDto> certificateDtos = new ArrayList<>();
        for(Certificate c:certificates){
            certificateDtos.add(new RequestCertificateDto(c));
        }
        return certificateDtos;
    }

    @Override
    public Certificate findCertificateByCertificateType(CertificateType certificateType) { return  this.certificateRepository.findCertificateByCertificateType(certificateType); }

    @Override
    public Certificate findCertificateById(Long id) { return this.certificateRepository.findCertificateById(id); }
}
