import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CERTIFICATE_ISSUERS_PATH, CERTIFICATE_PATH, CERTIFICATE_TYPES_PATH, CERTIFICATE_VALIDATION_PATH, ENDENTITY_PATH, INTERMEDIATE_PATH, PDF_PATH, REVOKE_PATH, ROOT_PATH  } from '../util/paths';


@Injectable({
  providedIn: 'root'
})
export class CertificateService {

  constructor(private httpClient: HttpClient) { }

  getAllCertificates():any{
    return this.httpClient.get(CERTIFICATE_PATH);
  }

  getTypes():any{
    return this.httpClient.get(CERTIFICATE_TYPES_PATH);
  }

  getAllIssuers():any{
    return this.httpClient.get(CERTIFICATE_ISSUERS_PATH);
  }

  checkCertificate(serialNumber: any, issuerSerial: any){
    return this.httpClient.get(CERTIFICATE_VALIDATION_PATH + '/' + serialNumber + '/' + issuerSerial);
  }

  createRootCertificate(certificate: any): any{
    return this.httpClient.post(ROOT_PATH, certificate);
  }

  createIntermediateCertificate(certificate: any): any{
    return this.httpClient.post(CERTIFICATE_PATH, certificate);
  }

  createEndEntityCertificate(certificate: any): any{
    return this.httpClient.post(CERTIFICATE_PATH, certificate);
  }

  generatePdf(id: any): any{
    return this.httpClient.get(PDF_PATH + "/" + id);
  }

  revokeCertificate(serialNumber: number){
    return this.httpClient.delete(REVOKE_PATH + "/" + serialNumber);
  }

}
