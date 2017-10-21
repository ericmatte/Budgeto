import { Component, Input, ElementRef, EventEmitter, Output } from '@angular/core';
import { Transaction } from "app/classes/transaction.class";
import { Category } from "app/classes/category.interface";
import { SharedService } from "app/services/shared.service";

@Component({
  selector: 'transactions-table',
  templateUrl: './transactions-table.component.html',
  styleUrls: ['../transactions.component.css']
})
export class TransactionsTableComponent {
  @Input() categories: Category[];
  @Input() transactions: Transaction[];

  @Output() onModalCall = new EventEmitter<any>();
  
  allSelected:boolean = false;

  constructor(private el: ElementRef, private root: SharedService) { }

  editTransaction(transaction) {
    this.onModalCall.emit({transaction: transaction, modalType: 'edit'});
  }

  deleteTransaction(transaction: Transaction) {
    this.onModalCall.emit({transaction: transaction, modalType: 'delete'});
  }

  selectAll() {
    if (this.transactions.length > 0) {
      this.allSelected = !this.allSelected;
      for (let transaction of this.transactions) {
        transaction.isSelected = this.allSelected;
      }
    }
  }

  selectTransaction(transaction: Transaction) {
    transaction.isSelected = !(transaction.isSelected);
  }

}
