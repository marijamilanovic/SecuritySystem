import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AddCertificateComponent } from './components/add-certificate/add-certificate.component';
import { ValidationCertificateComponent } from './components/validation-certificate/validation-certificate.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import {AllCertificatesComponent} from './components/all-certificates/all-certificates.component';
import { LoginComponent } from './components/login/login.component';
import { RegistrationComponent } from './components/registration/registration.component';

const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'registration', component: RegistrationComponent },
  { path: 'addCertificate', component: AddCertificateComponent },
  { path: 'validationCertificate', component: ValidationCertificateComponent },
  { path: 'nav', component: NavbarComponent },
  { path: 'allCertificates', component: AllCertificatesComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
