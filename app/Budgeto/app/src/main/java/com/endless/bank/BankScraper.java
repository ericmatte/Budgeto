package com.endless.bank;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import com.endless.tools.Callable;
import com.endless.tools.Sanitizer;

import org.json.JSONObject;

import java.util.Map;

/**
 * This class extract transactions from bank.
 * A bank must inherit from this class.
 *
 * @author  Eric Matte
 * @version 1.0
 */
abstract public class BankScraper {

    protected String bankName;
    protected WebView webView;
    protected Context context;
    protected Map<String, String> userInfo;

    JSONObject bankResponse = new JSONObject();

    /** Instantiate a specific bank scraper from the given bankName */
    public static BankScraper fromName(String bankName, WebView webView, Context context,
                                       Map<String, String> userInfo) throws Exception {
        switch (bankName) {
            case "Tangerine":
                return new Tangerine(webView, context, userInfo);
            default:
                throw new Exception("The requested bank has not been implemented.");
        }
    }

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

        class MyJavaScriptInterface
        {
            @JavascriptInterface
            @SuppressWarnings("unused")
            public void processHTML(String html) { nextCall(null, html); }
        }
        webView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
    }

    public void sendJavascript(String command) {
        final String com = command;
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("javascript:(function() { " + com + "})()");
            }
        });
    }

    public void getDocumentHTML() {
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
            }
        });
    }

    abstract protected void login();
    abstract protected void logout();
    abstract public void requestTransactions(Callable callable, View parent);
    abstract protected void nextCall(String url, String response);

    public void promptInput(String value) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        alert.setTitle(bankName + " ask you a question.");
        alert.setMessage(value);

        // Set an EditText view to get user input
        final EditText input = new EditText(context);
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