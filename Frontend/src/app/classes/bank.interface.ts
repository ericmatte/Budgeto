import { Transaction } from "app/classes/transaction.class";

export interface Bank {
    bank_id: number,
    name: string,
    color: string,

    transactions: Transaction[]
}