import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LOGIN_PATH, REGISTER_PATH } from '../util/paths';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private httpClient: HttpClient) { }

  login(loginDto : any){
    return this.httpClient.post(LOGIN_PATH, loginDto);
  }

  register(registerDto : any){
    return this.httpClient.post(REGISTER_PATH, registerDto);
  }
}
