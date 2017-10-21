package com.endless.budgeto;

import com.endless.bank.BankScraper.Bank;
import com.endless.bank.Tangerine;
import com.endless.bank.Transaction;
import com.endless.tools.CallAPI;
import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class APITest {
    private String relativePath = "./src/test/java/com/endless/budgeto/TangerinePages/";

    @Test
    public void sendTransactions() throws Exception {
        // https://secure.tangerine.ca/web/Tangerine.html?command=displayAccountDetails&fill=1
        Document htmlChequeDocument = Jsoup.parse(new File(relativePath + "ChequeAccount.html"), null);
        Document htmlCreditDocument = Jsoup.parse(new File(relativePath + "CreditCardAccount.html"), null);

        List<Transaction> transactions = Tangerine.getAccountTransactions(htmlChequeDocument, Bank.Tangerine);
        transactions.addAll(Tangerine.getCreditCardAccountTransactions(htmlCreditDocument, Bank.Tangerine));

        sendTransactionsToAPI(transactions);
        assertTrue(transactions.size() > 0);
    }

    private void sendTransactionsToAPI(List<Transaction> transactions) {
        // http-request: https://github.com/kevinsawicki/http-request
        String apiUrl = "http://127.0.0.1:5000/budgeto/fetch-transactions"; // "http://endlessapi.ddns.net:8080/budgeto/fetch-transactions"

        JSONArray trans = new JSONArray();
        for (Transaction transaction: transactions) {
            trans.put(transaction.getJSONObject());
        }

        HashMap<String, String> data = new HashMap<>();
        data.put("email", "ericmatte.inbox@gmail.com");
        data.put("transactions", trans.toString());

        HttpRequest httpRequest = HttpRequest.post(apiUrl).form(data);
        // assertTrue(httpRequest.ok());
        String a = httpRequest.body();
        assertTrue(a.length() != 0);
    }
}