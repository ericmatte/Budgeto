import { Component, ViewChild, AfterViewInit } from '@angular/core';
import { ApiService } from "app/services/api.service";
import { SharedService } from "app/services/shared.service";
import { Transaction } from "app/classes/transaction.class";
import { Bank } from "app/classes/bank.interface";
import { TransactionModalComponent } from "app/components/transaction-modal/transaction-modal.component";
declare var $: any;
declare var Materialize: any;

@Component({
  selector: 'app-transactions',
  templateUrl: './transactions.component.html',
  styleUrls: ['./transactions.component.css']
})
export class TransactionsComponent {

  @ViewChild(TransactionModalComponent)
  private transactionModal: TransactionModalComponent;

  constructor(private apiService: ApiService, private root: SharedService) { }

  editTransaction($event) {
    if ($event.modalType == 'delete') {
      this.transactionModal.openModal('delete', $event.transaction);
    } else {
      this.transactionModal.openModal('edit', $event.transaction);
    }
  }

  addTransaction(bankId: number) {
    this.transactionModal.openModal('add', null, bankId);
  }

  deleteSelectedTransactions() {
    $('#transaction-delete-selected-modal').modal('close');
    // Selecting transactions to delete
    var selectedTransactions = this.root.transactions.filter(transaction => transaction.isSelected == true);

    // Removing transactions to delete from list
    this.root.transactions = this.root.transactions.filter(transaction => transaction.isSelected != true);

    this.apiService.deleteTransaction(selectedTransactions).subscribe(
      (transaction: Transaction) => {},
      err => Materialize.toast("Une erreur s'est produite pendant la suppression des transactions. Veuillez réessayer plus tard.", 4000));
  }
}
