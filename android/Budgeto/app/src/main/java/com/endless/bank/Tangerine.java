package com.endless.bank;

import android.webkit.WebView;

import com.endless.tools.Logger;
import com.endless.tools.Sanitizer;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 2016-09-09.
 */
public class Tangerine extends BankScraper {

    private boolean userAsLoggedIn = false;
    private List<String> accountsLink;

    public Tangerine(WebView webView) {
        super(webView);
        this.bank = Bank.Tangerine;
        this.loginUrl = baseUrl + "/web/InitialTangerine.html?command=displayLogin&device=web&locale=en_CA";
        this.logoutUrl = baseUrl + "/web/InitialTangerine.html?command=displayLogout&device=web&locale=en_CA";
    }

    public static String baseUrl = "https://secure.tangerine.ca";

    /** Get all accounts link */
    public static List<String> getAccountsLink(Document documentHTML) {
        List<String> linksList = new ArrayList<>();

        Element parent = documentHTML.getElementsByClass("account-summary").first();
        Elements links = parent.getElementsByAttributeValueContaining("href", "command=goTo");
        for (Element link : links) {
            linksList.add(baseUrl + link.attr("href"));
        }

        return linksList;
    }

    /** Return a list of transactions from the account page */
    public static List<Transaction> getAccountTransactions(Document documentHTML, Bank bank) {
        List<Transaction> transactions = new ArrayList<>();

        Element table = documentHTML.getElementsByTag("table").first()
                .getElementsByAttributeValue("data-target", "#transactionTable").first();
        Elements rows = table.getElementsByTag("tbody").first().getElementsByTag("tr");
        for (Element tr : rows) {
            Elements td = tr.getElementsByTag("td");
            String date = td.get(0).text();
            String desc = td.get(1).text();
            String amount = td.get(3).text();
            String cat = (td.get(2).text()+" ").split(" ")[0];

            transactions.add(new Transaction(bank, date, desc, amount, cat));
        }

        return transactions;
    }

    /** Return a list of transactions from the credit card account page */
    public static List<Transaction> getCreditCardAccountTransactions(Document documentHTML, Bank bank) {
        List<Transaction> transactions = new ArrayList<>();

        Element table = documentHTML.getElementsByTag("table").first()
                .getElementsByAttributeValue("data-target", "#transactionTable").first();
        Elements rows = table.getElementsByTag("tbody").first().getElementsByTag("tr");
        for (Element tr : rows) {
            String date = tr.getElementsByClass("tr-date").first().text();
            String desc = tr.getElementsByClass("tr-desc").first().text();

            Float amount = Float.valueOf(Sanitizer.floatValue(tr.getElementsByClass("tr-amount").first().text()));
            amount = 0 - amount; // Inverse amount value for credit cards

            Element icon = tr.getElementsByClass("tr-icon").first().getElementsByTag("i").first();
            String cat = "-" + (icon != null ? icon.className() : "");
            cat = cat.substring(cat.lastIndexOf("-")+1);

            transactions.add(new Transaction(bank, date, desc, String.valueOf(amount), cat));
        }

        return transactions;
    }

    @Override
    public void nextCall(String url, Document documentHTML) {
        Logger.print(this.getClass(), url, "nextCall url");

        if (url.contains("command=displayAccountSummary")) {

            // Connected, bank summary
            if (documentHTML == null) {
                userAsLoggedIn = true;
                dialog.hide();
                getDocumentHTML(url);
            } else if (accountsLink.size() == 0) {

                accountsLink = getAccountsLink(documentHTML);
                nextLink();

            }

        } else if (url.contains("command=displayAccountDetails")) {

            if (documentHTML == null) {
                getDocumentHTML(url);
            } else {

                bankResponse.addTransactions(getAccountTransactions(documentHTML, bank));
                nextLink();

            }

        } else if (url.contains("command=displayCreditCardAccount")) {

            if (documentHTML == null) {
                getDocumentHTML(url);
            } else {

                bankResponse.addTransactions(getCreditCardAccountTransactions(documentHTML, bank));
                nextLink();

            }

        } else if (url.contains("displayLogout")) {

            Logger.print(this.getClass(), "Operations successfully completed for " + String.valueOf(bank));
            loadUrl("about:blank");

        } else {
//            if (!url.startsWith("https://secure.tangerine.ca/web/Tangerine.html") &&
//                    !url.startsWith("https://secure.tangerine.ca/web/InitialTangerine.html")) {
//                Logger.print(this.getClass(), url, "User tried to ge else where! Canceling...");
//
//                if (!url.equals("about:blank")) {
//                    loadUrl("about:blank");
//                    dialog.cancel();
//                }
//            }
        }



    }

    private void nextLink() {
        if (userAsLoggedIn) {
            // Load next bank account or logout if finished
            if (accountsLink != null && accountsLink.size() > 0) {
                loadUrl(accountsLink.remove(0));
            } else {
                bankCallable.callBack(bankResponse.finalizeResponse());
                loadUrl(logoutUrl);
            }
        }
    }
}
