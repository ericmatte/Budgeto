import { Component, AfterViewInit, AfterViewChecked, EventEmitter } from '@angular/core';
import { MaterializeAction } from "angular2-materialize";
import { Router } from '@angular/router';
import { SharedService } from '../../services/shared.service';
import { Transaction } from "app/classes/transaction.class";
import { ApiService } from "app/services/api.service";
import { ListService } from "app/services/list.service";
declare var $: any;
declare var Materialize: any;

@Component({
  selector: 'transaction-modal',
  templateUrl: './transaction-modal.component.html',
  styleUrls: ['./transaction-modal.component.css']
})
export class TransactionModalComponent implements AfterViewChecked {
  type: string; // add, edit, delete
  inputTransaction: Transaction;
  inputBankId: number;

  transaction: Transaction;  

  transactionModal = new EventEmitter<string | MaterializeAction>();
  deleteTransactionModal = new EventEmitter<string | MaterializeAction>();
  private _(args: string): any { return { action: "modal", params: [args] } }

  constructor(private root: SharedService, private List: ListService, private apiService: ApiService) { }

  ngAfterViewChecked(): void { Materialize.updateTextFields() }

  showAddTransactionModal() {
    this.openModal('add', null, 1);
  }

  public openModal(type: string, transaction?: Transaction, bankId?: number) {
    this.inputTransaction = transaction;
    this.type = type;
    if (type == 'delete') {
      this.deleteTransactionModal.emit(this._('open'));
    } else {
      this.transaction = new Transaction(transaction, this.root.user, bankId);
      this.transactionModal.emit(this._('open'));
    }
  }

  private onSubmit() {
    this.transactionModal.emit(this._('close'));

    if (this.transaction.transaction_id == null) {
      this.addTransaction();
    } else {
      this.editTransaction();
    }
  }

  private onCancel() {
    this.transactionModal.emit(this._('close'));
  }

  private addTransaction() {
    this.apiService.addTransaction(this.transaction).subscribe(
      (transaction: Transaction) => {
        this.root.transactions = this.List.add(this.root.transactions, transaction);
      },
      err => Materialize.toast(err, 4000));
  }

  private editTransaction() {
    this.inputTransaction.isLoading = true;
    this.apiService.editTransaction(this.transaction).subscribe(
      (transaction: Transaction) => {
        this.root.transactions = this.List.replace(this.root.transactions, this.inputTransaction, transaction);
      },
      err => Materialize.toast(err, 4000));
  }

  private deleteTransaction() {
    this.deleteTransactionModal.emit(this._('close'));

    this.apiService.deleteTransaction([this.inputTransaction]).subscribe(
      (transaction: Transaction) => {
        this.root.transactions = this.List.remove(this.root.transactions, this.inputTransaction);
      },
      err => Materialize.toast("Une erreur s'est produite pendant la suppression de la transaction. Veuillez réessayer plus tard.", 4000));
  }
}
