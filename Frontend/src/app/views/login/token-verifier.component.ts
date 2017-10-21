import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { ApiService } from "app/services/api.service";
import { User } from "app/classes/user.interface";
import { SharedService } from "app/services/shared.service";

@Component({
  selector: 'app-token-verifier',
  template: `
    <div class="progress" style="margin:0">
        <div class="indeterminate"></div>
    </div>
  `
})
export class TokenVerifierComponent implements OnInit {
  returnUrl: string;

  constructor(private route: ActivatedRoute, private router: Router, private root: SharedService, private apiService: ApiService) { }

  redirectToLogin() {
    // User token invalidated, returning to login page
    this.router.navigate(['/login'], { queryParams: { returnUrl: this.returnUrl } });
  }

  ngOnInit() {
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';

    var user: User = JSON.parse(localStorage.getItem('currentUser'));

    if (user != null) {
      // Validate user token from localstorage with server
      this.apiService.validateToken(user.token || 'invalid').subscribe(
        apiUser => this.router.navigate([this.returnUrl]),
        err => this.redirectToLogin());

    } else {
      this.redirectToLogin();
    }
  }
}
