import exception.AccountNotFoundException;
import exception.BankException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.Account;
import model.CurrentAccount;
import model.FixedDepositAccount;
import model.SavingsAccount;

public class MiniBank {

    public static void main(String[] args) {

        System.out.println("================================");
        System.out.println("          MiniBank");
        System.out.println("          Main Branch");
        System.out.println("================================");

        Account rahul = new SavingsAccount("Rahul", 5000, 1000);
        Account priya = new CurrentAccount("Priya", 3000, 2000);
        Account amit = new FixedDepositAccount("Amit", 10000);

        System.out.println("\n----- Account Details -----");
        printAccount(rahul);
        printAccount(priya);
        printAccount(amit);

        System.out.println("\n----- Invalid Deposit Test -----");
        try {
            rahul.deposit(-500);
            System.out.println("Deposit successful.");
        } catch (InvalidAmountException e) {
            System.out.println("Deposit failed: " + e.getMessage());
        } finally {
            System.out.println("Deposit operation finished.");
        }

        System.out.println("\n----- Insufficient Funds Test -----");
        try {
            rahul.withdraw(10000);
            System.out.println("Withdrawal successful.");
        } catch (InsufficientFundsException e) {
            System.out.println("Withdrawal failed: " + e.getMessage());
            System.out.println("Shortfall: Rs. " + e.getShortfall());
        } catch (InvalidAmountException e) {
            System.out.println("Invalid withdrawal: " + e.getMessage());
        } finally {
            System.out.println("Withdrawal operation finished.");
        }

        System.out.println("\n----- Invalid Withdrawal Test -----");
        try {
            priya.withdraw(0);
            System.out.println("Withdrawal successful.");
        } catch (InvalidAmountException e) {
            System.out.println("Withdrawal failed: " + e.getMessage());
        } catch (InsufficientFundsException e) {
            System.out.println("Withdrawal failed: " + e.getMessage());
        } finally {
            System.out.println("Invalid withdrawal test finished.");
        }

        System.out.println("\n----- Successful Withdrawal -----");
        try {
            rahul.withdraw(1000);
            System.out.println("Withdrawal successful.");
            System.out.println("Rahul's balance: Rs. " + rahul.getBalance());
        } catch (BankException e) {
            System.out.println("Transaction failed: " + e.getMessage());
        } finally {
            System.out.println("Transaction attempt completed.");
        }

        System.out.println("\n----- Transfer Test -----");
        try {
            rahul.transfer(priya, 500);
            System.out.println("Transfer successful.");
            System.out.println("Rahul's balance: Rs. " + rahul.getBalance());
            System.out.println("Priya's balance: Rs. " + priya.getBalance());
        } catch (AccountNotFoundException e) {
            System.out.println("Transfer failed: " + e.getMessage());
        } catch (BankException e) {
            System.out.println("Transfer failed: " + e.getMessage());
        } finally {
            System.out.println("Transfer test completed.");
        }

        System.out.println("\n----- Account Not Found Test -----");
        try {
            rahul.transfer(null, 100);
        } catch (BankException e) {
            System.out.println("Transfer failed: " + e.getMessage());
        } finally {
            System.out.println("Missing-account test completed.");
        }

        System.out.println("\n----- Try-with-Resources -----");
        try (BankResource resource = new BankResource()) {
            resource.use();
        } catch (Exception e) {
            System.out.println("Resource error: " + e.getMessage());
        }

    }

    private static void printAccount(Account account) {
        System.out.println("\nAccount Number : " + account.getAccountNumber());
        System.out.println("Owner Name     : " + account.getOwnerName());
        System.out.println("Balance        : Rs. " + account.getBalance());
        System.out.println("Interest Rate  : " + account.interestRate() + "%");
    }
}
