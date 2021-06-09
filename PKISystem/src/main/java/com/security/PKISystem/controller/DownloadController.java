package com.security.PKISystem.controller;

import com.security.PKISystem.service.DownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@RestController
@RequestMapping("/download")
@CrossOrigin(origins = "http://localhost:4200")
public class DownloadController {
    @Autowired
    private DownloadService downloadService;


    @GetMapping("/{id}")
    public void downloadCertificate(@PathVariable Long id) throws Exception {
        File certificateForDownload = downloadService.downloadCertificate(id);
        if (certificateForDownload == null) {
            throw new Exception();
        }
    }
}
