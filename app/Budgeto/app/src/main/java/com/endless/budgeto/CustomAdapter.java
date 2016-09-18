package com.endless.budgeto;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Custom adapter for displaying categories on main listView.
 *
 * @author  Eric Matte
 * @version 1.0
 */
class CustomAdapter extends ArrayAdapter<JSONObject> {

    public CustomAdapter(Context context, List<JSONObject> resource) {
        super(context, R.layout.category_row, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.category_row, parent, false);

        JSONObject singleItem = getItem(position);
        TextView txtCat = (TextView) customView.findViewById(R.id.txtCat);
        ProgressBar pbLimit = (ProgressBar) customView.findViewById(R.id.pbLimit);
        TextView txtAmount = (TextView) customView.findViewById(R.id.txtAmount);
        LinearLayout linearTrans = (LinearLayout) customView.findViewById(R.id.linearTrans);

        try {
            txtCat.setText((String) singleItem.get("cat"));

            float amount = 0;
            JSONArray transactions = (JSONArray) singleItem.get("trans");
            for (int i = 0; i < transactions.length(); i++) {
                JSONObject transaction = (JSONObject) transactions.get(i);
                amount += Float.parseFloat(((String)(transaction.get("amount"))).replace(",", "."));

                TextView txtTrans = new TextView(getContext());
                txtTrans.setText(transaction.get("amount") + "$ -- " + transaction.get("desc"));
                linearTrans.addView(txtTrans);
            }

            int current = (int) Math.abs(amount);
            int objective = ThreadLocalRandom.current().nextInt(current, current*2 + 1);
            pbLimit.setMax(objective);
            pbLimit.setProgress(current);
            pbLimit.setScaleY(2.2f);
            pbLimit.setProgressTintList(ColorStateList.valueOf(Color.rgb(20,60,180)));

            txtAmount.setText(String.valueOf(Math.abs(amount)) + "$ of " + String.valueOf(objective) + "$");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return customView;
    }
}
