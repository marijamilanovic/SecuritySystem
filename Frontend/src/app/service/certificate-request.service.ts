import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CERT_REQUEST_PATH, USERS_REQUESTS } from '../util/paths';

@Injectable({
  providedIn: 'root'
})
export class CertificateRequestService {

  constructor(private httpClient: HttpClient) { }

  getAllRequests(){
    return this.httpClient.get(CERT_REQUEST_PATH);
  }

  getRequestById(id : number){
    return this.httpClient.get(CERT_REQUEST_PATH + "/" + id);
  }

  getUserRequests(username : string){
    return this.httpClient.get(USERS_REQUESTS + "/" + username);
  }

  createRequest(requestDto : any){
    return this.httpClient.post(CERT_REQUEST_PATH, requestDto);
  }
}
