package com.security.PKISystem.domain;

import com.security.PKISystem.domain.dto.RequestCertificateDto;
import com.security.PKISystem.domain.mapper.CertificateMapper;
import lombok.Data;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;

import java.security.PrivateKey;

@Data
public class IssuerData {

	private X500Name x500name;
	private PrivateKey privateKey;

	public IssuerData() {
	}

	public IssuerData(PrivateKey privateKey, X500Name x500name) {
		this.privateKey = privateKey;
		this.x500name = x500name;
	}
	public IssuerData(PrivateKey privateKey, RequestCertificateDto certificateDto, Long serialNumber) {
		this.privateKey = privateKey;
		this.x500name = CertificateMapper.mapCertificateDtoToX500name(certificateDto, serialNumber);;
	}

}
