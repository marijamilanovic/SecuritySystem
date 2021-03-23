package com.security.PKISystem.domain;

import java.security.KeyPair;
import java.security.PublicKey;
import java.util.Date;

import com.security.PKISystem.dto.RequestCertificateDto;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;

public class SubjectData {

	private PublicKey publicKey;
	private X500Name x500name;
	private String serialNumber;
	private Date startDate;
	private Date endDate;

	public SubjectData() {

	}

	public SubjectData(KeyPair keyPair, RequestCertificateDto certificateDto, Long serialNumber) {
		//TODO: Napraviti mapper
		this.publicKey = keyPair.getPublic();
		X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
		builder.addRDN(BCStyle.CN, certificateDto.getIssuedToCommonName());
		builder.addRDN(BCStyle.SURNAME, certificateDto.getSurname());
		builder.addRDN(BCStyle.GIVENNAME, certificateDto.getGivenName());
		builder.addRDN(BCStyle.O, certificateDto.getOrganisation());
		builder.addRDN(BCStyle.OU, certificateDto.getOrganisationalUnit());
		builder.addRDN(BCStyle.C, certificateDto.getCountry());
		builder.addRDN(BCStyle.E, certificateDto.getEmail());
		builder.addRDN(BCStyle.SERIALNUMBER, serialNumber.toString());
		this.x500name = builder.build();
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

	public X500Name getX500name() {
		return x500name;
	}

	public void setX500name(X500Name x500name) {
		this.x500name = x500name;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
