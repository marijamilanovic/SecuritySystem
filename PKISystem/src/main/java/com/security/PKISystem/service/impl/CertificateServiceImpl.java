package com.security.PKISystem.service.impl;

import com.security.PKISystem.certificates.CertificateGenerator;
import com.security.PKISystem.domain.Certificate;
import com.security.PKISystem.domain.*;
import com.security.PKISystem.domain.dto.CertificateDto;
import com.security.PKISystem.domain.dto.RequestCertificateDto;
import com.security.PKISystem.domain.mapper.CertificateMapper;
import com.security.PKISystem.keystores.KeyStoreReader;
import com.security.PKISystem.keystores.KeyStoreWriter;
import com.security.PKISystem.logger.Logger;
import com.security.PKISystem.repository.CertificateRepository;
import com.security.PKISystem.service.CertificateService;
import com.security.PKISystem.service.CertificateValidationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.juli.logging.Log;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.regex.Pattern;

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
    public ResponseEntity addCertificate(RequestCertificateDto requestCertificateDto) {
        // end-entity can't sign certificate
        if(certificateRepository.findCertificateBySerialNumber(requestCertificateDto.getCertificateDto().getIssuerSerial()).getCertificateType() == CertificateType.END_ENTITY){
            Logger.infoMessage("End-entity certificate can't sign certificate.");
            return new ResponseEntity("Issuer have no permission to sign certificate.", HttpStatus.FORBIDDEN);
        }
        // start date if it's today -> add current time
        long validFrom = requestCertificateDto.getCertificateDto().getValidFrom().getTime();
        if(new Date().getTime() - validFrom < 86400000 && new Date().getTime() - validFrom > 0){
            requestCertificateDto.getCertificateDto().setValidFrom(new Date());
        }

        // check input format
        if(!checkName(requestCertificateDto)){
            Logger.error("Input isn't in valid format for certificate with common name " + requestCertificateDto.getIssuedToCommonName(), requestCertificateDto.getEmail());
            return new ResponseEntity("Input isn't in valid format.", HttpStatus.BAD_REQUEST);
        }

        // check date, if it's revoked and check certificate chain
        if(!certificateValidationService.isNewCertificateValid(requestCertificateDto)){
            Logger.error("Certificate with common name " + requestCertificateDto.getIssuedToCommonName() + " didn't created because date isn't valid.", requestCertificateDto.getEmail());
            return new ResponseEntity("Certificate didn't created because date isn't valid.", HttpStatus.BAD_REQUEST);
        }

        KeyStoreWriter keyStoreWriter = new KeyStoreWriter();
        String keyStore = certificateValidationService.getCertificateKeyStore(requestCertificateDto.getCertificateDto().getCertificateType());

        File f = new File(keyStore);
        if(f.exists() && !f.isDirectory()) {
            Logger.info("Load keystore for " + requestCertificateDto.getCertificateDto().getCertificateType(), requestCertificateDto.getEmail());
            keyStoreWriter.loadKeyStore(keyStore, requestCertificateDto.getKeystorePassword().toCharArray());
        }else{
            Logger.info("Create and load keystor for ." + requestCertificateDto.getCertificateDto().getCertificateType(), requestCertificateDto.getEmail());
            keyStoreWriter.loadKeyStore(null, requestCertificateDto.getKeystorePassword().toCharArray());
        }

        KeyPair keyPairSubject = generateKeyPair();
        Logger.infoMessage("Keys for current certificate has been generated.");
        Long serial = generateSerialNumber();
        Logger.infoDb("Serial number for certificate has been generated: " + serial);

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
        Logger.infoDb("Save certificate in database: " + certForDatabase.getSerialNumber());
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
        Logger.infoMessage("Certificate with serial number : " + certForDatabase.getSerialNumber() + " has been generated.");

        keyStoreWriter.write(serial.toString(), keyPairSubject.getPrivate(), requestCertificateDto.getKeystorePassword().toCharArray(), certificate);
        keyStoreWriter.saveKeyStore(keyStore, requestCertificateDto.getKeystorePassword().toCharArray());
        Logger.infoDb("Saved keys for certificate with serial number: " + certForDatabase.getSerialNumber());

        return new ResponseEntity(HttpStatus.OK);
    }

    @Override
    public ResponseEntity addRootCertificate(RequestCertificateDto requestCertificateDto) {
        long validFrom = requestCertificateDto.getCertificateDto().getValidFrom().getTime();
        if(new Date().getTime() - validFrom < 86400000 && new Date().getTime() - validFrom > 0){
            requestCertificateDto.getCertificateDto().setValidFrom(new Date());
        }

        if(!checkName(requestCertificateDto)){
            Logger.infoMessage("Input isn't in valid format for certificate with common name " + requestCertificateDto.getIssuedToCommonName());
            return new ResponseEntity("Input isn't in valid format.", HttpStatus.BAD_REQUEST);
        }

        if(!certificateValidationService.isNewCertificateValid(requestCertificateDto)){
            Logger.infoMessage("Certificate with common name " + requestCertificateDto.getIssuedToCommonName() + " didn't created because date isn't valid.");
            return new ResponseEntity("Certificate didn't created because date isn't valid.", HttpStatus.BAD_REQUEST);
        }


        KeyStoreWriter keyStoreWriter = new KeyStoreWriter();
        File f = new File(rootKSPath);

        if(f.exists() && !f.isDirectory()) {
            Logger.infoMessage("Load keystore for " + requestCertificateDto.getCertificateDto().getCertificateType());
            keyStoreWriter.loadKeyStore(rootKSPath, requestCertificateDto.getKeystorePassword().toCharArray());
        } else {
            Logger.infoMessage("Create and load keystor for ." + requestCertificateDto.getCertificateDto().getCertificateType());
            keyStoreWriter.loadKeyStore(null, requestCertificateDto.getKeystorePassword().toCharArray());
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
        Logger.infoDb("Try to save certificate:" + certificateForDatabase.getSerialNumber());
        this.certificateRepository.save(certificateForDatabase);

        SubjectData subjectData = new SubjectData(keyPairSubject, requestCertificateDto, serial);
        IssuerData issuerData = new IssuerData(keyPairSubject.getPrivate(), requestCertificateDto, serial);

        CertificateGenerator certificateGenerator = new CertificateGenerator();
        X509Certificate certificate = certificateGenerator.generateCertificate(subjectData, issuerData);
        Logger.infoMessage("Certificate with serial number : " + certificateForDatabase.getSerialNumber() + " has been generated.");

        keyStoreWriter.write(serial.toString(), keyPairSubject.getPrivate(), requestCertificateDto.getKeystorePassword().toCharArray(), certificate);
        keyStoreWriter.saveKeyStore(rootKSPath, requestCertificateDto.getKeystorePassword().toCharArray());
        Logger.infoMessage("Saved keys for certificate with serial number: " + certificateForDatabase.getSerialNumber());

        return new ResponseEntity(HttpStatus.OK);
    }

    private boolean checkName(RequestCertificateDto requestCertificateDto) {
        Pattern patternFullName = Pattern.compile("^[a-zA-Z]{4,}(?: [a-zA-Z]+){0,2}$");
        if(patternFullName.matcher(requestCertificateDto.getIssuedToCommonName()).matches() &&
                patternFullName.matcher(requestCertificateDto.getSurname()).matches() &&
                patternFullName.matcher(requestCertificateDto.getGivenName()).matches() &&
                patternFullName.matcher(requestCertificateDto.getOrganisation()).matches() &&
                patternFullName.matcher(requestCertificateDto.getCountry()).matches() && checkEmail(requestCertificateDto.getEmail())){
            return true;
        }
        Logger.infoMessage("Input isn't in valid format.");
        return false;
    }

    private boolean checkEmail(String email) {
        Pattern patternEmail = Pattern.compile("[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$");
        if(patternEmail.matcher(email).matches()){
            return true;
        }
        Logger.infoMessage("Email isn't in valid format.");
        return false;
    }



    @Override
    public Certificate findCertificateById(Long id) {
        Logger.infoDb("Try to find certificate with id: " + id);
        Certificate certificate = certificateRepository.findCertificateById(id);
        if(certificate == null)
            Logger.infoDb("There is no certificate with id: " + id);
        return certificate;
    }

    @Override
    public Certificate getCertificateBySerialNumber(Long serialNumber){
        Logger.infoDb("Try to find certificate with serial number: " + serialNumber);
        Certificate certificate = certificateRepository.findCertificateBySerialNumber(serialNumber);
        if(certificate == null)
            Logger.infoDb("There is no certificate with seral number: " + serialNumber);
        return certificate;
    }



    @Override
    public List<CertificateDto> findAll() {
        List<CertificateDto> certificateDtos = new ArrayList<>();
        for(Certificate c: certificateRepository.findAll()){
            if(c.getValidTo().getTime() <= new Date().getTime()){
                c.setState(State.EXPIRED);
            }
            certificateDtos.add(CertificateMapper.mapCertificateToCertificateDto(c));
        }
        return certificateDtos;
    }


    @Override
    public List<CertificateDto> getValidIssuers() {
        List<CertificateDto> certificateDtos = new ArrayList<>();
        for(Certificate c: certificateRepository.findAll()){
            if(c.getCertificateType() != CertificateType.END_ENTITY && certificateValidationService.isCertificateValid(c.getSerialNumber())){
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
        Certificate certificate = getCertificateBySerialNumber(serialNumber);
        certificate.setState(State.REVOKED);
        certificateRepository.save(certificate);
    }

    private Long generateSerialNumber(){
        Long serialNumber;
        do{
            Random rand = new Random();
            serialNumber = Math.abs(new Long(rand.nextInt(1000000000)));
        }while(getCertificateBySerialNumber(serialNumber) != null);
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
