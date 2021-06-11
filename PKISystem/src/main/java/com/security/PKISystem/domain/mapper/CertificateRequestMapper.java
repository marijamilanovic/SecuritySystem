package com.security.PKISystem.domain.mapper;

import com.security.PKISystem.domain.CertificateRequest;
import com.security.PKISystem.domain.dto.CertificateRequestDto;

import java.util.ArrayList;
import java.util.List;

public class CertificateRequestMapper {

    public static CertificateRequestDto mapRequestToRequestDto(CertificateRequest request){
        CertificateRequestDto requestDto = new CertificateRequestDto();
        requestDto.setCommonName(request.getCommonName());
        requestDto.setSurname(request.getSurname());
        requestDto.setGivenName(request.getGivenName());
        requestDto.setOrganisation(request.getOrganisation());
        requestDto.setOrganisationUnit(request.getOrganisationUnit());
        requestDto.setEmail(request.getEmail());
        requestDto.setCountry(request.getCountry());
        requestDto.setValidFrom(request.getValidFrom());
        requestDto.setValidTo(request.getValidTo());
        requestDto.setKeyUsage(request.getKeyUsage());
        requestDto.setIsCA(request.getIsCA());
        requestDto.setUsername(request.getUser().getUsername());
        return requestDto;
    }

    public static CertificateRequest mapRequestDtoToRequest(CertificateRequestDto requestDto){
        CertificateRequest request = new CertificateRequest();
        request.setCommonName(requestDto.getCommonName());
        request.setSurname(requestDto.getSurname());
        request.setGivenName(requestDto.getGivenName());
        request.setOrganisation(requestDto.getOrganisation());
        request.setOrganisationUnit(requestDto.getOrganisationUnit());
        request.setEmail(requestDto.getEmail());
        request.setCountry(requestDto.getCountry());
        request.setValidFrom(requestDto.getValidFrom());
        request.setValidTo(requestDto.getValidTo());
        request.setKeyUsage(requestDto.getKeyUsage());
        request.setIsCA(requestDto.getIsCA());
        return request;
    }

    public static List<CertificateRequestDto> mapRequestsToRequestDtos(List<CertificateRequest> requests){
        List<CertificateRequestDto> requestDtos = new ArrayList<>();
        for (CertificateRequest r : requests)
            requestDtos.add(mapRequestToRequestDto(r));
        return requestDtos;
    }
}
