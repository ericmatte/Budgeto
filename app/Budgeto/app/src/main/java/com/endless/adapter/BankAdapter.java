package com.endless.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.endless.budgeto.R;

/**
 * Created by Eric on 2016-09-25.
 */
public class BankAdapter extends RecyclerView.Adapter<BankAdapter.ViewHolder> {
    private String[] mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mView;
        public ViewHolder(View v) {
            super(v);
            mView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public BankAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public BankAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card, parent, false);
        // set the view's size, margins, paddings and layout parameters
        LinearLayout layMore = (LinearLayout) v.findViewById(R.id.layMore);
        layMore.setVisibility(View.GONE);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        setup_listeners(mDataset[position], holder.mView);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    private void setup_listeners(String bankName, final View parent) {
        CheckBox cbxBankName = (CheckBox) parent.findViewById(R.id.cbxBankName);
        Button btnTestBank = (Button) parent.findViewById(R.id.btnTestBank);

        cbxBankName.setText(bankName);
        cbxBankName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                LinearLayout layMore = (LinearLayout) parent.findViewById(R.id.layMore);
                layMore.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });


        btnTestBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String credential = ((EditText) parent.findViewById(R.id.txtCredential)).getText().toString();
                String password = ((EditText) parent.findViewById(R.id.txtPassword)).getText().toString();
                ProgressBar pbTestBank = (ProgressBar) parent.findViewById(R.id.pbTestBank);
                pbTestBank.setVisibility(View.VISIBLE);
                Log.d("App: Tester", credential + " - " + password);
            }
        });
    }
}