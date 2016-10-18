package com.endless.bank;

import com.endless.bank.BankScraper.Bank;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is what a bank should return after extracting all transactions.
 *
 * @author  Eric Matte
 * @version 1.0
 */
public class BankResponse {

    public enum State { fetching, ok, error }
    public enum ErrorFrom { username, password, internal }

    private Bank bank;
    private List<Transaction> transactions;
    private String message;
    private State state;
    private ErrorFrom errorFrom;

    /** Create a bank response and wait for transactions */
    public BankResponse(Bank bank) {
        this.bank = bank;
        this.transactions = new ArrayList<>();
        this.state = State.fetching;
    }

    /** Generate an error response */
    public BankResponse(Bank bank, ErrorFrom errorFrom, String message) {
        this.bank = bank;
        this.errorFrom = errorFrom;
        this.message = message;
        this.state = State.error;
    }

    /** Finalize fetching transaction */
    public BankResponse finalizeResponse() {
        if (state == State.fetching) {
            if (transactions.size() == 0) {
                message = "No transactions found!";
                state = State.error;
            } else {
                state = State.ok;
            }
        }
        return this;
    }

    public boolean addTransaction(String date, String desc, String amount) {
        return addTransaction(date, desc, amount, null);
    }

    /** Add a transaction only if the is not fetching else return false */
    public boolean addTransaction(String date, String desc, String amount, String cat) {
        return (state == State.fetching) && transactions.add(new Transaction(bank, date, desc, amount, cat));
    }

    public Bank getBank() { return bank; }
    public List<Transaction> getTransactions() { return transactions; }
    public String getMessage() { return message; }
    public State getState() { return state; }
    public ErrorFrom getErrorFrom() { return errorFrom; }

}
