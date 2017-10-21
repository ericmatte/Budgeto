import { Component } from '@angular/core';
import { Routes, Router, } from '@angular/router';
import { SharedService } from './services/shared.service';
import { AuthService } from "angular2-social-login/dist";
import { ApiService } from "./services/api.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  constructor(private router: Router, private root: SharedService, private apiService: ApiService) {
    this.apiService.fetchInitialData().subscribe();
  }

}
