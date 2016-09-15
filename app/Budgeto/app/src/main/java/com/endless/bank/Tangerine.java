package com.endless.bank;

import android.content.Context;
import android.webkit.WebView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

    public Tangerine(WebView webView, Context context, Map<String, String> userInfo) {
        super(webView, context, userInfo);
        this.bankName = "Tangerine";
    }

    @Override
    public void login() {
        webView.loadUrl("https://secure.tangerine.ca/web/InitialTangerine.html?command=displayLogin&device=web&locale=fr_CA");
    }

    @Override
    public void logout() {
        webView.loadUrl("https://secure.tangerine.ca/web/InitialTangerine.html?command=displayLogout&device=web&locale=fr_CA");
    }

    @Override
    public void requestTransactions() {
        login();
    }

    String referrer;
    @Override
    public void nextCall(String url, String response) {
        if (url == null && referrer != null)
            url = referrer;

        if (url.contains("displayLogin")) {
            // username
            sendJavascript(String.format(calls.get(0), userInfo.get("username")));
        }
        else if (url.contains("displayChallengeQuestion")) {
            if (response == null) {
                // fetching question text
                referrer = url;
                getWebViewHTML();
            } else if (response.startsWith("<head>")) {
                // prompt for answer
                String question = jerry(response).$("div.content-main-wrapper .CB_DoNotShow:first").html();
                promptInput(question); //response.replaceAll("^\"|\"$", ""));
            } else {
                // answering question
                referrer = null;
                sendJavascript(String.format(calls.get(1), response));
            }
        }
        else if (url.contains("displayPIN")) {
            // password
            sendJavascript(String.format(calls.get(2), userInfo.get("password")));
        }
        else if (url.contains("displayAccountSummary")) {
            // connected, getting to credit card
            webView.loadUrl(calls.get(3));
        }
        else if (url.contains("displayCreditCardAccount")) {
            if (response == null) {
                referrer = url;
                getWebViewHTML();
            } else if (response.startsWith("<head>")) {
                // extracting data
                Jerry doc = jerry(response);
                doc = doc.$("table[data-target='#transactionTable'] tbody");
                try {
                    bankResponse.put("bank", bankName);

                    JSONArray transactions = new JSONArray();
                    for (Jerry tr : doc.$("tr")) {
                        Transaction trans = new Transaction();
                        trans.setDate(tr.$(".tr-date").html());
                        trans.setDesc(tr.$(".tr-desc").html());
                        String cat = "-" + tr.$(".tr-icon i").attr("class");
                        trans.setCat(cat.substring(cat.lastIndexOf("-")+1));
                        trans.setAmount(tr.$(".tr-amount").html());
                        transactions.put(trans.getJSONObject());
                    }
                    bankResponse.put("transactions", transactions);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                referrer = null;
                logout(); // TODO: logout not working
            }
        }
    }
}
