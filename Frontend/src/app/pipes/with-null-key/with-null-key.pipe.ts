import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'withNullKey',  pure: true })
export class WithNullKeyPipe implements PipeTransform {


  /** Filter a list by matching conditions
   * @param {Array<any>} items The list of items to filter
   * @param {{[field: string]: any}} conditions This dict of all conditions to match
   * @return {Array<any>} The filtered array
   */
  transform(items: Array<any>, field: string): Array<any> {
    if (items !== undefined) {
      return items.filter(item => {
        if (item[field] != null) {
          return false;
        }
        return true;
      });
    }
  }
}
