import { Injectable } from '@angular/core';
import { User } from "../classes/user.interface";
import { Bank } from "../classes/bank.interface";
import { Category } from "app/classes/category.interface";
import { Transaction } from "app/classes/transaction.class";
declare var $: any;

@Injectable()
export class SharedService {
  user: User;

  transactions: Transaction[] = [];
  banks: Bank[];
  categories: Category[];
  limits: any[];

  setUser(theUser: User, token: string) {
    theUser.token = token;
    localStorage.setItem('currentUser', JSON.stringify(theUser));
    this.user = theUser;
  }

  clearUser() {
    localStorage.removeItem('currentUser');
    this.user = null;
  }

  /** Create a new instance of the object to force update the view */
  updateObject(obj: any) {
    if (obj instanceof Array) { return obj.slice(); }
    return obj;
  }

  constructor() { }
}
