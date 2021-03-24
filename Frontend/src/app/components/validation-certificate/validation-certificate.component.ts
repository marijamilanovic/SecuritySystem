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
    if(check == true){
      this.isChecked = false;
    }else{
      //salji zahtev
      /*this.certificateService..subscribe((data:any[]) => {
        this.availableMeds = data;
        console.log(data);
        this.isChecked = true;
        this.toastrService.info('Medicines are checked.');
      })*/
      this.isChecked = true;
    }
    
  }


}
