import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CERTIFICATE_CHAIN_PATH, CERTIFICATE_ISSUERS_PATH, CERTIFICATE_PATH, CERTIFICATE_TYPES_PATH, CERTIFICATE_VALIDATION_PATH, PDF_PATH, ROOT_PATH, REVOKE_PATH  } from '../util/paths';
import { getToken } from '../util/tokenUtil';



@Injectable({
  providedIn: 'root'
})
export class CertificateService {

  constructor(private httpClient: HttpClient) { }

  createAuthorizationHeader() {
    return 'Bearer ' + getToken(); 
  }


  getAllCertificates():any{
    return this.httpClient.get(CERTIFICATE_PATH);
  }

  getTypes():any{
    return this.httpClient.get(CERTIFICATE_TYPES_PATH);
  }

  getAllIssuers():any{
    return this.httpClient.get(CERTIFICATE_ISSUERS_PATH);
  }

  checkCertificate(serialNumber: any){
    return this.httpClient.get(CERTIFICATE_VALIDATION_PATH + '/' + serialNumber);
  }

  createRootCertificate(certificate: any): any{
    return this.httpClient.post(ROOT_PATH, certificate, {headers: {Authorization: this.createAuthorizationHeader()}});
  }

  createIntermediateCertificate(certificate: any): any{
    return this.httpClient.post(CERTIFICATE_PATH, certificate, {headers: {Authorization: this.createAuthorizationHeader()}});
  }

  createEndEntityCertificate(certificate: any): any{
    return this.httpClient.post(CERTIFICATE_PATH, certificate, {headers: {Authorization: this.createAuthorizationHeader()}});
  }

  downloadCert(id: any): any{
    return this.httpClient.get(PDF_PATH + "/" + id);
  }


  viewChain(serialNumber: any):any{
    return this.httpClient.get(CERTIFICATE_CHAIN_PATH + "/" + serialNumber);
  }

  revokeCertificate(serialNumber: number){
    return this.httpClient.delete(REVOKE_PATH + "/" + serialNumber, {headers: {Authorization: this.createAuthorizationHeader()}});
  }

}
