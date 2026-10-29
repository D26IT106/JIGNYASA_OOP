import java.nio.file.Path;
import model.Account;
import model.CurrentAccount;
import model.Customer;
import model.FixedDepositAccount;
import model.SavingsAccount;
import util.ReportGenerator;
import util.StatePersister;
import util.TransactionLog;

public class MiniBank {
    public static void main(String[] args) {

        Path stateFile = Path.of("data", "accounts.dat");
        Path logsFolder = Path.of("logs");
        Path transactionFile = logsFolder.resolve("transactions.log");
        Path reportFile = Path.of("reports", "end-of-day-report.txt");

        Account[] accounts = {
            new SavingsAccount("Rahul", 5000, 1000),
            new CurrentAccount("Priya", 3000, 2000),
            new FixedDepositAccount("Amit", 10000)
        };

        try {
            System.out.println("\n----- Append Transaction Log -----");
            TransactionLog.append(transactionFile, "DEPOSIT,"
                    + accounts[0].getAccountNumber() + ",500");
            accounts[0].deposit(500);
            System.out.println("Logged deposit: Rs. 500 for Rahul");

            TransactionLog.append(transactionFile, "WITHDRAW,"
                    + accounts[1].getAccountNumber() + ",200");
            accounts[1].withdraw(200);
            System.out.println("Logged withdrawal: Rs. 200 for Priya");

            TransactionLog.append(transactionFile, "DEPOSIT,"
                    + accounts[2].getAccountNumber() + ",1000");
            accounts[2].deposit(1000);
            System.out.println("Logged deposit: Rs. 1000 for Amit");

            System.out.println("\n----- Save Account State -----");
            StatePersister.save(accounts, stateFile);
            System.out.println("Accounts saved to: " + stateFile);

            System.out.println("\n----- Reload Saved Accounts -----");
            Account[] loadedAccounts = StatePersister.load(stateFile);
            for (Account account : loadedAccounts) {
                System.out.println(account);
                System.out.println("Type: "
                        + account.getClass().getSimpleName());
            }

            System.out.println("\n----- Customer Serialization Check -----");
            Customer customer = new Customer(
                    "Rahul", "rahul@example.com", "9876543210");
            customer.setAddress(new Customer.Address(
                    "12 Main Road", "Surat", "395001"));
            System.out.println("Serializable Customer: "
                    + customer.getName() + ", " + customer.getAddress());

            System.out.println("\n----- End-of-Day Report -----");
            String report = ReportGenerator.generate(logsFolder, reportFile);
            System.out.println(report);
            System.out.println("Report saved to: " + reportFile);

        } catch (Exception e) {
            System.out.println("MiniBank persistence/reporting error: "
                    + e.getMessage());
            e.printStackTrace();
        }
    }
}
