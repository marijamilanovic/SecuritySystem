import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AddCertificateComponent } from './components/add-certificate/add-certificate.component';
import { ValidationCertificateComponent } from './components/validation-certificate/validation-certificate.component';
import { NavbarComponent } from './components/navbar/navbar.component';

const routes: Routes = [
  { path: '', redirectTo: '/addCertificate', pathMatch: 'full' },
  { path: 'addCertificate', component: AddCertificateComponent },
  { path: 'validationCertificate', component: ValidationCertificateComponent },
  { path: 'nav', component: NavbarComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
