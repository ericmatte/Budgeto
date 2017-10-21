import { Component, OnInit, AfterViewInit } from '@angular/core';
import { Bank } from "app/classes/bank.interface";
import { SharedService } from "app/services/shared.service";
import { Uploader } from 'angular2-http-file-upload';
import { MyUploadItem } from './upload-item';
import { ApiService } from "app/services/api.service";
declare var $: any;
declare var Materialize: any;

@Component({
  selector: 'app-upload-transactions',
  templateUrl: './upload-transactions.component.html',
  styleUrls: ['./upload-transactions.component.css']
})
export class UploadTransactionsComponent implements AfterViewInit {
  supportedBanks: Bank[] = [];
  selectedBank: Bank;
  uploadInProgress: boolean = false;

  constructor(private root: SharedService, public uploaderService: Uploader, private apiService: ApiService) {
    var supportedBanksId: number[] = [1, 2];
    for (let bank of root.banks) {
      if (supportedBanksId.indexOf(bank.bank_id) > -1) {
        this.supportedBanks.push(bank);
      }
    }
    this.selectedBank = this.supportedBanks[0];
  }


  ngAfterViewInit(): void {
    $('.materialboxed').materialbox();
  }

  submit() {
    let uploadFile = (<HTMLInputElement>window.document.getElementById('file')).files[0];

    if (uploadFile == undefined) {
      Materialize.toast('Vous devez d\'abord sélectionner un fichier', 4000);
      return false;
    }

    this.uploadInProgress = true;
    let myUploadItem = new MyUploadItem(uploadFile, this.apiService);
    myUploadItem.formData = { bank_id: this.selectedBank.bank_id };  // (optional) form data can be sent with file

    this.uploaderService.onSuccessUpload = (item, response, status, headers) => {
      // success callback
      if (status == 200) {
        this.root.transactions = response.transactions;
        Materialize.toast(`<i class="material-icons">done</i> ${response.number} transactions ${this.selectedBank.name} téléchargé!`, 5000);
      } else if (status == 204) {
        Materialize.toast(`Le fichié a bien été reçu, mais aucune transaction ${this.selectedBank.name} n\'a été trouvé.`, 5000);
      }
    };
    this.uploaderService.onErrorUpload = (item, response, status, headers) => {
      // error callback
      Materialize.toast(response, 4000);
    };

    this.uploaderService.onCompleteUpload = (item, response, status, headers) => {
      this.uploadInProgress = false;
    };
    this.uploaderService.upload(myUploadItem);
  }

}
