package com.endless.budgeto;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    String bank = "Tangerine";
    String url = "https://secure.tangerine.ca/web/InitialTangerine.html?command=displayLogin&device=web&locale=fr_CA";
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
        webView.loadUrl(url);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
    }

    int step = 0;
    public void addBill(View view) {
        try {
            switch (step) {
                case 0:
                    Log.d("username", userInfo.get("username"));
                    String input_id = "ACN";
                    String command = "input = $('#" + input_id + "');"
                            + "input.val('" + userInfo.get("username") + "');"
                            + "input.closest(\"form\").submit();";
                    bridge.sendJsData(command);

                    break;
                case 1:
                    Log.d("Question:", "Starting");
                    command = "$(\"div.content-main-wrapper .CB_DoNotShow:first\").html()";
                    String question = bridge.getJsData(command);
                    Log.d("La question", question);
                    break;
                case 10:
                    Log.d("username", userInfo.get("username"));
                    input_id = "ACN";
                    command = "javascript:(function() { "
                            + "input = $('#" + input_id + "');"
                            + "input.val('" + userInfo.get("username") + "');"
                            + "input.closest(\"form\").submit();"
                            + "})()";
                    webView.loadUrl(command);
                    break;
                case 100:
                    Log.d("Question:", "Starting");
                    command = "$(\"div.content-main-wrapper .CB_DoNotShow:first\").html()";
                    webView.loadUrl("javascript:android.question(" + command + ")");
                default:
                    break;
            }
            step += 1;
        }
        catch (InterruptedException e) {
            Log.e("ERROR", e.getMessage());
        }
    }


//    @JavascriptInterface
//    public void question(String value) {
//        AlertDialog.Builder alert = new AlertDialog.Builder(this);
//
//        alert.setTitle(bank + " want you to answer a question");
//        alert.setMessage(value);
//
//        // Set an EditText view to get user input
//        final EditText input = new EditText(this);
//        alert.setView(input);
//
//        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                String input_id = "Answer";
//                final String command = "input = $('#" + input_id + "');"
//                        + "input.val('" + input.getText().toString() + "');"
//                        + "input.closest('form').submit();";
//
//                webView.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        webView.loadUrl("javascript:" + command);
//                    }
//                });
//            }
//        });
//
//        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                // Canceled.
//            }
//        });
//
//        alert.show();
//    }


}
