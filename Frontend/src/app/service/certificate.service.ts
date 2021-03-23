import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CERTIFICATE_ISSUERS_PATH, CERTIFICATE_PATH, CERTIFICATE_TYPES_PATH } from '../util/paths';

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

}
