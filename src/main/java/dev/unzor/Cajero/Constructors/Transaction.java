package dev.unzor.Cajero.Constructors;

public class Transaction {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getRemainingBalance() {
        return remainingBalance;
    }

    public void setRemainingBalance(double remainingBalance) {
        this.remainingBalance = remainingBalance;
    }

    private int id;
    private int userId;
    private String datetime;
    private String type;
    private double amount;
    private double remainingBalance;

    public Transaction(int id, int userId, String datetime, String type, double amount, double remainingBalance) {
        this.id = id;
        this.userId = userId;
        this.datetime = datetime;
        this.type = type;
        this.amount = amount;
        this.remainingBalance = remainingBalance;
    }


}
