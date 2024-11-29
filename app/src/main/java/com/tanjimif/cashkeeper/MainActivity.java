package com.tanjimif.cashkeeper;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.tanjimif.cashkeeper.Database.DatabaseHelper;
import com.tanjimif.cashkeeper.ViewModel.Transaction;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    NavigationView navigationView;
    private ImageView imageMenu;
    private DatabaseHelper dbHelper;
    private TextView txtTotalAmout;
    private CardView cardHistory, cardSaving, cardaddMoney, cardaddExpense;
    View itemHistory1, itemHistory2;
    TextView txtCounter1, txtDes1, txtdate1, txtamount1;
    TextView txtCounter2, txtDes2, txtdate2, txtamount2;
    private LinearLayout lastLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DatabaseHelper(this);

        initialization();
        clickevent();
        GetLastTransaction();

        double totalAmount = dbHelper.calculateBalance();
        txtTotalAmout.setText(String.format("%.2f", totalAmount));

        drawerLayout = findViewById(R.id.drawer_layout);
        imageMenu = findViewById(R.id.imageMenu);

        imageMenu.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(androidx.core.view.GravityCompat.START)) {
                drawerLayout.closeDrawer(androidx.core.view.GravityCompat.START);
            } else {
                drawerLayout.openDrawer(androidx.core.view.GravityCompat.START);
            }
        });
    }

    private void GetLastTransaction() {
        List<Transaction> transactions = dbHelper.getAllTransactions();
        if (transactions != null && transactions.size() >= 2) {
            lastLayout.setVisibility(View.VISIBLE);
            Transaction transaction1 = transactions.get(0);
            Transaction transaction2 = transactions.get(1);
            txtCounter1.setText("1");
            txtDes1.setText(transaction1.getDescription());
            txtdate1.setText(transaction1.getDate());
            setAmmount(transaction1, txtamount1, txtCounter1);

            txtCounter2.setText("2");
            txtDes2.setText(transaction2.getDescription());
            txtdate2.setText(transaction2.getDate());
            setAmmount(transaction2, txtamount2, txtCounter2);
        } else {
            lastLayout.setVisibility(View.GONE);
        }
    }


    private void setAmmount(Transaction transaction,TextView txtamount,TextView txtCounter) {

        if (transaction.getType().equals("addmoney")){
            txtamount.setTextColor(ContextCompat.getColor(txtamount.getContext(), R.color.blue));
            txtamount.setText(String.format("+%s", transaction.getAmount()));
        }
        else if (transaction.getType().equals("expensemoney")){
            txtamount.setTextColor(ContextCompat.getColor(txtamount.getContext(), R.color.red));
            txtamount.setText(String.format("-%s", transaction.getAmount()));
        }

        Random random = new Random();
        int red = random.nextInt(150);
        int green = random.nextInt(150);
        int blue = random.nextInt(150);
        int randomColor = Color.rgb(red, green, blue);
        txtCounter.setBackgroundColor(randomColor);

    }


    private void clickevent() {
        cardHistory.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, History.class));
        });

        cardSaving.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, SavingActivity.class));
        });

        cardaddMoney.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, AddMoneyActivity.class));
        });

        cardaddExpense.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, ExpenseActivity.class));
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                String appPackageName = "com.tanjimif.cashkeeper";
                if(item.getItemId()==R.id.share){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    String shareText = "Check out this app: https://play.google.com/store/apps/details?id=" + appPackageName;
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                    startActivity(Intent.createChooser(shareIntent, "Share via"));
                    return true;
                }
                else if(item.getItemId()==R.id.Rate){
                    drawerLayout.closeDrawer(GravityCompat.START);

                    try {
                        Intent rateIntent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=" + appPackageName));
                        startActivity(rateIntent);
                    } catch (ActivityNotFoundException e) {

                        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName));
                        startActivity(browserIntent);
                    }
                    return true;
                }
                else if(item.getItemId()==R.id.About){
                    showAboutDialog();

                    drawerLayout.closeDrawer(GravityCompat.START);
                } else if(item.getItemId()==R.id.Privacypolicy){
                    startActivity(new Intent(MainActivity.this,PrivacyPolicy.class));
                    drawerLayout.closeDrawer(GravityCompat.START);
                }



                return true;
            }
        });

    }

    private void initialization() {
        cardHistory = findViewById(R.id.cardHistory);
        cardSaving = findViewById(R.id.cardSaving);
        cardaddMoney = findViewById(R.id.cardaddMoney);
        cardaddExpense = findViewById(R.id.cardaddExpense);
        txtTotalAmout = findViewById(R.id.txtTotalAmout);
        itemHistory1 = findViewById(R.id.itemHistory1);
        itemHistory2 = findViewById(R.id.itemHistory2);
        navigationView = findViewById(R.id.navigationView);

        txtCounter1 = itemHistory1.findViewById(R.id.txtCounter);
        txtDes1 = itemHistory1.findViewById(R.id.txtDes);
        txtdate1 = itemHistory1.findViewById(R.id.txtdate);
        txtamount1 = itemHistory1.findViewById(R.id.txtamount);

        txtCounter2 = itemHistory2.findViewById(R.id.txtCounter);
        txtDes2 = itemHistory2.findViewById(R.id.txtDes);
        txtdate2 = itemHistory2.findViewById(R.id.txtdate);
        txtamount2 = itemHistory2.findViewById(R.id.txtamount);
        lastLayout =findViewById(R.id.lastLayout);


    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exit App")
                .setIcon(R.drawable.icone)
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    super.onBackPressed();
                    finish();
                })
                .setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss();
                })
                .setCancelable(true)
                .show();
    }
    @Override
    protected void onPostResume() {
        super.onPostResume();
        GetLastTransaction();
        double totalAmount = dbHelper.calculateBalance();
        txtTotalAmout.setText(String.format("%.2f", totalAmount));
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
    private void showAboutDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("About")
                .setMessage("This app is designed to provide an excellent user experience with intuitive features and functionality. \n\n" +
                        "Developer: SoftOptimize\n" +
                        "Email: cashkeeper@gmail.com\n" +
                        "This app reflects the dedication and passion of the developer to create useful and innovative solutions.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builder.create().show();
    }


}
