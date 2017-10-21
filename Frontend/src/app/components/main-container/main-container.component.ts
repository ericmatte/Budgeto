import { Component } from '@angular/core';

@Component({
  selector: 'main-container',
  template: `
    <main>
      <div class="container">
        <ng-content></ng-content>
      </div>
    </main>
  `,
  styles: [`
    @media only screen and (max-width: 599px) {
        .container {
           width: 95%;
        }
    }
  `]
})
export class MainContainerComponent {

  constructor() { }

}
