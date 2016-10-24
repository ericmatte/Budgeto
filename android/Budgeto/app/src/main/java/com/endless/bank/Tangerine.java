package com.endless.bank;

import android.app.Activity;
import android.webkit.WebView;

import com.endless.tools.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jodd.jerry.Jerry;

import static jodd.jerry.Jerry.jerry;

/**
 * Created by Eric on 2016-09-09.
 */
public class Tangerine extends BankScraper {

    private List<String> calls = Arrays.asList("https://secure.tangerine.ca/web/Tangerine.html?command=goToCreditCardAccount&creditCardAccount=0");

    public Tangerine(WebView webView) {
        super(webView);
        this.bank = Bank.Tangerine;
        this.loginUrl = "https://secure.tangerine.ca/web/InitialTangerine.html?command=displayLogin&device=web&locale=fr_CA";
        this.logoutUrl = "https://secure.tangerine.ca/web/InitialTangerine.html?command=displayLogout&device=web&locale=fr_CA";
    }

    String referrer;
    @Override
    public void nextCall(String url, String response) {
        Logger.print(this.getClass(), url, "nextCall url");

        if (url == null && referrer != null)
            url = referrer;

        // TODO: Cancel bank request if the user go else where

        if (url.contains("displayAccountSummary")) {
            // connected, getting to credit card
            dialog.hide();
            //dialog.dismiss();
            webView.loadUrl(calls.get(0));

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
