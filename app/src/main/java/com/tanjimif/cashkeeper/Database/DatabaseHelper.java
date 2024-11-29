package com.tanjimif.cashkeeper.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.tanjimif.cashkeeper.ViewModel.Transaction;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Cashkeeper";
    private static final int DATABASE_VERSION = 2;  // Incremented version for schema changes

    private static final String TABLE_NAME = "AddMoney";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_TYPE = "type";

    private static final String TABLE_SAVING = "SavingAmount";
    private static final String COLUMN_SAVING_ID = "id";
    private static final String COLUMN_SAVING_AMOUNT = "amount";
    private static final String COLUMN_SAVING_DESCRIPTION = "description";
    private static final String COLUMN_SAVING_DATE = "date";
    private static final String COLUMN_SAVING_TYPE = "type"; // New column for type

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTableAddMoney = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_AMOUNT + " REAL, " + // Changed to REAL
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_TYPE + " TEXT)";
        db.execSQL(createTableAddMoney);

        String createTableSaving = "CREATE TABLE " + TABLE_SAVING + " (" +
                COLUMN_SAVING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SAVING_AMOUNT + " REAL, " + // Changed to REAL
                COLUMN_SAVING_DESCRIPTION + " TEXT, " +
                COLUMN_SAVING_DATE + " TEXT, " +
                COLUMN_SAVING_TYPE + " TEXT)";
        db.execSQL(createTableSaving);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAVING);
            onCreate(db);
        }
    }

    public boolean insertData(Transaction transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_AMOUNT, Double.parseDouble(transaction.getAmount())); // Ensuring numeric type
        contentValues.put(COLUMN_DESCRIPTION, transaction.getDescription());
        contentValues.put(COLUMN_DATE, transaction.getDate());
        contentValues.put(COLUMN_TYPE, transaction.getType());

        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    // Retrieve all transactions from the AddMoney table
    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID + " DESC", null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String amount = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION));
                    String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE));
                    String type = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE));

                    transactions.add(new Transaction(amount, description, date, type));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) cursor.close();
        }

        return transactions;
    }

    public double calculateBalance() {
        SQLiteDatabase db = this.getReadableDatabase();
        double balance = 0.0;

        String addMoneyQuery = "SELECT SUM(" + COLUMN_AMOUNT + ") AS TotalAdd FROM " + TABLE_NAME +
                " WHERE " + COLUMN_TYPE + " = 'addmoney'";
        Cursor addCursor = null;
        try {
            addCursor = db.rawQuery(addMoneyQuery, null);
            if (addCursor != null && addCursor.moveToFirst()) {
                balance += addCursor.getDouble(addCursor.getColumnIndexOrThrow("TotalAdd"));
            }
        } finally {
            if (addCursor != null) addCursor.close();
        }

        String expenseMoneyQuery = "SELECT SUM(" + COLUMN_AMOUNT + ") AS TotalExpense FROM " + TABLE_NAME +
                " WHERE " + COLUMN_TYPE + " = 'expensemoney'";
        Cursor expenseCursor = null;
        try {
            expenseCursor = db.rawQuery(expenseMoneyQuery, null);
            if (expenseCursor != null && expenseCursor.moveToFirst()) {
                balance -= expenseCursor.getDouble(expenseCursor.getColumnIndexOrThrow("TotalExpense"));
            }
        } finally {
            if (expenseCursor != null) expenseCursor.close();
        }

        return balance;
    }

    public boolean insertSaving(Transaction transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_SAVING_AMOUNT, Double.parseDouble(transaction.getAmount())); // Ensuring numeric type
        contentValues.put(COLUMN_SAVING_DESCRIPTION, transaction.getDescription());
        contentValues.put(COLUMN_SAVING_DATE, transaction.getDate());
        contentValues.put(COLUMN_SAVING_TYPE, transaction.getType());

        long result = db.insert(TABLE_SAVING, null, contentValues);
        return result != -1;
    }

    public List<Transaction> getAllSavings() {
        List<Transaction> savings = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_SAVING + " ORDER BY " + COLUMN_SAVING_ID + " DESC", null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String amount = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SAVING_AMOUNT));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SAVING_DESCRIPTION));
                    String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SAVING_DATE));
                    String type = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SAVING_TYPE));

                    savings.add(new Transaction(amount, description, date, type));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) cursor.close();
        }

        return savings;
    }

    public double calculateTotalSavings() {
        SQLiteDatabase db = this.getReadableDatabase();
        double totalSavings = 0.0;

        String query = "SELECT SUM(" + COLUMN_SAVING_AMOUNT + ") AS TotalSavings FROM " + TABLE_SAVING;
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst()) {
                totalSavings = cursor.getDouble(cursor.getColumnIndexOrThrow("TotalSavings"));
            }
        } finally {
            if (cursor != null) cursor.close();
        }

        return totalSavings;
    }
}
