package com.endless.budgeto;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Map<String, String> userInfo = new HashMap<String, String>();
    WebView webView;
    JavascriptBridge bridge;

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
        bridge = new JavascriptBridge(webView);
        webView.loadUrl(BankData.url);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (url.contains("?")) {
                    nextCall(null);
                }
            }
        });
    }



    public void addBill(View view) {
    }

    int step = 0;
    public void nextCall(String value) {
        switch (step) {
            case 0:
                // username
                String command = String.format(BankData.tangerineCalls.get(step), userInfo.get("username"));
                bridge.sendJsData(command);

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
                command = String.format(BankData.tangerineCalls.get(step), value);
                bridge.sendJsData(command);

                break;
            case 3:
                // password
                command = String.format(BankData.tangerineCalls.get(step), userInfo.get("password"));
                bridge.sendJsData(command);

                break;
            default:
                break;
        }
        step += 1;
    }

    public void promptInput(String value) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(BankData.bank + " want you to answer a question.");
        alert.setMessage(value);

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                nextCall(input.getText().toString());
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
