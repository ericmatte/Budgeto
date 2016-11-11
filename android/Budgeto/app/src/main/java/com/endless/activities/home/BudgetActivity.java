package com.endless.activities.home;

import android.graphics.Movie;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

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

public class BudgetActivity extends AppCompatActivity {

    private List<Movie> categoryList = new ArrayList<>();
    private CategoryAdapter categoryAdapter;
    private List<Transaction> allBanksTransactions;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Votre Budget");
        setSupportActionBar(toolbar);

        showCategories();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void showCategories() {
        final DeviceDataSaver deviceDataSaver = new DeviceDataSaver(this.getBaseContext());
        allBanksTransactions = deviceDataSaver.retrieveTransactionsList();
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

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        categoryAdapter = new CategoryAdapter(cats);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(categoryAdapter);
    }
}
