package com.endless.bank;

import android.content.Context;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import com.endless.bank.BankScraper;
import static jodd.jerry.Jerry.jerry;

import java.util.List;
import java.util.Map;

/**
 * Created by Eric on 2016-09-09.
 */
public class Tangerine extends BankScraper {

    int step;

    public Tangerine(WebView webView, Map<String, String> userInfo) {
        super(webView, userInfo);
        this.bankName = "Tangerine";
    }

    @Override
    public void login() {
        webView.loadUrl(BankData.url);
    }

    @Override
    public void logout() {
        webView.loadUrl("logout"); // TODO: Get logout url
    }

    @Override
    public void requestTransactions() {
        step = 0;
        login();
        // nextCall() will be triggered by page loaded
    }

    @Override
    public void nextCall(String url, String response) {
        if (!url.equals(null) && !url.contains("?"))
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
                promptInput(response);
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
                Jerry doc = jerry(html);
                doc.$("div#jodd p.neat").css("color", "red").addClass("ohmy");
                break;
            default:
                // last step
                logout();

                break;
        }
        step += 1;
    }
}
