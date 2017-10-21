import { Component, Input, OnInit } from '@angular/core';
import { Transaction } from "app/classes/transaction.class";

@Component({
  selector: 'category-transaction',
  templateUrl: './category-transaction.component.html',
  styleUrls: ['../budget.component.css']
})
export class CategoryTransactionComponent {
  @Input() transactions: Transaction[];
  @Input() banks: any[];

  constructor() { }
}
