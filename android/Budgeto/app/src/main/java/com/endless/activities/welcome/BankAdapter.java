package com.endless.activities.welcome;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.endless.bank.BankScraper;
import com.endless.budgeto.R;
import com.endless.tools.Callable;
import com.endless.tools.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 2016-09-25.
 */
public class BankAdapter extends ArrayAdapter<String> implements Callable {
    private List<View> views = new ArrayList<>();

    public BankAdapter(Context context, List<String> resource) {
        super(context, R.layout.card, resource);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.card, parent, false);

            view.findViewById(R.id.layMore).setVisibility(View.GONE);
            setup_listeners(getItem(position), view);

            views.add(view);
        }
        return view;
    }

    private View getView(String item) {
        View view = null;
        for (int i=0; i<getCount(); i++) if (getItem(i).equals(item)) view = views.get(i);
        return view;
    }

    private void setup_listeners(final String bankName, final View parent) {
        final BankAdapter bankAdapter = this;
        CheckBox cbxBankName = (CheckBox) parent.findViewById(R.id.cbxBankName);
        Button btnTestBank = (Button) parent.findViewById(R.id.btnTestBank);

        cbxBankName.setText(bankName);
        cbxBankName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Toggle bank fields
                parent.findViewById(R.id.layMore).setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });


        btnTestBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usr = ((EditText) parent.findViewById(R.id.txtCredential)).getText().toString();
                String pwd = ((EditText) parent.findViewById(R.id.txtPassword)).getText().toString();
                WebView webView = (WebView) parent.findViewById(R.id.webView);

                parent.findViewById(R.id.pbTestBank).setVisibility(View.VISIBLE);

                Logger.print(this.getClass(), usr + " - " + pwd, bankName + " card");

                try {
                    BankScraper bank = BankScraper.fromName(bankName, webView);
                    bank.requestTransactions(bankAdapter, usr, pwd);
                } catch (Exception e) { Logger.print(this.getClass(), e.getMessage(), bankName); }
            }
        });
    }

    public void callBack(JSONObject jsonObject) {
        try {
            String state = jsonObject.getString("state");
            View view = getView(jsonObject.getString("bank"));
            TextView txtBankCallback = (TextView) view.findViewById(R.id.txtBankCallback);

            Logger.print(this.getClass(), state);
            if (state.equals("ok")) {
                int nbrTransactions = jsonObject.getJSONArray("transactions").length();
                txtBankCallback.setTextColor(view.getContext().getResources().getColor(R.color.colorPrimary));
                txtBankCallback.setText("Succès! (" + String.valueOf(nbrTransactions) + " transactions)");
            } else {
                txtBankCallback.setTextColor(Color.RED);
                txtBankCallback.setText("Erreur!");
            }

            txtBankCallback.setVisibility(View.VISIBLE);
            view.findViewById(R.id.pbTestBank).setVisibility(View.GONE);
        } catch (JSONException e) {
            Logger.print(this.getClass(), e.getMessage());
        }
    }
}