import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CERTIFICATE_PATH } from '../util/paths';

@Injectable({
  providedIn: 'root'
})
export class CertificateService {

  constructor(private httpClient: HttpClient) { }

  getAllCertificates():any{
    return this.httpClient.get(CERTIFICATE_PATH);
  }

}
