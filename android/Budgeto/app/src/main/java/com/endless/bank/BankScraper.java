package com.endless.bank;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import com.endless.tools.Callable;
import com.endless.tools.Sanitizer;
import com.endless.tools.Sanitizer.StringType;

/**
 * This class extract transactions from bank.
 * A bank must inherit from this class.
 *
 * @author  Eric Matte
 * @version 1.0
 */
abstract public class BankScraper {

    protected String bankName;
    protected String loginUrl, logoutUrl;
    protected WebView webView;

    protected Callable callable;
    protected String username, password;

    /** Instantiate a specific bank scraper from the given bankName */
    public static BankScraper fromName(String bankName, WebView webView) throws Exception {
        BankScraper bankFromName;
        switch (bankName) {
            case "Tangerine":
                bankFromName = new Tangerine(webView);
                break;
            default:
                throw new Exception("The requested bank has not been implemented.");
        }

        return bankFromName;
    }

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

    abstract public void requestTransactions(Callable callable, String username, String password);
    abstract protected void nextCall(String url, String response);

    abstract protected boolean validateUsername(String usr);
    abstract protected boolean validatePassword(String pwd);
    protected boolean validateString(String s, int min, int max, StringType stringType) {
        String validator = Sanitizer.validateString(s, min, max, stringType);
        return validator.equals("validated");
    }

    protected void promptInput(String value) {
        AlertDialog.Builder alert = new AlertDialog.Builder(webView.getContext());

        alert.setTitle(bankName + " vous pose une question.");
        alert.setMessage(value);

        // Set an EditText view to get user input
        final EditText input = new EditText(webView.getContext());
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = Sanitizer.sanitize(input.getText().toString());
                nextCall(null, value);
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