package com.tanjimif.cashkeeper.ViewModel;

public class Transaction {
    private String amount;
    private String description;
    private String date;
    private String type;

    public Transaction(String amount, String description, String date, String type) {
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }
}
