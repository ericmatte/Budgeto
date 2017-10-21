import { Component, Input, Output, EventEmitter, OnChanges, SimpleChanges } from '@angular/core';
import { Transaction } from "app/classes/transaction.class";
import { Category } from "app/classes/category.interface";
declare var $: any;

@Component({
  selector: 'category-options',
  templateUrl: './category-options.component.html',
  styleUrls: ['./category-options.component.css']
})
export class CategoryOptionsComponent implements OnChanges {
  @Input() inputCategory: Category;

  category: any = {};
  setLimit: boolean;

  constructor() { }

  ngOnChanges(changes: SimpleChanges): void {
    this.category = {};
    Object.assign(this.category, this.inputCategory)
    this.setLimit = this.category['limit'] != undefined;
  }

  onSubmit() {
    $('#category-options').modal('close');
    console.log('Next step: Send to API')
    console.log(this.category)
    this.inputCategory = this.category;
  }

  onCancel() {
    this.ngOnChanges(undefined);
  }

}
