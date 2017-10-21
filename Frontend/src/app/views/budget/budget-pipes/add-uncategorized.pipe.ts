import { Pipe, PipeTransform } from '@angular/core';
import { FilterPipe } from "app/pipes/filter/filter.pipe";
import { SharedService } from "app/services/shared.service";
import { WithNullKeyPipe } from "app/pipes/with-null-key/with-null-key.pipe";

@Pipe({ name: 'addUncategorized', pure: true })
export class AddUncategorizedPipe implements PipeTransform {

  constructor(private withNullKey: WithNullKeyPipe, private root: SharedService) { }

  /** 
   * @param {Array<any>}
   * @return {Array<any>}
   */
  transform(categoriesTree: Array<any>): Array<any> {
    var uncategorizedTransactions = this.withNullKey.transform(this.root.transactions, 'category_id');
    var otherCategory = {
        name: 'Non catégorisé',
        count: uncategorizedTransactions.length,
        items: uncategorizedTransactions
    }
    categoriesTree.push(otherCategory);
    return categoriesTree;
  }
}
