import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';

import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgSelectModule } from '@ng-select/ng-select';

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
    AllCertificatesComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    NgbModule,
    NgSelectModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
