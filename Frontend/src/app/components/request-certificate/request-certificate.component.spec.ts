import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RequestCertificateComponent } from './request-certificate.component';

describe('RequestCertificateComponent', () => {
  let component: RequestCertificateComponent;
  let fixture: ComponentFixture<RequestCertificateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RequestCertificateComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RequestCertificateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
