import { Component, OnInit, AfterViewInit, ElementRef } from '@angular/core';
import { SharedService } from "app/services/shared.service";
import { ApiService } from "app/services/api.service";
declare var $: any;

@Component({
  selector: 'app-budget',
  templateUrl: './budget.component.html',
  styleUrls: ['./budget.component.css']
})
export class BudgetComponent implements AfterViewInit {
  selectedCategory: any = null;

  constructor(private root: SharedService, private apiService: ApiService, private el: ElementRef) { }

  ngAfterViewInit() {
    this.getAllCategoriesDetails();
  }

  showCategoryOptions(category) {
    this.selectedCategory = category;
    $('#category-options').modal('open');
  }

  /* Get total amount of money for all category */
  getAllCategoriesDetails() {
    $(".cat").each(function () {
      var total = 0.0,
        parent = $(".collapsible-header", this);
      $(".amount", this).each(function () {
        total += parseFloat(this.textContent)
      });
      var limit = $("[name='limit']", parent).data('limit'),
        percent = Math.round((total / parseFloat(limit)) * 100),
        progressBar = $("[name='limit']", parent),
        tooltip = "";

      if (progressBar.length != 0) {

        if (percent < 100) {
          tooltip = 'tooltipped" materialize="tooltip" data-tooltip="' + (limit - total).toFixed(2) + '$ restant (' + percent + '% utilisé)" data-position="left" data-delay="50';
          progressBar.css('width', percent + '%');
        } else {
          tooltip = 'tooltipped" materialize="tooltip" data-tooltip="' + (total - limit).toFixed(2) + '$ de dépassé! (' + percent + '%)" data-position="left" data-delay="50';
          progressBar.css('width', '100%').addClass('red darken-4');
        }
      }

      $("[name='dollar-bill']", parent).html('<span class="new badge ' + tooltip + '" data-badge-caption="$">' + total.toFixed(2) + '</span>');
    });
    $('.tooltipped', this.el.nativeElement).tooltip();
  }

}
