import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { CertificateService } from 'src/app/service/certificate.service';
import { getRoleFromToken } from 'src/app/util/tokenUtil';


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
  chain: any[]=[];
  openedCertificate: any;

  constructor(private certificateService: CertificateService,
              private toastrService: ToastrService) { }

  ngOnInit(): void {
    this.certificateService.getAllCertificates().subscribe((listCertificate:any) => {
      this.certificates = listCertificate;
      console.log(this.certificates);
    });
  }

  details(i: any, c: any){
    this.reverseChain = [];
    this.publicKey = this.certificates[i].publicKey;
    this.openedCertificate = this.certificates[i];
    this.seeDetails = true;
    this.certificateService.viewChain(c.serialNumber).subscribe((data: any) => {
      this.certificatesChain = data;
      this.chain = [];
      var label = "";
      var size = this.certificatesChain.length;
      for(let p of this.certificatesChain){
        var j = '';
        for(var i=0; i<size; i++){
          j += "-";
        }
        label = j + " " + p.owner + " [" + p.serialNumber + "]";
        this.chain.push(label);
        console.log(label)
        size--;
      }
      this.chain.reverse();
    });
  }

  download(i: any){
    this.certificateService.downloadCert(this.certificates[i].id).subscribe((response: any) =>{
      this.toastrService.success("Cerificate downloaded. Check your Documents folder");
    }, () => {
      this.toastrService.error("Something went wrong, try again later!");
    })
    this.seeDetails = false;
  }

  cancel(){
    this.seeDetails = false;
  }

  revoke(i: any){
    this.certificateService.revokeCertificate(this.certificates[i].serialNumber).subscribe(() =>{
      this.toastrService.success("Certificate with serial:" + this.certificates[i].serialNumber + " has been revocated");
      window.location.reload();
    }, error => {
      this.toastrService.error("Something went wrong, try again later!");
    });
  }

  getRole(){
    return getRoleFromToken();
  }

}
