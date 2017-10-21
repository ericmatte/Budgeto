import { Directive, ElementRef } from '@angular/core';
import { SharedService } from '../services/shared.service';

@Directive({
  selector: '[admin]'
})
export class AdminDirective {

  constructor(private root: SharedService, el: ElementRef) {
    // TODO: Do not create this component is not admin!
    el.nativeElement.hidden = (root.user['is_admin'] == undefined);
  }

}
