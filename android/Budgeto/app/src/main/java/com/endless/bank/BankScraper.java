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

    protected Bank bank;
    protected String loginUrl, logoutUrl;
    protected BankResponse bankResponse; // The response after fetching transactions
    protected BankCallable bankCallable; // The callback
    protected WebView webView;
    protected Dialog dialog;

    private String referrerUrl; // Save the referrer url when asking to get document HTML

    public BankScraper(WebView webView, Bank bank) {
        this.bank = Bank.Tangerine;
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
        class MyJavaScriptInterface {
            @JavascriptInterface
            @SuppressWarnings("unused")
            public void processHTML(String html) {
                String url = referrerUrl;
                referrerUrl = null;
                nextCall(url, Jsoup.parse(html));
            }
        }
        this.webView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
    }

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

    protected void sendJavascript(final String command) {
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("javascript:(function() { " + command + "})()");
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

    /** List of calls to scrap a bank */
    abstract protected void nextCall(String url, Document htmlDocument);

}