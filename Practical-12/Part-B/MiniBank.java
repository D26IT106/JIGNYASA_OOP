import java.util.ArrayList;
import model.Account;
import model.AccountStore;
import model.CurrentAccount;
import model.FixedDepositAccount;
import model.SavingsAccount;

public class MiniBank {
    public static void main(String[] args) {

        AccountStore store = new AccountStore();
        Account rahul = new SavingsAccount("Rahul", 5000, 1000);
        Account priya = new CurrentAccount("Priya", 3000, 2000);
        Account amit = new FixedDepositAccount("Amit", 10000);
        Account neha = new SavingsAccount("Neha", 7000, 1000);

        store.add(rahul);
        store.add(priya);
        store.add(amit);
        store.add(neha);

        System.out.println("\n----- Accounts Sorted by Balance -----");
        for (Account account : store.sortedByBalance()) {
            System.out.println(account.getAccountNumber() + " | "
                    + account.getOwnerName() + " | Rs. "
                    + account.getBalance());
        }

        System.out.println("\n----- Search by Account Number -----");
        Account found = store.get(priya.getAccountNumber());
        System.out.println(found == null ? "Account not found." : found);

        System.out.println("\n----- Comparable by Account Number -----");
        ArrayList<Account> byNumber = store.getAll();
        byNumber.sort(null);
        for (Account account : byNumber) {
            System.out.println(account.getAccountNumber() + " - "
                    + account.getOwnerName());
        }

        System.out.println("\n----- Transaction History -----");
        for (String entry : store.getTransactionHistory()) {
            System.out.println(entry);
        }

        System.out.println("\n----- ConcurrentHashMap -----");
        TransactionProcessor processor = new TransactionProcessor();
        for (Account account : store.getAll()) {
            processor.registerAccount(account);
        }
        System.out.println("Thread-safe shared accounts: "
                + processor.getSharedAccounts().size());
        System.out.println("Retrieved account: "
                + processor.getAccount("AC0001").getOwnerName());
        processor.stop();

    }
}
