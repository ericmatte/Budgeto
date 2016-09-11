package com.endless.bank;

import android.content.Context;
import android.webkit.WebView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Map;

/**
 * Created by Eric on 2016-09-09.
 */
public class Tangerine extends BankScraper {

    int step;

    public Tangerine(WebView webView, Context context, Map<String, String> userInfo) {
        super(webView, context, userInfo);
        this.bankName = "Tangerine";
    }

    @Override
    public void login() {
        webView.loadUrl(BankData.tangerineLogin);
    }

    @Override
    public void logout() {
        webView.loadUrl(BankData.tangerineLogout);
    }

    @Override
    public void requestTransactions() {
        step = 0;
        login();
        // nextCall() will be triggered by page loaded
    }

    @Override
    public void nextCall(String url, String response) {
        if (url != null && !url.contains("?"))
            return;

        switch (step) {
            case 0:
                // username
                sendJsData(String.format(BankData.tangerineCalls.get(0), userInfo.get("username")));
                break;
            case 1:
                // question
                requestJsData(BankData.tangerineCalls.get(1));
                break;
            case 2:
                // prompt for answer
                promptInput(response.replaceAll("^\"|\"$", ""));
                break;
            case 3:
                // answering question
                sendJsData(String.format(BankData.tangerineCalls.get(2), response));
                break;
            case 4:
                // password
                sendJsData(String.format(BankData.tangerineCalls.get(3), userInfo.get("password")));
                break;
            case 5:
                // connected, getting to credit card
                webView.loadUrl(BankData.tangerineCalls.get(4));
                break;
            case 6:
                // fetching transactions
                requestJsData(BankData.tangerineCalls.get(5));
                break;
            case 7:
                // extracting data
                Document doc = Jsoup.parse(response);
                break;
            default:
                // last step
                logout();

                break;
        }
        step += 1;
    }
}
