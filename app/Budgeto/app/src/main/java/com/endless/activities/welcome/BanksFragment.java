package com.endless.activities.welcome;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.endless.budgeto.R;

import java.util.Arrays;
import java.util.List;

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

        List<String> bankNames = Arrays.asList("Tangerine", "Desjardins", "BNC");
        ListAdapter adapter = new BankAdapter(this.getContext(), bankNames);
        ListView lstBanks = (ListView) rootView.findViewById(R.id.lstBanks);
        lstBanks.setAdapter(adapter);

        return rootView;
    }
}