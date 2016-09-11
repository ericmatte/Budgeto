package com.endless.bank;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import java.util.Map;

abstract public class BankScraper {

    protected String bankName;
    protected WebView webView;
    protected Context context;
    protected Map<String, String> userInfo;

    public BankScraper(WebView webView, Context context, Map<String, String> userInfo) {
        this.userInfo = userInfo;
        this.context = context;
        this.webView = webView;
        this.webView.getSettings().setJavaScriptEnabled(true);

        this.webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                nextCall(url, null);
            }
        });
    }

    abstract protected void login();
    abstract protected void logout();
    abstract public void requestTransactions();
    abstract protected void nextCall(String url, String response);

    public void sendJsData(String command) {
        webView.loadUrl("javascript:(function() { " + command + "})()");
    }

    public void requestJsData(String command) {
        webView.evaluateJavascript("(function() { return " + command + "; })();", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String response) {
                nextCall(null, response);
            }
        });
    }

    public void promptInput(String value) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        alert.setTitle(BankData.bank + " vous demande une question.");
        alert.setMessage(value);

        // Set an EditText view to get user input
        final EditText input = new EditText(context);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                nextCall(null, input.getText().toString());
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }
}