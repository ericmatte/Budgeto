import { Component, OnInit, AfterViewInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from "angular2-social-login";
import { ApiService } from "app/services/api.service";
import { GoogleUser } from "app/classes/google-user.interface";
import { SharedService } from "app/services/shared.service";
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  returnUrl: string;
  sub: any;
  constructor(private route: ActivatedRoute, private router: Router, private root: SharedService,
    public _auth: AuthService, private apiService: ApiService) { }

  getUserFromAPI(googleUser: GoogleUser) {
    this.apiService.validateToken(googleUser.token).subscribe(
      isOk => this.router.navigate([this.returnUrl]),
      err => alert('Error: Unable to validate your crendentials!'));
  }

  signIn(provider) {
    this.sub = this._auth.login(provider).subscribe(
      (googleUser: GoogleUser) => {
        this.getUserFromAPI(googleUser);
      }
    )
  }
  
  ngOnInit() {
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/budget';
    if (this.root.user != null) {
      this.router.navigate([this.returnUrl]);
    }
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }
}
