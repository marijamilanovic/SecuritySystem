import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { CertificateRequestService } from 'src/app/service/certificate-request.service';

@Component({
  selector: 'app-all-requests',
  templateUrl: './all-requests.component.html',
  styleUrls: ['./all-requests.component.css']
})
export class AllRequestsComponent implements OnInit {

  seeDetails: boolean = false;
  requests: any[] = [];
  openedRequest: any = {commonName: '', givenName:'', surname:'', organisation: '', organisation_unit: '',
                        country: '', email: '', validFrom:'', validTo:'', keyUsage:'', isCA: '' };

  constructor(private requestService: CertificateRequestService,
              private toastrService : ToastrService,
              private router: Router) { }

  ngOnInit(): void {
    this.requestService.getAllRequests().subscribe((data : any) => {
      this.requests = data;
    }, error => {
      this.toastrService.error("Error while loading certificate requests!");
    });
  }

  details(i : number){
    this.openedRequest = this.requests[i];
    this.seeDetails = true;
  }

  approve(){
    this.toastrService.info("Feature coming soon");
  }

  cancel(){
    this.seeDetails = false;
  }

}
