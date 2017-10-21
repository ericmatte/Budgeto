package com.endless.bank;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent a list of transactions
 *
 * @author  Eric Matte
 * @version 1.0
 */
public class Category {
    private String name;
    private List<Transaction> associatedTransactions;

    public Category(String name, List<Transaction> transactions) {
        this.name = name;
        this.associatedTransactions = new ArrayList<>();
        this.associatedTransactions.addAll(transactions);
    }

    public void linkTransaction(Transaction transaction) {
        associatedTransactions.add(transaction);
    }

    public String getName() { return name; }
    public List<Transaction> getAssociatedTransactions() { return associatedTransactions; }
}
