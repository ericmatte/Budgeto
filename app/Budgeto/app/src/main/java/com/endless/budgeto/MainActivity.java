package com.endless.budgeto;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    String bank = "Tangerine";
    String url = "https://secure.tangerine.ca/web/InitialTangerine.html?command=displayLogin&device=web&locale=fr_CA";
    Map<String, String> userInfo = new HashMap<String, String>();
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userInfo.put("username", extras.getString("username"));
            userInfo.put("password", extras.getString("password"));
        }
        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(this, "android");
        webView.loadUrl(url);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Snackbar.make(view, "Page loaded", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    int step = 0;
    public void addBill(View view) {
        switch (step) {
            case 0:
                Log.d("username", userInfo.get("username"));
                String input_id = "ACN";
                String command = "javascript:(function() { "
                        + "input = $('#" + input_id + "');"
                        + "input.val('" + userInfo.get("username") + "');"
                        + "input.closest(\"form\").submit();"
                        + "})()";
                webView.loadUrl(command);
                break;
            case 1:
                Log.d("Question:", "Starting");
                command = "$(\"div.content-main-wrapper .CB_DoNotShow:first\").html()";
                webView.loadUrl("javascript:android.question(" + command + ")");
            default:
                break;
        }
        step += 1;
    }


    @JavascriptInterface
    public void question(String value) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(bank + " want you to answer a question");
        alert.setMessage(value);

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String input_id = "Answer";
                String command = "input = $('#" + input_id + "')"
                        + "input.val('" + input.getText().toString() + "')"
                        + "input.closest('form').submit()";
                WebView wv = (WebView)findViewById(R.id.webView);
                wv.loadUrl("javascript:" + command);
                // Do something with value!
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
