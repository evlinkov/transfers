package entities;

import com.google.gson.annotations.SerializedName;

public class TransferRequest {

    @SerializedName("user_id")
    private int userIdentifier;

    @SerializedName("amount")
    private double amount;

    public int getUserIdentifier() {
        return this.userIdentifier;
    }

    public void setUserIdentifier(int userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public TransferRequest() {

    }
}
