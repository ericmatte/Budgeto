package com.endless.bank;

import android.app.Activity;
import android.app.Dialog;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * This class extract transactions from bank.
 * A bank must inherit from this class.
 *
 * @author  Eric Matte
 * @version 1.0
 */
abstract public class BankScraper {

    /** This is the list of all banks covered by budgeto */
    public enum Bank { Tangerine, Desjardins }

    /** Instantiate a specific bank scraper from the given bankName */
    public static BankScraper fromName(Bank bank, WebView webView) throws Exception {
        BankScraper bankFromName;
        switch (bank) {
            case Tangerine:
                bankFromName = new Tangerine(webView);
                break;
            default:
                throw new Exception("The requested bank has not been implemented.");
        }
        return bankFromName;
    }

    protected Bank bank;
    protected String loginUrl, logoutUrl;
    protected WebView webView;

    protected BankCallable bankCallable;
    protected Dialog dialog;

    public BankScraper(WebView webView) {
        this.webView = webView;
        this.webView.getSettings().setJavaScriptEnabled(true);
        this.webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                nextCall(url, null);
            }
        });

        class MyJavaScriptInterface
        {
            @JavascriptInterface
            @SuppressWarnings("unused")
            public void processHTML(String html) {
                nextCall(null, html);
            }
        }
        webView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
    }

    protected void sendJavascript(String command) {
        final String com = command;
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("javascript:(function() { " + com + "})()");
            }
        });
    }

    protected void getDocumentHTML() {
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
            }
        });
    }

    public void requestTransactions(BankCallable bankCallable, Dialog dialog) {
        this.bankCallable = bankCallable;
        this.dialog = dialog;
        webView.loadUrl(loginUrl);
    }

    /** Make sur to always load webView url on UI thread. */
    protected void loadUrl(final String url) {
        ((Activity) webView.getContext()).runOnUiThread(new Runnable() {
            @Override public void run() { webView.loadUrl(url); }
        });
    }

    abstract protected void nextCall(String url, String response);

}