import { Component, OnInit } from '@angular/core';
import { CertificateService } from 'src/app/service/certificate.service';

@Component({
  selector: 'app-validation-certificate',
  templateUrl: './validation-certificate.component.html',
  styleUrls: ['./validation-certificate.component.css']
})
export class ValidationCertificateComponent implements OnInit {

  isChecked: boolean = false;

  constructor(private certificate: CertificateService) { }

  ngOnInit(): void {
    this.certificate.getAllCertificates().subscribe((data: any[]) => {
      console.log(data);
    });
  }

  checkCertificate(check: boolean){
    if(check == true){
      this.isChecked = false;
    }else{
      this.isChecked = true;
    }
    
  }


}
