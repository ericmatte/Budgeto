import { Transaction } from "app/classes/transaction.class";

export interface Category {
    category_id: number,
    parent_id: number,
    name: string,
    icon: string,
    update_time: string,

    children: Category[],
    transactions: Transaction[]
}