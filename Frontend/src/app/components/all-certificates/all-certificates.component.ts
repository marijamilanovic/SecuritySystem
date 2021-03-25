import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
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
  certificatesChain: any[]=[];
  reverseChain: any[]=[];

  constructor(private certificateService: CertificateService) { }

  ngOnInit(): void {
    this.certificateService.getAllCertificates().subscribe((listCertificate:any) => {
      this.certificates = listCertificate;
    });
  }

  details(i: any, c: any){
    this.reverseChain = [];
    this.publicKey = this.certificates[i].publicKey;
    this.seeDetails = true;
    this.certificateService.viewChain(c.serialNumber).subscribe((data: any) => {
      this.certificatesChain = data;
      console.log(this.certificatesChain);
      for (var i = this.certificatesChain.length - 1; i >= 0; i--) {
        var minus = "--";
        console.log(this.certificatesChain[i]);
        for(var j = 0; j<i; i++){
          minus = minus.concat(minus);
        }
        this.certificatesChain[i].label = minus;
        this.reverseChain.push(this.certificatesChain[i]);
      }
      console.log(this.reverseChain);
    });
  }

  download(i: any){
    this.certificateService.generatePdf(this.certificates[i].id).subscribe((response: any) =>{
      alert("Generated pdf");
    })
    this.seeDetails = false;
  }

  cancel(){
    this.seeDetails = false;
  }

  revoke(i: any){
    // uraditi
  }

}
