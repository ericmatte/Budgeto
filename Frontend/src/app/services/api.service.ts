import { Injectable } from '@angular/core';
import { Http, Response, Headers, RequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { environment } from '../../environments/environment';
import { SharedService } from '../services/shared.service';
import { User } from "app/classes/user.interface";
import { Router, ActivatedRoute } from '@angular/router';
import { Transaction } from "app/classes/transaction.class";

@Injectable()
export class ApiService {
  api: string = environment.api;

  constructor(private http: Http, private root: SharedService, private router: Router, private route: ActivatedRoute) { }

  /** Handle http error and redirect if it is because the user is not connected
   * @param {any} error The response error
   * @return {Observable} The observable for the caller
   */
  private errorHandler(error: any) {
    if (error.status == 401) {
      var returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/budget';
      this.router.navigate(['/login'], { queryParams: { returnUrl: returnUrl } });
    } else {
      return Observable.throw(error._body || 'Server error');
    }
  }

  /** Generate the authorization bearer for an api call */
  public getAuthorizationBearer() {
    return { "Authorization": "Bearer " + this.root.user.token };
  }

  /** Generate the valid headers with token for json content type
   * @param {string} token (optional) A token to validate
   * @param {string} noToken (optional) If true will not send a Bearer in the header
   */
  private getHeaders(token?: String, noToken?: boolean) {
    let headers: Headers = new Headers();
    if (!noToken) { headers.append("Authorization", "Bearer " + (token || this.root.user.token)); }
    headers.append("Content-Type", "application/json");
    return { headers: headers };
  }

  /** Verify that the given token is still valid
   * @param {string} token The token to validate
   */
  validateToken(token: string): Observable<User> {
    let googleToken = token;
    return this.http.get(this.api + 'validate-token', this.getHeaders(token))
      .map((res: Response) => {
        var data = res.json();
        this.root.setUser(data['user'], googleToken);
        this.root.transactions = data['transactions'];
        this.root.limits = data['limits'];
        return true;
      })
      .catch((error: any) => this.errorHandler(error));
  }

  /** Fetch app data from API */
  fetchInitialData(): Observable<User> {
    return this.http.get(this.api + 'fetch-initial-data', this.getHeaders(undefined, true))
      .map((res: Response) => {
        var data = res.json();
        this.root.banks = data.banks;
        this.root.categories = data.categories;
      })
      .catch((error: any) => {
        console.log(error);
        return Observable.throw(error.json().error || 'Server error');
      });
  }

  /** Fetch all existing transactions of the user with specified parametters */
  getTransactions(): Observable<Transaction[]> {
    return this.http.get(this.api + 'transactions', this.getHeaders())
      .map((res: Response) => res.json())
      .catch((error: any) => this.errorHandler(error));
  }

  /** Add a transaction to database
   * @param {string} transaction The transaction to add
   */
  addTransaction(transaction: Transaction): Observable<Transaction> {
    return this.http.put(this.api + 'transactions', JSON.stringify(transaction), this.getHeaders())
      .map((res: Response) => res.json())
      .catch((error: any) => this.errorHandler(error));
  }

  /** Edit a transaction in the database
   * @param {string} transaction The transaction to edit
   */
  editTransaction(transaction: Transaction): Observable<Transaction> {
    return this.http.post(this.api + 'transactions', JSON.stringify(transaction), this.getHeaders())
      .map((res: Response) => res.json())
      .catch((error: any) => this.errorHandler(error));
  }

  /** Remove a transaction from database
   * @param {string} transactions The list of transaction to delete
   */
  deleteTransaction(transactions: Transaction[]): Observable<Transaction> {
    // Extracting ids
    var ids = [];
    for (let t of transactions) { ids.push(t.transaction_id) } 
    // Sending request
    return this.http.delete(this.api + `transactions/delete/${ids.join()}`, this.getHeaders())
      .map((res: Response) => res.json())
      .catch((error: any) => this.errorHandler(error));
  }

  //  // Add a new comment
  // addComment (body: Object): Observable<Comment[]> {
  //     let bodyString = JSON.stringify(body); // Stringify payload
  //     let headers = new Headers({ 'Content-Type': 'application/json' }); // ... Set content type to JSON
  //     let options = new RequestOptions({ headers: headers }); // Create a request option

  //     return this.http.post(this.commentsUrl, body, options) // ...using post request
  //                      .map((res:Response) => res.json()) // ...and calling .json() on the response to return data
  //                      .catch((error:any) => Observable.throw(error.json().error || 'Server error')); //...errors if any
  // }   

  // // Update a comment
  // updateComment (body: Object): Observable<Comment[]> {
  //     let bodyString = JSON.stringify(body); // Stringify payload
  //     let headers = new Headers({ 'Content-Type': 'application/json' }); // ... Set content type to JSON
  //     let options = new RequestOptions({ headers: headers }); // Create a request option

  //     return this.http.put(`${this.commentsUrl}/${body['id']}/`, body, options) // ...using put request
  //                      .map((res:Response) => res.json()) // ...and calling .json() on the response to return data
  //                      .catch((error:any) => Observable.throw(error.json().error || 'Server error')); //...errors if any
  // }   
  // // Delete a comment
  // removeComment (id:string): Observable<Comment[]> {
  //     return this.http.delete(`${this.commentsUrl}/${id}`) // ...using put request
  //                      .map((res:Response) => res.json()) // ...and calling .json() on the response to return data
  //                      .catch((error:any) => Observable.throw(error.json().error || 'Server error')); //...errors if any
  // }   

}
