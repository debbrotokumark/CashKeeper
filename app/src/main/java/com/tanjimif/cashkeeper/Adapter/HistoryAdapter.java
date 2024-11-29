package com.tanjimif.cashkeeper.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.tanjimif.cashkeeper.R;
import com.tanjimif.cashkeeper.ViewModel.Transaction;
import java.util.List;
import java.util.Random;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryHolder> {

    private List<Transaction> transactions;

    public HistoryAdapter(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public HistoryAdapter.HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemhistory, parent, false);
        return new HistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.HistoryHolder holder, int position) {

        Random random = new Random();
        int red = random.nextInt(150);   // Random value between 0 and 255
        int green = random.nextInt(150);
        int blue = random.nextInt(150);
        int randomColor = Color.rgb(red, green, blue);
        holder.txtCounter.setBackgroundColor(randomColor);
        holder.txtCounter.setText(String.valueOf(position + 1));

        Transaction transaction = transactions.get(position);
        holder.txtDes.setText(transaction.getDescription());


        if (transaction != null) {
            holder.txtDes.setText(transaction.getDescription());
            holder.txtdate.setText(transaction.getDate());
            if (transaction.getType().equals("addmoney")) {
                holder.txtamount.setTextColor(ContextCompat.getColor(holder.txtamount.getContext(), R.color.blue));
                holder.txtamount.setText(String.format("+%s", transaction.getAmount()));
            } else if (transaction.getType().equals("expensemoney")) {
                holder.txtamount.setTextColor(ContextCompat.getColor(holder.txtamount.getContext(), R.color.red));
                holder.txtamount.setText(String.format("-%s", transaction.getAmount()));
            }
        }



    }

    @Override
    public int getItemCount() {
        return transactions != null ? transactions.size() : 0;
    }

    public class HistoryHolder extends RecyclerView.ViewHolder {
        TextView txtCounter, txtDes,txtdate,txtamount;

        public HistoryHolder(@NonNull View itemView) {
            super(itemView);
            txtCounter = itemView.findViewById(R.id.txtCounter);
            txtDes = itemView.findViewById(R.id.txtDes);
            txtdate = itemView.findViewById(R.id.txtdate);
            txtamount = itemView.findViewById(R.id.txtamount);
        }
    }
}
