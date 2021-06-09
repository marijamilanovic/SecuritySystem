import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import {NgbDate, NgbCalendar, NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';
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
  issuer: any;
  certificateDto: any = {certificateType: '', keyUsage:'', issuerSerial:''}
  requestCertificate: any = {issuedToCommonName: '', surname: '', givenName: '', organisation: '', organisationalUnit: '', country: '', email: '', certificateDto: this.certificateDto, keystorePassword:'ftn', keystoreIssuedPassword:'ftn'};
  issuerMod: any = {owner:'', serialNumber:0};
  today: {year: number, month: number, day: number};
  now: any;
  
  constructor(calendar: NgbCalendar, private certificateService: CertificateService, private toastrService: ToastrService,
              private router: Router, private datePipe: DatePipe) {
    this.fromDate = calendar.getToday();
    this.today = calendar.getToday();
    console.log(this.fromDate);
    this.toDate = calendar.getNext(calendar.getToday(), 'd', 10);
  }

  ngOnInit(): void {
    this.certificateService.getTypes().subscribe((data: any[]) => {
      this.types = data;
    });
    this.certificateService.getAllIssuers().subscribe((data: any[]) => {
      this.issuers = data;
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
    this.requestCertificate.certificateDto.validFrom = this.fromDate.year + '-' + this.fromDate.month + '-' + this.fromDate.day + ' ' + '00:00:00';
    this.requestCertificate.certificateDto.validTo = this.toDate?.year + '-' + this.toDate?.month + '-' + this.toDate?.day + ' ' + '00:00:00';
    if(!this.fieldChecker()){
      return;
    }
    if(this.requestCertificate.certificateDto.certificateType == 'ROOT'){
      this.createRoot();
      return;
    }
    if (typeof(this.issuer) == 'undefined') {
      this.toastrService.info('Please choose issuer.');
    }
    this.requestCertificate.certificateDto.issuerName = this.issuer.owner;
    this.requestCertificate.certificateDto.issuerSerial = this.issuer.serialNumber;
    if(this.requestCertificate.certificateDto.certificateType == 'INTERMEDIATE'){
      this.createIntermediate();
    }

    if(this.requestCertificate.certificateDto.certificateType == 'END_ENTITY'){
      this.createEndEntity();
    }
  }

  createRoot(): void{
    this.certificateService.createRootCertificate(this.requestCertificate).subscribe((response: any) =>{
        this.toastrService.success('Added new root certificate.');
        this.router.navigateByUrl('/', {skipLocationChange: true}).then(() => {
          this.router.navigate(['allCertificates']);
        });
     }, (err: any)=>{
        this.toastrService.error(err.error.text);
     })
  }

  createIntermediate(): void{
    this.certificateService.createIntermediateCertificate(this.requestCertificate).subscribe((response: any) =>{
      this.toastrService.success('Added new intermediate certificate.');
      this.router.navigateByUrl('/', {skipLocationChange: true}).then(() => {
        this.router.navigate(['allCertificates']);
      }); 
     }, (err: any)=>{
      this.toastrService.error(err.error.text);
     })
  }

  createEndEntity(){
    this.certificateService.createEndEntityCertificate(this.requestCertificate).subscribe((response: any) =>{
        this.toastrService.success('Added new end-entity certificate.');
        this.router.navigateByUrl('/', {skipLocationChange: true}).then(() => {
          this.router.navigate(['allCertificates']);
        });
     }, (err: any)=>{
        this.toastrService.error(err.error.text);
     })
  }

  fieldChecker(): any{
    if(this.requestCertificate.issuedToCommonName == '' || this.requestCertificate.surname == '' ||
        this.requestCertificate.givenName == '' || this.requestCertificate.organisation == '' ||
        this.requestCertificate.organisationalUnit == '' || this.requestCertificate.country == '' ||
        this.requestCertificate.email == '' || this.requestCertificate.certificateDto.certificateType == '' ||
        this.requestCertificate.certificateDto.keyUsage == ''){
      this.toastrService.info('Please fill in all fields.');
      return false;
    }
    return true;
  }


}


