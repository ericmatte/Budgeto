import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'filter',  pure: true })
export class FilterPipe implements PipeTransform {


  /** Filter a list by matching conditions
   * @param {Array<any>} items The list of items to filter
   * @param {{[field: string]: any}} conditions This dict of all conditions to match
   * @return {Array<any>} The filtered array
   */
  transform(items: Array<any>, conditions: { [field: string]: any }): Array<any> {
    if (items !== undefined) {
      return items.filter(item => {
        for (let field in conditions) {
          if (item[field] !== conditions[field]) {
            return false;
          }
        }
        return true;
      });
    }
  }

}
