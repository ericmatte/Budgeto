import { Component, Input } from '@angular/core';
import { Routes, Router } from '@angular/router';

@Component({
  selector: 'menu-item',
  template: `
      <li [routerLinkActive]="['active']">
          <a [routerLink]="url" class="waves-effect waves-teal"><i class="material-icons">{{ icon }}</i>{{ title }}</a>
      </li>
  `
})
export class MenuItemComponent {
  @Input() title: string;
  @Input() icon: string;
  @Input() url: string;

  constructor(private router: Router) { }

}
