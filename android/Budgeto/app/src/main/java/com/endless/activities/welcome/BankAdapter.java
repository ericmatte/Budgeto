package com.endless.activities.welcome;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.endless.bank.BankCallable;
import com.endless.bank.BankResponse;
import com.endless.bank.BankScraper;
import com.endless.bank.BankScraper.Bank;
import com.endless.budgeto.R;
import com.endless.tools.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 2016-09-25.
 */
public class BankAdapter extends ArrayAdapter<Bank> implements BankCallable {
    private List<View> views = new ArrayList<>();

    public BankAdapter(Context context, List<Bank> resource) {
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

    private View getView(Bank item) {
        View view = null;
        for (int i=0; i<getCount(); i++) if (getItem(i).equals(item)) view = views.get(i);
        return view;
    }

    public List<BankResponse> getBankResponses() {
        List<BankResponse> bankResponses = new ArrayList<>();
        for (int i=0; i<views.size(); i++) {
            BankResponse bankResponse = (BankResponse) views.get(i).getTag();
            if (bankResponse != null) {
                // Check if bank is selected
                if (((CheckBox) views.get(i).findViewById(R.id.cbxBankName)).isChecked()) {
                    bankResponses.add(bankResponse);
                }
            }
        }
        return bankResponses;
    }

    private void setup_listeners(final Bank bank, final View parent) {
        final BankAdapter bankAdapter = this;
        CheckBox cbxBankName = (CheckBox) parent.findViewById(R.id.cbxBankName);
        Button btnTestBank = (Button) parent.findViewById(R.id.btnTestBank);

        cbxBankName.setText(String.valueOf(bank));
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
                parent.findViewById(R.id.txtBankCallback).setVisibility(View.GONE);
                parent.findViewById(R.id.pbTestBank).setVisibility(View.VISIBLE);

                LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.bank_login_dialog, null);

                WebView webView = (WebView) dialogView.findViewById(R.id.webView);
                Dialog dialog = showBankLoginDialog(dialogView, bank);

                try {
                    BankScraper bankScraper = BankScraper.fromName(bank, webView);
                    bankScraper.requestTransactions(bankAdapter, dialog);
                } catch (Exception e) {
                    Logger.print(this.getClass(), e.getMessage(), String.valueOf(bank));
                }
            }
        });
    }

    private Dialog showBankLoginDialog(View dialogView, final Bank bank) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                callBack(new BankResponse(bank, BankResponse.ErrorFrom.internal, "Request canceled."));
            }
        });

        /* HACK TODO: Remove the hack */
        EditText keyboardHack = new EditText(getContext());
        keyboardHack.setVisibility(View.GONE);
        ((RelativeLayout) dialogView.findViewById(R.id.alertWeb))
                .addView(keyboardHack, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        /* END OF HACK */

        ((TextView) dialogView.findViewById(R.id.txtAlertTitle)).setText("Connection à " + bank.toString());
        alert.setView(dialogView);

        final Dialog dialog = alert.show();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialogView.findViewById(R.id.btnCancelAlert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { dialog.cancel(); }
        });

        return dialog;
    }

    public void callBack(final BankResponse response) {
        final View view = getView(response.getBank());
        ((Activity) view.getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BankResponse.State state = response.getState();
                TextView txtBankCallback = (TextView) view.findViewById(R.id.txtBankCallback);

                Logger.print(this.getClass(), String.valueOf(state));
                if (state == BankResponse.State.ok) {
                    view.setTag(response);
                    int nbrTransactions = response.getTransactions().size();
                    txtBankCallback.setTextColor(view.getContext().getResources().getColor(R.color.colorPrimary));
                    txtBankCallback.setText("Succès! (" + String.valueOf(nbrTransactions) + " transactions)");
                } else {
                    view.setTag(null);
                    txtBankCallback.setTextColor(Color.RED);
                    txtBankCallback.setText("Opération annulée!");
                }

                view.findViewById(R.id.pbTestBank).setVisibility(View.GONE);
                txtBankCallback.setVisibility(View.VISIBLE);
            }
        });
    }
}