import { Component, OnInit } from '@angular/core';
import { CertificateService } from 'src/app/service/certificate.service';
import {Observable} from 'rxjs';
import {debounceTime, distinctUntilChanged, map} from 'rxjs/operators';

@Component({
  selector: 'app-validation-certificate',
  templateUrl: './validation-certificate.component.html',
  styleUrls: ['./validation-certificate.component.css']
})
export class ValidationCertificateComponent implements OnInit {

  names: any[]=[];
  isChecked: boolean = false;
  cert: any;
  certificates: any[]=[];
  isValid: number = 0;

  constructor(private certificateService: CertificateService) { }

  ngOnInit(): void {
    this.certificateService.getAllCertificates().subscribe((data: any[]) => {
      console.log(data);
      this.certificates = data;
      for(let i of this.certificates){
        let name = i.owner + ' [' + i.serialNumber + '] -- ' + i.certificateType;
        this.names.push(name);
      }
    });
  }

  
  checkCertificate(check: boolean){
    this.isValid = 11;
    if(check == true){
      this.isChecked = false;
    }else{
      this.certificateService.checkCertificate(this.cert.serialNumber).subscribe(data => {
        console.log(data);
        if(data){
          this.isValid = 1;
        } else if(!data){
          this.isValid = 2;
        }  
      }, error => {
        this.isValid = 3;
      });
      this.isChecked = true;
    }
    
  }


}
