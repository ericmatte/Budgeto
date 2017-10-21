import { Pipe, PipeTransform } from '@angular/core';
import { FilterPipe } from "app/pipes/filter/filter.pipe";
import { SharedService } from "app/services/shared.service";

@Pipe({ name: 'getLimits', pure: true })
export class GetLimitsPipe implements PipeTransform {

  constructor(private filter: FilterPipe, private root: SharedService) { }

  /** 
   * @param {Array<any>}
   * @return {Array<any>}
   */
  transform(categories: Array<any>): Array<any> {
    for (let category of categories) {
      var limit = this.filter.transform(this.root.limits, { category_id: category['category_id'] })[0]
      if (limit != null) {
        Object.assign(category, { limit: limit['value'] })
      }
    }
    return categories;
  }

}
