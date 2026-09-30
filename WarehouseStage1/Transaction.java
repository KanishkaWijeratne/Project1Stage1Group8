import java.io.*;
import java.lang.*;
import java.security.SecureRandom;
public class Transaction implements Serializable {
private static final long serialVersionUID = 1L;    
private SecureRandom rand = new SecureRandom();
private long transactionId = Math.abs(rand.nextLong());
private double amount;  
private String date;


    public Transaction(double amount) {
        this(null, amount);
    }

    public Transaction(String date, double amount) {
        this.date = date;
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }
                                                                                    
    public long getTransactionId() {
        return transactionId;
    }

    public double getAmount() {
        return amount;
    }
    public void Invoice() {
        System.out.println("Transaction ID: " + transactionId);
        System.out.println("Date: " + date);
        System.out.printf("Amount: %.2f%n", amount);
    }
    public String toString() {
        String string = "Transaction ID: " + transactionId + "\n" +
                        "Date: " + date + "\n" +
                        "Amount: " + amount;
        return string;
    }
}