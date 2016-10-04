package com.endless.activities.welcome;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.endless.budgeto.R;

/**
 * Banks selector for the application
 *
 * @author  Eric Matte
 * @version 1.0
 */
public class BanksFragment extends Fragment {
    public BanksFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_banks, container, false);

        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.banks_recycler_view);
        RecyclerView.Adapter mAdapter;
        RecyclerView.LayoutManager mLayoutManager;

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        String[] myDataset = {"Tangerine", "Desjardins", "BNC"};
        mAdapter = new BankAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }
}