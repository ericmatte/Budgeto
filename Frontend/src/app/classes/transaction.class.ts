import { User } from "app/classes/user.interface";

export class Transaction {
    transaction_id: number;
    user_id: number;
    bank_id: number;
    category_id: number;
    description: string;
    amount: number;
    date: string;
    upload_time: Date;

    isSelected: boolean;
    isLoading: boolean;

    constructor(jsonData: any, user?: User, bankId?: number){
        // Default values
        this.transaction_id = null;
        this.bank_id = 1;
        this.category_id = 2;
        this.description = null;
        this.amount = null;
        this.date = null;
        this.upload_time = null;
        
        this.isSelected = false;
        this.isLoading = false;

        // Values assignment
        Object.assign(this, jsonData || {});
        this.user_id = bankId || this.bank_id;
        this.user_id = user.user_id || this.user_id;
    }
}