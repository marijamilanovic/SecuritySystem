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
      var br = 0;

      for(let p of this.certificatesChain){
        br++;
        var label = p.owner + " [" + p.serialNumber + "]";
        this.certificatesChain.push(label);
      }
      console.log(this.certificatesChain);

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
    this.certificateService.revokeCertificate(this.certificates[i].serialNumber).subscribe(() =>{
      alert("Certificate with serial:" + this.certificates[i].serialNumber + " has been revocated");
    }, error => {
      alert("Something went wrong!");
    });
  }

}
