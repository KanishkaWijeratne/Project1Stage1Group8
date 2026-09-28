public class Transaction_Test {
  public static void main(String[] args) {
    Transaction transaction1 = new Transaction("2020-06-01", 100.00);
    Transaction transaction2 = new Transaction("1995-10-12", 250.00);
    
    
    //transaction1.Invoice();
    // This should print the transaction ID, date, and amount for transaction1
    System.out.println("Expected output of transaction1's Invoice(): \n Transaction ID: <some_long_number> \n Date: 2020-06-01 \n Amount: 100.00 \n");
    System.out.println("Actual output of transaction1's Invoice(): \n" + transaction1.toString());
    
    //transaction1.getTransactionId(); //This should return the transaction ID for transaction1 by itself
    System.out.println("\n Expected output of transaction1's getTransactionId(): <some_long_number> \n");
    System.out.println("Actual output of transaction1's getTransactionId(): " + transaction1.getTransactionId() + "\n");
    
    //transaction1.getDate(); //This should return the date for transaction1 by itself
    System.out.println("\nExpected output of transaction1's getDate(): 2020-06-01 \n");
    System.out.println("Actual output of transaction1's getDate(): " +transaction1.getDate() + "\n");
    
    //transaction1.getAmount(); //This should return the amount for transaction1 by itself
    System.out.println("Expected output of transaction1's getAmount(): 100.00 \n");
    System.out.println("Actual output of transaction1's getAmount(): " + transaction1.getAmount() + "\n");

    System.out.println(); // Just to add a blank line between the two transactions for clarity
    
    //transaction2.Invoice();
    // This should print the transaction ID, date, and amount for transaction2
    System.out.println("Expected output of transaction2's Invoice(): \n Transaction ID: <some_long_number> \nDate: 1995-10-12 \n Amount: 250.00 \n");
    System.out.println("Actual output of transaction2's Invoice(): \n" + transaction2.toString());
    
    //transaction2.getTransactionId(); //This should return the transaction ID for transaction2 by itself
    System.out.println("\nExpected output of transaction2's getTransactionId(): <some_long_number> \n");
    System.out.println("Actual output of transaction2's getTransactionId():" + transaction2.getTransactionId() + "\n");
    
    //transaction2.getDate(); //This should return the date for transaction2 by itself
    System.out.println("Expected output of transaction2's getDate(): 1995-10-12" + "\n");
    System.out.println("Actual output of transaction2's getDate(): "+ transaction2.getDate() + "\n");

    //transaction2.getAmount(); //This should return the amount for transaction2 by itself
    System.out.println("Expected output of transaction2's getAmount(): 250.00" + "\n");
    System.out.println("Actual output of transaction2's getAmount():" + transaction2.getAmount() + "\n");

}
}
