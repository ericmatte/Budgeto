package com.endless.bank;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.endless.budgeto.R;

import static android.widget.GridLayout.HORIZONTAL;
import static android.widget.GridLayout.VERTICAL;

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

    private RelativeLayout setupBankDialogContent(View referrerView) {
        // Setup dialog views
        Context that = referrerView.getContext();
        LayoutParams match_parent = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        LayoutParams match_wrap_content = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        LayoutParams wrap_content = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);


        RelativeLayout dialogContent = new RelativeLayout(that);
        dialogContent.setId(R.id.alertWeb);
        dialogContent.setLayoutParams(match_parent);

        LinearLayout linearLayout_381 = new LinearLayout(that);
        linearLayout_381.setOrientation(VERTICAL);
        linearLayout_381.setLayoutParams(match_parent);

        LinearLayout linearLayout_511 = new LinearLayout(that);
        linearLayout_511.setOrientation(HORIZONTAL);
        linearLayout_511.setLayoutParams(match_wrap_content);

        TextView txtAlertTitle = new TextView(that);
        txtAlertTitle.setText("AlertTitle");
        txtAlertTitle.setId(R.id.txtAlertTitle);
        txtAlertTitle.setTextSize((18/that.getResources().getDisplayMetrics().scaledDensity));
        txtAlertTitle.setTextColor(that.getResources().getColor(R.color.common_google_signin_btn_text_dark_focused));
        txtAlertTitle.setLayoutParams(match_wrap_content);
        linearLayout_511.addView(txtAlertTitle);

        ImageButton btnCancelAlert = new ImageButton(that);
        btnCancelAlert.setId(R.id.btnCancelAlert);
        btnCancelAlert.setLayoutParams(wrap_content);
        linearLayout_511.addView(btnCancelAlert);
        linearLayout_381.addView(linearLayout_511);
        dialogContent.addView(linearLayout_381);

        return dialogContent;
    }

    abstract protected void nextCall(String url, String response);

}