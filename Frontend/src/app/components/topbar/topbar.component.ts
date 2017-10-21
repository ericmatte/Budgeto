import { Component, Input, AfterViewInit } from '@angular/core';
import { Routes, Router, NavigationEnd, ActivatedRouteSnapshot } from '@angular/router';
import { SharedService } from '../../services/shared.service';
declare var $: any;

@Component({
  selector: 'app-topbar',
  templateUrl: './topbar.component.html',
  styleUrls: ['./topbar.component.css']
})
export class TopBarComponent implements AfterViewInit {
  title: string;

  constructor(private root: SharedService, private router: Router) { }

  ngAfterViewInit() {
    // Side Navigation fix
    $('.side-nav li a').on('click', function (e) {
      if ($(window).width() < 992) {
        $('.button-collapse').sideNav('hide');
      }
    });
  }

  private getDeepestTitle(routeSnapshot: ActivatedRouteSnapshot) {
    var title = routeSnapshot.data ? routeSnapshot.data['title'] : '';
    if (routeSnapshot.firstChild) {
      title = this.getDeepestTitle(routeSnapshot.firstChild) || title;
    }
    return title;
  }

  ngOnInit() {
    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.title = this.getDeepestTitle(this.router.routerState.snapshot.root);
      }
    });
    this.title = this.getDeepestTitle(this.router.routerState.snapshot.root);
  }
}
