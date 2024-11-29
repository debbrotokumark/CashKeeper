package com.tanjimif.cashkeeper;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.tanjimif.cashkeeper.Database.DatabaseHelper;
import com.tanjimif.cashkeeper.ViewModel.Transaction;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExpenseActivity extends AppCompatActivity {
    private ImageView imgback;
    private EditText edAmount, edDetails;
    private TextView errorText;
    private Button saveButton;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        databaseHelper = new DatabaseHelper(this);

        initialization();
        clickEvent();
    }

    private void clickEvent() {
        imgback.setOnClickListener(view -> finish());

        saveButton.setOnClickListener(view -> {
            String amount = edAmount.getText().toString().trim();
            String description = edDetails.getText().toString().trim();
            String date = new SimpleDateFormat("dd/MM/yy").format(new Date());
            String type = "expensemoney"; // Example predefined value for type

            if (amount.isEmpty() || description.isEmpty()) {
                showError("Please fill in all fields");
            } else {
                Transaction transaction = new Transaction(amount, description, date, type);

                boolean isInserted = databaseHelper.insertData(transaction);

                if (isInserted) {
                    Toast.makeText(ExpenseActivity.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ExpenseActivity.this, "Failed to save data", Toast.LENGTH_SHORT).show();
                }

                edAmount.setText("");
                edDetails.setText("");
            }
        });
    }

    private void initialization() {
        imgback = findViewById(R.id.imgback);
        edAmount = findViewById(R.id.edAmmount);
        edDetails = findViewById(R.id.edDetails);
        errorText = findViewById(R.id.errorText);
        saveButton = findViewById(R.id.saveButton);
    }

    private void showError(String message) {
        errorText.setText(message);
        errorText.setVisibility(TextView.VISIBLE);
        new Handler().postDelayed(() -> errorText.setVisibility(TextView.INVISIBLE), 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}
