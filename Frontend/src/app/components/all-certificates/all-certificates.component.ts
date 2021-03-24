import { Component, OnInit } from '@angular/core';
import { CertificateService } from 'src/app/service/certificate.service';

@Component({
  selector: 'app-all-certificates',
  templateUrl: './all-certificates.component.html',
  styleUrls: ['./all-certificates.component.css']
})
export class AllCertificatesComponent implements OnInit {

  certificates: any;
  publicKey: any;
  seeDetails: boolean = false;

  constructor(private certificateService: CertificateService) { }

  ngOnInit(): void {
    this.certificateService.getAllCertificates().subscribe((listCertificate:any) => {
      this.certificates = listCertificate;
    });
  }

  details(i: any){
    this.publicKey = this.certificates[i].publicKey;
    this.seeDetails = true;
  }

  download(i: any){
    this.seeDetails = false;
  }

  cancel(){
    this.seeDetails = false;
  }

}
