package com.tanjimif.cashkeeper;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tanjimif.cashkeeper.Adapter.HistoryAdapter;
import com.tanjimif.cashkeeper.Database.DatabaseHelper;
import com.tanjimif.cashkeeper.ViewModel.Transaction;

import java.util.ArrayList;
import java.util.List;

public class History extends AppCompatActivity {
    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private ImageView imgback;
    private DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        databaseHelper = new DatabaseHelper(this);

        initilization();
        clcikevent();


        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        List<Transaction> transactions = databaseHelper.getAllTransactions();
        if (transactions.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
        }


        adapter = new HistoryAdapter(transactions);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.divider);
        if (drawable != null) {
            dividerItemDecoration.setDrawable(drawable);
        }
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void clcikevent() {
        imgback.setOnClickListener(view ->{
            finish();
        });
    }

    private void initilization() {
        recyclerView = findViewById(R.id.recyclerView);
        imgback = findViewById(R.id.imgback);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }

}