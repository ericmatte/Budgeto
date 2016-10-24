package com.endless.activities.home;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.endless.bank.Category;
import com.endless.bank.Transaction;
import com.endless.budgeto.R;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Custom adapter for displaying categories on main listView.
 *
 * @author  Eric Matte
 * @version 1.0
 */
public class CategoryAdapter extends ArrayAdapter<Category> {

    public CategoryAdapter(Context context, List<Category> resource) {
        super(context, R.layout.category_row, resource);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        view = inflater.inflate(R.layout.category_row, parent, false);

        Category singleItem = getItem(position);
        TextView txtCat = (TextView) view.findViewById(R.id.txtCat);
        ProgressBar pbLimit = (ProgressBar) view.findViewById(R.id.pbLimit);
        TextView txtAmount = (TextView) view.findViewById(R.id.txtAmount);
        LinearLayout linearTrans = (LinearLayout) view.findViewById(R.id.linearTrans);


        txtCat.setText((String) singleItem.getName());
        List<Transaction> transactions = singleItem.getAssociatedTransactions();

        float categoryAmount = 0;
        for (int i = 0; i < transactions.size(); i++) {
            Transaction transaction = transactions.get(i);
            if (!transaction.getAmount().equals(""))
                categoryAmount += Float.parseFloat(((String) (transaction.getAmount())).replace(",", ".").replace(" ", ""));

            TextView txtTrans = new TextView(getContext());
            txtTrans.setText(transaction.getAmount() + "$ -- " + transaction.getDesc());
            linearTrans.addView(txtTrans);
        }

        int current = (int) Math.abs(categoryAmount);
        int objective = ThreadLocalRandom.current().nextInt(current, current * 2 + 1);
        pbLimit.setMax(objective);
        pbLimit.setProgress(current);
        pbLimit.setScaleY(2.2f);
        pbLimit.setProgressTintList(ColorStateList.valueOf(Color.rgb(20, 60, 180)));

        txtAmount.setText(String.valueOf(Math.abs(categoryAmount)) + "$ of " + String.valueOf(objective) + "$");
        
        return view;
    }
}
