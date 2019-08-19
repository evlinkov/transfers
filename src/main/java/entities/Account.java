package entities;

public class Account {

    private double amount;

    private int userIdentifier;

    public Account(double amount, int userIdentifier) {
        this.amount = amount;
        this.userIdentifier = userIdentifier;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double changeAmount(double add) {
        this.amount += add;
        return this.amount;
    }

    public int getUserIdentifier() {
        return this.userIdentifier;
    }
}
