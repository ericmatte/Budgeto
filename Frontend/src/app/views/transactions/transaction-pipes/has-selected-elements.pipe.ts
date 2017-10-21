import { Pipe, PipeTransform } from '@angular/core';
import { Transaction } from "app/classes/transaction.class";

@Pipe({ name: 'hasSelectedElements', pure: false })
export class HasSelectedElementsPipe implements PipeTransform {

  constructor() { }

  /** 
   * @param {Array<any>}
   * @return {Array<any>}
   */
  transform(transactions: Transaction[]): boolean {
    for (let transaction of transactions) {
      if (transaction.isSelected) {
        return true;
      }
    }
    return false;
  }
}
