package com.endless.budgeto;

import com.endless.bank.BankScraper.Bank;
import com.endless.bank.Tangerine;
import com.endless.bank.Transaction;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class TangerineTest {
    private String relativePath = "./src/test/java/com/endless/budgeto/TangerinePages/";

    @Test
    public void displayAccountSummary() throws Exception {
        // https://secure.tangerine.ca/web/Tangerine.html?command=displayAccountSummary&fill=1
        Document htmlDocument = Jsoup.parse(new File(relativePath + "AccountsSummary.html"), null);

        List<String> accountLinks = Tangerine.getAccountsLink(htmlDocument);
        assertTrue(accountLinks.size() == 3);
        for (String link : accountLinks) {
            assertTrue(link.contains("command=goTo"));
        }
    }

    @Test
    public void displayAccountDetails() throws Exception {
        // https://secure.tangerine.ca/web/Tangerine.html?command=displayAccountDetails&fill=1
        Document htmlDocument = Jsoup.parse(new File(relativePath + "ChequeAccount.html"), null);

        validateTransactions(Tangerine.getAccountTransactions(htmlDocument, Bank.Tangerine));
    }

    @Test
    public void displayCreditCardAccount() throws Exception {
        // https://secure.tangerine.ca/web/Tangerine.html?command=displayCreditCardAccount
        Document htmlDocument = Jsoup.parse(new File(relativePath + "CreditCardAccount.html"), null);

        validateTransactions(Tangerine.getCreditCardAccountTransactions(htmlDocument, Bank.Tangerine));
    }

    private void validateTransactions(List<Transaction> transactions) {
        assertTrue(transactions.size() > 0);
        for (Transaction transaction : transactions) {
            System.out.println(transaction.getDate()+", "+String.valueOf(transaction.getAmount())+"$ :  \t"+transaction.getDesc());
            assertTrue(transaction.getDate().matches("[0-9]+-[0-9]+-[0-9]+"));
        }
    }
}