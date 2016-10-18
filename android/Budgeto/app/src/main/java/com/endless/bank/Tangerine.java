package com.endless.bank;

import android.app.Activity;
import android.webkit.WebView;

import com.endless.bank.BankResponse.ErrorFrom;
import com.endless.tools.Logger;
import com.endless.tools.Sanitizer.StringType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jodd.jerry.Jerry;

import static jodd.jerry.Jerry.jerry;

/**
 * Created by Eric on 2016-09-09.
 */
public class Tangerine extends BankScraper {

    private List<String> calls = Arrays.asList(
            "input = $('#ACN'); input.val('%s'); input.closest('form').submit();", // {username}
            "input = $('#Answer'); input.val('%s'); input.closest('form').submit();", // {answer}
            "input = $('#PIN'); input.val('%s'); input.closest('form').submit()", // {password}
            "https://secure.tangerine.ca/web/Tangerine.html?command=goToCreditCardAccount&creditCardAccount=0");

    public Tangerine(WebView webView) {
        super(webView);
        this.bank = Bank.Tangerine;
        this.loginUrl = "https://secure.tangerine.ca/web/InitialTangerine.html?command=displayLogin&device=web&locale=fr_CA";
        this.logoutUrl = "https://secure.tangerine.ca/web/InitialTangerine.html?command=displayLogout&device=web&locale=fr_CA";
    }

    @Override
    public void requestTransactions(BankCallable bankCallable, String usr, String pwd) {
        BankResponse response = null;
        if (!validateUsername(usr)) {
            response = new BankResponse(bank, ErrorFrom.username,
                    String.format("Veuillez vérifier votre nom d'utilisateur %s", String.valueOf(bank)));
        } else if (!validatePassword(pwd)) {
            response = new BankResponse(bank, ErrorFrom.password,
                    String.format("Votre NIP %s doit comporter 4 ou 6 chiffres.", String.valueOf(bank)));
        }

        if (response != null) {
            bankCallable.callBack(response);
        } else {
            // response = MainActivity.tempCreateJson();
            this.username = usr;
            this.password = pwd;
            this.bankCallable = bankCallable;
            webView.loadUrl(loginUrl);
        }
    }

    @Override
    protected boolean validateUsername(String username) {
        return validateString(username, 0, 50, StringType.anyChars);
    }

    @Override
    protected boolean validatePassword(String password) {
        return validateString(password, 4, 6, StringType.numbersOnly);
    }

    String referrer;
    @Override
    public void nextCall(String url, String response) {
        Logger.print(this.getClass(), url, "nextCall url");

        if (url == null && referrer != null)
            url = referrer;

        if (url.contains("displayLogin")) {
            // username
            sendJavascript(String.format(calls.get(0), username));

        } else if (url.contains("displayChallengeQuestion")) {
            if (response == null) {
                // fetching question text
                referrer = url;
                getDocumentHTML();
            } else if (response.startsWith("<head>")) {
                // prompt for answer
                String question = jerry(response).$("div.content-main-wrapper .CB_DoNotShow:first").html();
                promptInput(question); //response.replaceAll("^\"|\"$", ""));
            } else {
                // answering question
                referrer = null;
                sendJavascript(String.format(calls.get(1), response));
            }

        } else if (url.contains("displayPIN")) {
            // password
            sendJavascript(String.format(calls.get(2), password));

        } else if (url.contains("displayAccountSummary")) {
            // connected, getting to credit card
            webView.loadUrl(calls.get(3));

        } else if (url.contains("displayCreditCardAccount")) {
            if (response == null) {
                referrer = url;
                getDocumentHTML();
            } else if (response.startsWith("<head>")) {
                // extracting data
                Jerry doc = jerry(response);
                doc = doc.$("table[data-target='#transactionTable'] tbody");

                BankResponse bankResponse = new BankResponse(bank);
                List<Transaction> transactions = new ArrayList<>();
                for (Jerry tr : doc.$("tr")) {
                    String date = tr.$(".tr-date").html();
                    String desc = tr.$(".tr-desc").html();
                    String amount = tr.$(".tr-amount").html();
                    String cat = "-" + tr.$(".tr-icon i").attr("class");
                    cat = cat.substring(cat.lastIndexOf("-")+1);

                    bankResponse.addTransaction(date, desc, amount, cat);
                }

                bankCallable.callBack(bankResponse.finalizeResponse());

                referrer = null;
                ((Activity) webView.getContext()).runOnUiThread(new Runnable() {
                    @Override public void run() { webView.loadUrl(logoutUrl); }
                });
            }
        } else if (url.contains("displayLogout")) {
            Logger.print(this.getClass(), "Operations successfully completed for " + String.valueOf(bank));
        }
    }
}
