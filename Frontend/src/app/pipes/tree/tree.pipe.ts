import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'tree', pure: true })
export class TreePipe implements PipeTransform {


  /** Built a tree from a list of object identified by a key
   * @param {Array<any>} items The list of items from which the tree will be built
   * @param {string} objectKey The key that represents an object
   * @param {string} parentKey The key that represents the parent of an object
   * @return {Array<any>} The tree
   */
  transform(items: Array<any>, objectKey: string, parentKey: string): Array<any> {
    var output = [];
    for (let item of items) {
      if (item[parentKey] == undefined) {
        var parent = this.mapChildItems(items, item, objectKey, parentKey);
        output.push(parent);
      }
    }
    return output;
  }

  mapChildItems(items: Array<any>, parentItem: any, objectKey: string, parentKey: string): any {
    var parent = Object.assign({}, parentItem)
    Object.assign(parent, { children: [] });

    for (let item of items) {
      if (item[parentKey] == parent[objectKey]) {
        var child = this.mapChildItems(items, item, objectKey, parentKey);
        parent['children'].push(child)
      }
    }

    return parent;
  }

}
