package com.security.PKISystem.domain;

import com.security.PKISystem.domain.dto.RequestCertificateDto;
import com.security.PKISystem.domain.mapper.CertificateMapper;
import lombok.Data;
import org.bouncycastle.asn1.x500.X500Name;

import java.security.KeyPair;
import java.security.PublicKey;
import java.util.Date;

@Data
public class SubjectData {

	private PublicKey publicKey;
	private X500Name x500name;
	private String serialNumber;
	private Date startDate;
	private Date endDate;

	public SubjectData() {}

	public SubjectData(KeyPair keyPair, RequestCertificateDto certificateDto, Long serialNumber) {
		//TODO: Napraviti mapper
		this.publicKey = keyPair.getPublic();
		this.x500name = CertificateMapper.mapCertificateDtoToX500name(certificateDto, serialNumber);
		this.serialNumber = serialNumber.toString();
		this.startDate = certificateDto.getCertificateDto().getValidFrom();
		this.endDate = certificateDto.getCertificateDto().getValidTo();
	}

	public SubjectData(PublicKey publicKey, X500Name x500name, String serialNumber, Date startDate, Date endDate) {
		this.publicKey = publicKey;
		this.x500name = x500name;
		this.serialNumber = serialNumber;
		this.startDate = startDate;
		this.endDate = endDate;
	}
}
