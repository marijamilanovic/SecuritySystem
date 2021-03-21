import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ValidationCertificateComponent } from './validation-certificate.component';

describe('ValidationCertificateComponent', () => {
  let component: ValidationCertificateComponent;
  let fixture: ComponentFixture<ValidationCertificateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ValidationCertificateComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ValidationCertificateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
