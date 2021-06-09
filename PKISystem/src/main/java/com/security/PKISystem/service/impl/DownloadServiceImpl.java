package com.security.PKISystem.service.impl;

import com.security.PKISystem.domain.Certificate;
import com.security.PKISystem.keystores.KeyStoreReader;
import com.security.PKISystem.service.CertificateService;
import com.security.PKISystem.service.CertificateValidationService;
import com.security.PKISystem.service.DownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.Charset;
import java.security.cert.CertificateEncodingException;
@Service
public class DownloadServiceImpl implements DownloadService {

    @Autowired
    private CertificateService certificateService;
    @Autowired
    private CertificateValidationService certificateValidationService;

    @Override
    public File downloadCertificate(Long id) {
        File file = null;
        try {
            Certificate certificate = certificateService.findCertificateById(id);
            KeyStoreReader keyStoreReader = new KeyStoreReader();
            String certificateKeyStore = certificateValidationService.getCertificateKeyStore(certificate.getCertificateType());
            java.security.cert.Certificate certificateToWrite = keyStoreReader.readCertificate(certificateKeyStore, "ftn", certificate.getSerialNumber().toString());
            file  = writeCertificate(certificateToWrite, id);
            FileInputStream inStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    private File writeCertificate(java.security.cert.Certificate certificate, long id) {
        String home = System.getProperty("user.home");
        File file = new File(home + "\\Documents\\", "certificate" + id + ".cer");
        FileOutputStream outputStream = null;
        byte[] bytes = new byte[0];
        try {
            bytes = certificate.getEncoded();
            outputStream = new FileOutputStream(file);
            outputStream.write(bytes);
            Writer writer = new OutputStreamWriter(outputStream, Charset.forName("UTF-8"));
            writer.write(new sun.misc.BASE64Encoder().encode(bytes));
            writer.flush();
            outputStream.close();
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }
}
