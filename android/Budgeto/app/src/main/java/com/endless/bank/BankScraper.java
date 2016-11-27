package com.endless.bank;

import android.app.Activity;
import android.app.Dialog;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * This class extract transactions from bank.
 * A bank must inherit from this class.
 *
 * @author  Eric Matte
 * @version 1.0
 */
abstract public class BankScraper {

    /** This is the list of all banks covered by budgeto */
    public enum Bank { Tangerine, Desjardins, TD }

    /** Instantiate a specific bank scraper from the given bankName */
    public static BankScraper fromName(Bank bank, WebView webView) throws Exception {
        BankScraper bankFromName;
        switch (bank) {
            case Tangerine:
                bankFromName = new Tangerine(webView);
                break;
            case Desjardins:
                bankFromName = new Desjardins(webView);
                break;
            default:
                throw new Exception("The requested bank has not been implemented.");
        }
        return bankFromName;
    }

    public void requestTransactions(BankCallable bankCallable, Dialog dialog) {
        this.bankCallable = bankCallable;
        this.dialog = dialog;
        webView.loadUrl(loginUrl);
    }

    protected Bank bank;
    protected String loginUrl, logoutUrl;
    protected WebView webView;


    protected BankResponse bankResponse;
    protected BankCallable bankCallable;
    protected Dialog dialog;

    public BankScraper(WebView webView) {
        this.bankResponse = new BankResponse(bank);
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
                String url = referrerUrl;
                referrerUrl = null;
                nextCall(url, Jsoup.parse(html));
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

    protected void getDocumentHTML(String referrerUrl) {
        this.referrerUrl = referrerUrl;
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
            }
        });
    }

    /** Make sur to always load webView url on UI thread. */
    protected void loadUrl(final String url) {
        ((Activity) webView.getContext()).runOnUiThread(new Runnable() {
            @Override public void run() { webView.loadUrl(url); }
        });
    }

    // Save the referrer url when asking to get document HTML
    private String referrerUrl;

    /** List of calls to scrap a bank */
    abstract protected void nextCall(String url, Document documentHTML);

}