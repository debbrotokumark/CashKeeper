package com.tanjimif.cashkeeper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tanjimif.cashkeeper.Adapter.HistoryAdapter;
import com.tanjimif.cashkeeper.Database.DatabaseHelper;
import com.tanjimif.cashkeeper.ViewModel.Transaction;

import java.util.List;

public class SavingActivity extends AppCompatActivity {
    private ImageView imgback, imgAddSaving;
    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private DatabaseHelper dbHelper;
    private TextView txtTotalAmout,allSaving;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving);

        initilization();
        clcikevent();

        dbHelper = new DatabaseHelper(this);

        double totalAmount = dbHelper.calculateTotalSavings();
        txtTotalAmout.setText(String.format("%.2f", totalAmount));

        if (fetchTransactions().isEmpty()){

            allSaving.setVisibility(View.INVISIBLE);
        }
        else{

            allSaving.setVisibility(View.VISIBLE);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HistoryAdapter(fetchTransactions());
        recyclerView.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider));
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void clcikevent() {
        imgback.setOnClickListener(view -> finish());
        imgAddSaving.setOnClickListener(view -> startActivity(new Intent(SavingActivity.this, AddSavingActivity.class)));
    }

    private void initilization() {
        recyclerView = findViewById(R.id.recyclerView);
        imgback = findViewById(R.id.imgback);
        imgAddSaving = findViewById(R.id.imgAddSaving);
        txtTotalAmout = findViewById(R.id.txtTotalAmout);
        allSaving = findViewById(R.id.allSaving);
    }

    private List<Transaction> fetchTransactions() {
        return dbHelper.getAllSavings();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        dbHelper = new DatabaseHelper(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HistoryAdapter(fetchTransactions());
        recyclerView.setAdapter(adapter);
        if (fetchTransactions().isEmpty()){
            allSaving.setVisibility(View.INVISIBLE);
        }
        else{
            allSaving.setVisibility(View.VISIBLE);
        }
        double totalAmount = dbHelper.calculateTotalSavings();
        txtTotalAmout.setText(String.format("%.2f", totalAmount));
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
