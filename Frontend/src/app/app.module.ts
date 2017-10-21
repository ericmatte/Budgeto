import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { Uploader } from 'angular2-http-file-upload';
import { FormsModule } from '@angular/forms';
import { HttpModule, JsonpModule } from '@angular/http';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './views/login/auth.guard';
import { Angular2SocialLoginModule } from 'angular2-social-login';
import { SharedService } from './services/shared.service';
import { ApiService } from './services/api.service';
import { MaterializeModule } from "angular2-materialize";
import { ListService } from "app/services/list.service";

// Views
import { AppComponent } from './app.component';
import { BudgetComponent } from './views/budget/budget.component';
import { TransactionsComponent } from './views/transactions/transactions.component';
import { PromotionComponent } from './views/promotion/promotion.component';
import { LoginComponent } from './views/login/login.component';
import { TokenVerifierComponent } from './views/login/token-verifier.component';
import { PageNotFoundComponent } from './views/page-not-found/page-not-found.component';

// Components
import { MenuItemComponent } from './components/menu/menu-item.component';
import { MenuComponent } from './components/menu/menu.component';
import { TopBarComponent } from './components/topbar/topbar.component';
import { MainContainerComponent } from './components/main-container/main-container.component';
import { CategoryTransactionComponent } from './views/budget/category-transaction/category-transaction.component';
import { CategoryComponent } from './views/budget/category/category.component';
import { TransactionsTableComponent } from './views/transactions/transactions-table/transactions-table.component';
import { CategoryOptionsComponent } from "./views/budget/category-options/category-options.component";
import { TransactionModalComponent } from "app/components/transaction-modal/transaction-modal.component";
import { UploadTransactionsComponent } from './views/upload-transactions/upload-transactions.component';
import { SpinnerComponent } from "app/components/main-container/spinner";

// Directives
import { AdminDirective } from './directives/admin.directive';

// Pipes
import { FilterPipe } from './pipes/filter/filter.pipe';
import { WithNullKeyPipe } from './pipes/with-null-key/with-null-key.pipe';
import { GroupByPipe } from './pipes/group-by/group-by.pipe';
import { GroupByKeyPipe } from './pipes/group-by-key/group-by-key.pipe';
import { TreePipe } from './pipes/tree/tree.pipe';
import { CleanTreePipe } from './pipes/clean-tree/clean-tree.pipe';
import { AddUncategorizedPipe } from "app/views/budget/budget-pipes/add-uncategorized.pipe";
import { HasSelectedElementsPipe } from "app/views/transactions/transaction-pipes/has-selected-elements.pipe";
import { GetLimitsPipe } from "app/views/budget/budget-pipes/get-limits.pipe";

const appRoutes: Routes = [
  { path: '', component: PromotionComponent /* redirectTo: '/promotion', pathMatch: 'full' */ },
  { path: 'guard', component: TokenVerifierComponent },
  { path: 'login', component: LoginComponent },
  { path: 'budget', data: { title: 'Budget' }, component: BudgetComponent, canActivate: [AuthGuard] },
  { path: 'transactions', data: { title: 'Transactions' }, component: TransactionsComponent, canActivate: [AuthGuard] },
  { path: 'upload-transactions', data: { title: 'Importer des transactions' }, component: UploadTransactionsComponent, canActivate: [AuthGuard] },
  { path: '**', component: PageNotFoundComponent }
];

const providers = {
  'google': {
    'clientId': '567386675874-5fqmolj2dt7cu9g2dam7cheq7k923p8q.apps.googleusercontent.com'
  }
};

@NgModule({
  declarations: [
    AppComponent,
    BudgetComponent,
    CategoryComponent,
    CategoryTransactionComponent,
    TransactionsComponent,
    PromotionComponent,
    LoginComponent,
    TokenVerifierComponent,
    AdminDirective,
    MenuItemComponent,
    MenuComponent,
    TopBarComponent,
    MainContainerComponent,
    PageNotFoundComponent,
    FilterPipe,
    WithNullKeyPipe,
    GroupByPipe,
    TransactionsTableComponent,
    GroupByKeyPipe,
    TreePipe,
    CleanTreePipe,
    AddUncategorizedPipe,
    GetLimitsPipe,
    UploadTransactionsComponent,
    CategoryOptionsComponent,
    TransactionModalComponent,
    HasSelectedElementsPipe,
    SpinnerComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    MaterializeModule,
    Angular2SocialLoginModule,
    HttpModule,
    JsonpModule,
    RouterModule.forRoot(appRoutes)
  ],
  providers: [
    AuthGuard,
    Uploader,
    SharedService,
    ListService,
    ApiService,
    FilterPipe,
    WithNullKeyPipe
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

Angular2SocialLoginModule.loadProvidersScripts(providers);