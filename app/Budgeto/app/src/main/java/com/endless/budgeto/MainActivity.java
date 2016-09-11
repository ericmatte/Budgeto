package com.endless.budgeto;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;

import com.endless.bank.BankScraper;
import com.endless.bank.Tangerine;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

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
        BankScraper tangerine = new Tangerine(webView, this, userInfo);
        tangerine.requestTransactions();
    }



    public void addBill(View view) {
    }


}
