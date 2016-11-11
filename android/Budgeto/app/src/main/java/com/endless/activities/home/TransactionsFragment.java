package com.endless.activities.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.endless.bank.Category;
import com.endless.bank.Transaction;
import com.endless.budgeto.R;
import com.endless.tools.DeviceDataSaver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * PIN chooser for the application
 *
 * @author  Eric Matte
 * @version 1.0
 */
public class TransactionsFragment extends Fragment {
    private CategoryAdapter categoryAdapter;
    private List<Transaction> allBanksTransactions;
    private RecyclerView rootView;

    @Override
    public RecyclerView onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (this.rootView == null) {
            RecyclerView rootView = (RecyclerView) inflater.inflate(R.layout.tab_content_main, container, false);

            showCategories(rootView);

            this.rootView = rootView;
        }
        return this.rootView;
    }

    public void showCategories(RecyclerView recyclerView) {
        final DeviceDataSaver deviceDataSaver = new DeviceDataSaver(this.getContext());
        allBanksTransactions = deviceDataSaver.retrieveTransactionsList();
        HashMap<String, List<Transaction>> transMap = new HashMap<>();
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

        categoryAdapter = new CategoryAdapter(cats);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(categoryAdapter);
    }
}