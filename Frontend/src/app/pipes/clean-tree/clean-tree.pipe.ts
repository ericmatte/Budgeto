import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'cleanTree', pure: true })
export class CleanTreePipe implements PipeTransform {


erve
  transform(tree: Array<any>): Array<any> {
    for (let leaf of tree) {
        this.countChildItems(leaf);
    }
    return tree;
  }

  countChildItems(parentLeaf: any) {
    parentLeaf['count'] = (parentLeaf.items || []).length;

    for (let i=parentLeaf.children.length-1; i>=0; i--) {
      this.countChildItems(parentLeaf.children[i]);
      parentLeaf['count'] += parentLeaf.children[i]['count'];

      if (parentLeaf.children[i]['count'] == 0) {
        parentLeaf.children.splice(i, 1);
      }
    }

    return parentLeaf;
  }

}
