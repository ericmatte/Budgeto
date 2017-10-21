import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'groupByKey', pure: true })
export class GroupByKeyPipe implements PipeTransform {

  /** Convert a list to an object from the given key
   * @param {Array<any>} items The item list
   * @param {string} key What to use for the conversion
   * @return {Array<any>} The resulting object
   */
  transform(items: Array<any>, key: string): Object {
    if (items !== undefined) {
      var output = {};
      for (let item of items) {
        if (item[key] != null) {
          output[item[key]] = item;
        }
      }
      return output;
    }
  }
}

