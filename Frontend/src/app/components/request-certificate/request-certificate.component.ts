import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbDate, NgbCalendar, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { CertificateRequestService } from 'src/app/service/certificate-request.service';
import { CertificateService } from 'src/app/service/certificate.service';
import { getRoleFromToken, getUsernameFromToken } from 'src/app/util/tokenUtil';

@Component({
  selector: 'app-request-certificate',
  templateUrl: './request-certificate.component.html',
  styleUrls: ['./request-certificate.component.css'],
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
export class RequestCertificateComponent implements OnInit {

  hoveredDate: NgbDate | null = null;
  fromDate: NgbDate;
  toDate: NgbDate | null = null;
  issuers: any[]=[];
  issuer: any;
  certificateDto: any = {certificateType: '', keyUsage:'', issuerSerial:''}
  certificateRequest: any = {commonName: '', givenName:'', surname:'', organisation: '', organisationUnit: '',
                        country: '', email: '', validFrom:'', validTo:'', keyUsage:'', isCA: false, username:'' };
  issuerMod: any = {owner:'', serialNumber:0};
  today: {year: number, month: number, day: number};
  now: any;
  
  constructor(calendar: NgbCalendar, 
              private certificateService: CertificateService,
              private requestService : CertificateRequestService, 
              private toastrService: ToastrService,
              private router: Router, 
              private datePipe: DatePipe) {
    this.fromDate = calendar.getToday();
    this.today = calendar.getToday();
    console.log(this.fromDate);
    this.toDate = calendar.getNext(calendar.getToday(), 'd', 10);
  }

  ngOnInit(): void {
    if(getRoleFromToken() == 'ROLE_ADMIN')
      window.history.back();
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

  createRequest(){
    this.certificateRequest.validFrom = this.fromDate.year + '-' + this.fromDate.month + '-' + this.fromDate.day + ' ' + '00:00:00';
    this.certificateRequest.validTo = this.toDate?.year + '-' + this.toDate?.month + '-' + this.toDate?.day + ' ' + '00:00:00';
    if(!this.fieldChecker()){
      return;
    }
    if (typeof(this.issuer) == 'undefined') {
      this.toastrService.info('Please choose issuer.');
    }
    this.certificateRequest.issuerSerial = this.issuer.serialNumber;
    if(getUsernameFromToken() != null)
      this.certificateRequest.username = getUsernameFromToken();
    this.requestService.createRequest(this.certificateRequest).subscribe(() => {
      this.toastrService.success("Request sent!");
    }, error => {
      this.toastrService.error("")
    });
  }

  fieldChecker(): any{
    if(this.certificateRequest.commonName == '' || this.certificateRequest.surname == '' ||
        this.certificateRequest.givenName == '' || this.certificateRequest.organisation == '' ||
        this.certificateRequest.organisationUnit == '' || this.certificateRequest.country == '' ||
        this.certificateRequest.email == '' ||
        this.certificateRequest.keyUsage == ''){
      this.toastrService.info('Please fill in all fields.');
      return false;
    }
    return true;
  }


}
