import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CERT_REQUEST_PATH, USERS_REQUESTS } from '../util/paths';
import { getToken } from '../util/tokenUtil';

@Injectable({
  providedIn: 'root'
})
export class CertificateRequestService {

  constructor(private httpClient: HttpClient) { }

  createAuthorizationHeader() {
    return 'Bearer ' + getToken(); 
  }

  getAllRequests(){
    return this.httpClient.get(CERT_REQUEST_PATH, {headers: {Authorization: this.createAuthorizationHeader()}});
  }

  getRequestById(id : number){
    return this.httpClient.get(CERT_REQUEST_PATH + "/" + id, {headers: {Authorization: this.createAuthorizationHeader()}});
  }

  getUserRequests(username : string){
    return this.httpClient.get(USERS_REQUESTS + "/" + username, {headers: {Authorization: this.createAuthorizationHeader()}});
  }

  createRequest(requestDto : any){
    return this.httpClient.post(CERT_REQUEST_PATH, requestDto);
  }
}
