import { Pipe, PipeTransform } from '@angular/core';
import { FilterPipe } from "app/pipes/filter/filter.pipe";

@Pipe({ name: 'groupBy', pure: true })
export class GroupByPipe implements PipeTransform {

  constructor(private filter: FilterPipe) { }

  /** Group firstGroup into a sub-array 'items' of secondGroup
   * @param {Array<any>} firstGroup The list of items to filter
   * @param {Array<any>} secondGroup The array to split
   * @param {string} on The key to for grouping firstGroup
   * @param {string} and The key to for grouping secondGroup. If null, will using the same as 'on'.
   * @param {boolean} keepEmpty If true, will keep all elements.
   * @return {Array<any>} The firstGroup with secondGroup as a grouped sub-array 'items'
   */
  transform(firstGroup: Array<any>, secondGroup: Array<any>, on: string, and?: string, keepEmpty?: boolean): Array<any> {
    if (and == null) { and = on; }
    if (keepEmpty == null) { keepEmpty = false; }

    var output = [];
    if (secondGroup !== undefined) {
      if (firstGroup !== undefined) {
        for (let item of secondGroup) {
          // Finding matching items
          var conditions = {};
          conditions[and] = item[on];
          var items = this.filter.transform(firstGroup, conditions);
          // Cloning object if there is some matching items
          if (items.length != 0 || keepEmpty) {
            var obj = Object.assign({}, item);
            obj['items'] = items;
            output.push(obj);
          }
        }
      }
      return output;
    }
  }

}
