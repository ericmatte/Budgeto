package com.endless.activities.welcome;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.endless.bank.BankScraper.Bank;
import com.endless.budgeto.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Banks selector for the application
 *
 * @author  Eric Matte
 * @version 1.0
 */
public class BanksFragment extends Fragment {
    public BanksFragment() {}
    public ListAdapter bankAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_banks, container, false);

        ArrayList<Bank> banks = new ArrayList<Bank>(Arrays.asList(Bank.values()));
        bankAdapter = new BankAdapter(this.getContext(), banks);
        ListView lstBanks = (ListView) rootView.findViewById(R.id.lstBanks);
        lstBanks.setAdapter(bankAdapter);

        return rootView;
    }
}