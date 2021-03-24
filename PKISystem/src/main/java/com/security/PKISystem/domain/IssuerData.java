package com.security.PKISystem.domain;

import com.security.PKISystem.domain.dto.RequestCertificateDto;
import com.security.PKISystem.domain.mapper.CertificateMapper;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;

import java.security.PrivateKey;


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

	public X500Name getX500name() {
		return x500name;
	}

	public void setX500name(X500Name x500name) {
		this.x500name = x500name;
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(PrivateKey privateKey) {
		this.privateKey = privateKey;
	}

}
