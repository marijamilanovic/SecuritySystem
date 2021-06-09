package com.security.PKISystem.service;

import org.springframework.stereotype.Service;

import java.io.File;
@Service
public interface DownloadService {

    File downloadCertificate(Long id);
}
