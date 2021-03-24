import { createUrlResolverWithoutPackagePrefix } from '@angular/compiler';
import { Component, OnInit } from '@angular/core';
import {NgbDate, NgbCalendar} from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { CertificateService } from 'src/app/service/certificate.service';

@Component({
  selector: 'app-add-certificate',
  templateUrl: './add-certificate.component.html',
  styleUrls: ['./add-certificate.component.css'],
  styles: [`
    .custom-day {
      text-align: center;
      padding: 0.185rem 0.25rem;
      display: inline-block;
      height: 2rem;
      width: 2rem;
    }
    .custom-day.focused {
      background-color: #e6e6e6;
    }
    .custom-day.range, .custom-day:hover {
      background-color: rgb(2, 117, 216);
      color: white;
    }
    .custom-day.faded {
      background-color: rgba(2, 117, 216, 0.5);
    }
  `]
})
export class AddCertificateComponent implements OnInit {

  hoveredDate: NgbDate | null = null;
  fromDate: NgbDate;
  toDate: NgbDate | null = null;
  types: any[]=[];
  issuers: any[]=[];
  certificateDto: any = {certificateType: ''}
  requestCertificate: any = {issuedToCommonName: '', surname: '', givenName: '', organisation: '', organisationalUnit: '', country: '', email: '', certificateDto: this.certificateDto};

  constructor(calendar: NgbCalendar, private certificateService: CertificateService) { 
    this.fromDate = calendar.getToday();
    this.toDate = calendar.getNext(calendar.getToday(), 'd', 10);
  }

  ngOnInit(): void {
    this.certificateService.getTypes().subscribe((data: any[]) => {
      this.types = data;
      console.log(this.types);
    });
    this.certificateService.getAllIssuers().subscribe((data: any[]) => {
      this.issuers = data;
      console.log(this.issuers);
    });
  }

  onDateSelection(date: NgbDate) {
    if (!this.fromDate && !this.toDate) {
      this.fromDate = date;
    } else if (this.fromDate && !this.toDate && date.after(this.fromDate)) {
      this.toDate = date;
    } else {
      this.toDate = null;
      this.fromDate = date;
    }
  }

  isHovered(date: NgbDate) {
    return this.fromDate && !this.toDate && this.hoveredDate && date.after(this.fromDate) && date.before(this.hoveredDate);
  }

  isInside(date: NgbDate) {
    return this.toDate && date.after(this.fromDate) && date.before(this.toDate);
  }

  isRange(date: NgbDate) {
    return date.equals(this.fromDate) || (this.toDate && date.equals(this.toDate)) || this.isInside(date) || this.isHovered(date);
  }

  addNewCertificate(){

    this.requestCertificate.certificateDto.validFrom = this.fromDate.year + '-' + this.fromDate.month + '-' + this.fromDate.day;
    this.requestCertificate.certificateDto.validTo = this.toDate?.year + '-' + this.toDate?.month + '-' + this.toDate?.day;
    
    if(this.requestCertificate.certificateDto.certificateType == 'ROOT'){
      this.createRoot();
    }

    if(this.requestCertificate.certificateDto.certificateType == 'INTERMEDIATE'){
      this.createIntermediate();
    }

    if(this.requestCertificate.certificateDto.certificateType == 'END_ENTITY'){
      this.createEndEntity();
    }
    
  }

  createRoot(){
    this.certificateService.createRootCertificate(this.requestCertificate).subscribe((response: any) =>{
      alert("Added certificate");
     }, (err: any)=>{
       alert("Error while create certificate "+err);
     })
  }

  createIntermediate(){
    this.certificateService.createIntermediateCertificate(this.requestCertificate).subscribe((response: any) =>{
      alert("Added intermediate certificate");
     }, (err: any)=>{
       alert("Error while create certificate "+err);
     })
  }

  createEndEntity(){
    this.certificateService.createEndEntityCertificate(this.requestCertificate).subscribe((response: any) =>{
      alert("Added end-entity certificate");
     }, (err: any)=>{
       alert("Error while create certificate "+err);
     })
  }

}
