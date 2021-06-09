import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';

import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgSelectModule } from '@ng-select/ng-select';
import { FormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ToastrModule } from 'ngx-toastr';

import { AddCertificateComponent } from './components/add-certificate/add-certificate.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { ValidationCertificateComponent } from './components/validation-certificate/validation-certificate.component';
import { AllCertificatesComponent } from './components/all-certificates/all-certificates.component';

@NgModule({
  declarations: [
    AppComponent,
    AddCertificateComponent,
    NavbarComponent,
    ValidationCertificateComponent,
    AllCertificatesComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    NgbModule,
    NgSelectModule,
    FormsModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot({
      positionClass: 'toast-bottom-right'
    })
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
