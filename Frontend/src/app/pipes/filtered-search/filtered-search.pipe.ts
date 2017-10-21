import { Pipe, PipeTransform } from '@angular/core';
import { Transaction } from "app/classes/transaction.class";
import { Category } from "app/classes/category.interface";

@Pipe({ name: 'filtered-search', pure: true })
export class FilteredSearch implements PipeTransform {


  /** Filter a list of transactions by categories and text
   * @param {Transaction[]} transaction The list of transactions to filter
   * @param {string} searchValue The text to search
   * @param {Category[]} categories The categories to filter from
   * @return {Transaction[]} The filtered array
   */
  transform(transactions: Transaction[], searchValue: string, categories: Category[]): Transaction[] {
    searchValue = searchValue || '';
    return transactions.filter(transaction => {
      if (transaction.description.indexOf(searchValue) >= 0) {
        for (let category of categories) {
          if (category['selected'] && category.category_id == transaction.category_id) {
            return true;
          }
        }
      }
      return false;
    });
  }

}
