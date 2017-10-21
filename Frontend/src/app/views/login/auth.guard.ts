import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { SharedService } from "app/services/shared.service";

@Injectable()
export class AuthGuard implements CanActivate {

    constructor(private router: Router, private root: SharedService) { }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        if (this.root.user != null) {
            // logged in so return true
            return true;
        }

        // not logged in so validate user token with the return url
        this.router.navigate(['/guard'], { queryParams: { returnUrl: state.url }});
        return false;
    }
}