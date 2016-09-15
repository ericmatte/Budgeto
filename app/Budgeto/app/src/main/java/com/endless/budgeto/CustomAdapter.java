package com.endless.budgeto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Eric on 2016-09-14.
 */
class CustomAdapter extends ArrayAdapter<String> {

    public CustomAdapter(Context context, List<String> resource) {
        super(context, R.layout.category_row, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.category_row, parent, false);

        String singleItem = getItem(position);
        TextView txtCat = (TextView) customView.findViewById(R.id.txtCat);
        ProgressBar pbLimit = (ProgressBar) customView.findViewById(R.id.pbLimit);
        TextView txtAmount = (TextView) customView.findViewById(R.id.txtAmount);

        txtCat.setText(singleItem);
        // txtAmount.setText(String.valueOf(position) + " of something");

        return customView;
    }
}
