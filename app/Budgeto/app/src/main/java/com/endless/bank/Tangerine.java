package com.endless.bank;

import android.content.Context;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import com.endless.bank.BankScraper;

import java.util.List;
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
        webView.loadUrl(BankData.url);
    }

    @Override
    public void logout() {

    }

    @Override
    public List<String> getTransactions() {
        step = 0;
        login();
        // nextCall() will be triggered by page loaded
        logout();
        return null;
    }

    @Override
    public void nextCall(String url, String response) {
        if (!url.equals(null) && !url.contains("?"))
            return;

        switch (step) {
            case 0:
                // username
                String command = String.format(BankData.tangerineCalls.get(step), userInfo.get("username"));
                sendJsData(command);

                break;
            case 1:
                // question
                command = BankData.tangerineCalls.get(step);
                webView.evaluateJavascript("(function() { return " + command + "; })();", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String s) {
                        promptInput(s);
                    }
                });
                break;
            case 2:
                // answering question
                command = String.format(BankData.tangerineCalls.get(step), response);
                sendJsData(command);

                break;
            case 3:
                // password
                command = String.format(BankData.tangerineCalls.get(step), userInfo.get("password"));
                sendJsData(command);

                break;
            default:
                break;
        }
        step += 1;
    }
}
