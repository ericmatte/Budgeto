import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Transaction } from "app/classes/transaction.class";
import { Category } from "app/classes/category.interface";

@Component({
  selector: 'budget-category',
  templateUrl: './category.component.html',
  styleUrls: ['../budget.component.css']
})
export class CategoryComponent {
  @Input() categories: Category[];
  @Input() transactions: Transaction[];
  @Input() banks: any[];

  @Output() onModalCall = new EventEmitter<any>();
  showCategoryOptions(category) {
    this.onModalCall.emit(category);
  }
  

  constructor() { }
}
