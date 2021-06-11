package com.security.PKISystem.controller;

import com.security.PKISystem.domain.CertificateRequest;
import com.security.PKISystem.domain.dto.CertificateRequestDto;
import com.security.PKISystem.domain.mapper.CertificateRequestMapper;
import com.security.PKISystem.service.CertificateRequestService;
import com.security.PKISystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/request")
public class CertificateRequestController {

    @Autowired
    private CertificateRequestService requestService;
    @Autowired
    private UserService userService;

    @GetMapping
    private List<CertificateRequestDto> getAllRequests(){
        return CertificateRequestMapper.mapRequestsToRequestDtos(requestService.findAll());
    }

    @GetMapping("/user/{username}")
    private List<CertificateRequestDto> getUserRequests(@PathVariable String username){
        return CertificateRequestMapper.mapRequestsToRequestDtos(requestService.findRequestsByUserUsername(username));
    }

    @GetMapping("/{id}")
    private CertificateRequestDto getRequestById(@PathVariable Long id){
        return CertificateRequestMapper.mapRequestToRequestDto(requestService.findRequestById(id));
    }

    @PostMapping
    private void createRequest(@RequestBody CertificateRequestDto requestDto){
        CertificateRequest c = CertificateRequestMapper.mapRequestDtoToRequest(requestDto);
        c.setUser(userService.findByUsername(requestDto.getUsername()));
        requestService.createRequest(c);
    }
}
