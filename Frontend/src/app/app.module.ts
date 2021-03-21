import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';

import { AddCertificateComponent } from './components/add-certificate/add-certificate.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { ValidationCertificateComponent } from './components/validation-certificate/validation-certificate.component';

@NgModule({
  declarations: [
    AppComponent,
    AddCertificateComponent,
    NavbarComponent,
    ValidationCertificateComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
