import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  registrationDto : any = {username: '', email: '', password: '', passwordRepeat: ''};

  constructor(private userService: UserService,
              private toastrService: ToastrService,
              private router: Router) { }

  ngOnInit(): void {
  }

  register(){
    this.userService.register(this.registrationDto).subscribe(data => {
      this.toastrService.success("You have been registered successfully!");
      this.router.navigate(['/login']);
    }, error => {
      this.toastrService.error("Something went wrong. Please check your input!");
    });
  }

}
