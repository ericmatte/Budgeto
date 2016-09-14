package com.endless.budgeto;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;


/**
 * Created by Eric on 2016-09-13.
 */
public class Category extends Fragment {

    // The interface communicate out to the parent Activity
    public interface TransactionsFetcher {
        public void getDedicatedTransactions(JSONObject bankResponse);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.category, container, false);
    }
}
