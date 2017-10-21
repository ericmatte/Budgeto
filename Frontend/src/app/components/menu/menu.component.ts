import { Component, AfterViewInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from "angular2-social-login";
import { SharedService } from '../../services/shared.service';
declare var $: any;

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
})
export class MenuComponent implements AfterViewInit {

  constructor(private root: SharedService, private router: Router, public _auth: AuthService) { }

  ngAfterViewInit() {
    $('.side-nav').perfectScrollbar();
  }

  logout() {
    this._auth.logout().subscribe(
      (data) => {
        this.root.clearUser();
        this.router.navigate(['/login']);
      }
    )
  }
}
