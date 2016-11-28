package com.endless.activities.home;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CatViewHolder> {

    private List<Category> categoryList;
    private boolean showProgress;

    public class CatViewHolder extends RecyclerView.ViewHolder {
        public TextView title, amount;
        public ProgressBar progressBar;
        public LinearLayout transactionList;

        public CatViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.txtTitle);
            amount = (TextView) view.findViewById(R.id.txtAmount);
            progressBar = (ProgressBar) view.findViewById(R.id.pbLimit);
            transactionList = (LinearLayout) view.findViewById(R.id.linearTrans);
        }
    }

    public CategoryAdapter(List<Category> categoryList, boolean showProgress) {
        this.categoryList = categoryList;
        this.showProgress = showProgress;
    }

    @Override
    public CatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_row, parent, false);

        return new CatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CatViewHolder holder, int position) {
        Category category = categoryList.get(position);

        // Get all transactions
        List<Transaction> transactions = category.getAssociatedTransactions();
        float categoryAmount = 0;
        for (int i = 0; i < transactions.size(); i++) {
            Transaction transaction = transactions.get(i);
            categoryAmount += transaction.getAmount();

            TextView txtTrans = new TextView(holder.transactionList.getContext());
            txtTrans.setText(transaction.getAmount() + "$ -- " + transaction.getDesc());
            holder.transactionList.addView(txtTrans);
        }

        if (showProgress) {
            // Get simulated progress
            int current = (int) Math.abs(categoryAmount);
            int objective = ThreadLocalRandom.current().nextInt(current, current * 2 + 1);
            holder.progressBar.setMax(objective);
            holder.progressBar.setProgress(current);
            holder.progressBar.setScaleY(1.5f);
            holder.amount.setText(String.valueOf(Math.abs(categoryAmount)) + "$ of " + String.valueOf(objective) + "$");
        } else {
            holder.progressBar.setVisibility(View.GONE);
            holder.amount.setVisibility(View.GONE);
        }

        holder.title.setText(category.getName());
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}
