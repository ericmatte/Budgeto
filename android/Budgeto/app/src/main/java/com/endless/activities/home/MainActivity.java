package com.endless.activities.home;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.endless.bank.BankScraper;
import com.endless.bank.Category;
import com.endless.bank.Transaction;
import com.endless.budgeto.R;
import com.endless.tools.DeviceDataSaver;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    BankScraper bank;
    Map<String, String> userInfo = new HashMap<String, String>();
    WebView webView;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        showCategories();
    }


    public void addBill(View view) {

    }


    public void showCategories() {
        final DeviceDataSaver deviceDataSaver = new DeviceDataSaver(this.getBaseContext());
        List<Transaction> allBanksTransactions = deviceDataSaver.retrieveTransactionsList();
        HashMap<String, List<Transaction>> transMap = new HashMap();
        for (int i=0; i<allBanksTransactions.size(); i++) {
            Transaction t = allBanksTransactions.get(i);
            List<Transaction> ts = transMap.get(t.getCat());
            if (ts == null) ts = new ArrayList<>();
            ts.add(t);
            transMap.put(t.getCat(), ts);
        }

        List<Category> cats = new ArrayList<>();
        Iterator it = transMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry cat = (Map.Entry)it.next();
            cats.add(new Category((String) cat.getKey(), (List<Transaction>) cat.getValue()));
            it.remove(); // avoids a ConcurrentModificationException
        }

        ListAdapter adapter = new CategoryAdapter(this, cats);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }

}
