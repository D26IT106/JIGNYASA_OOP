import model.Account;
import model.AnnotationValidator;
import model.CurrentAccount;
import model.FixedDepositAccount;
import model.Premium;
import model.Repository;
import model.SavingsAccount;
import model.WithdrawRule;
import service.BankService;
import service.ReportGenerator;
import service.StatePersister;
import service.TransactionLog;
import service.TransactionProcessor;
import util.Command;
import util.CommandParser;
import util.StatementFormatter;
import util.TransactionType;
import util.Validator;
import exception.AccountNotFoundException;
import exception.BankException;
import exception.InvalidAmountException;
import exception.InsufficientFundsException;
import exception.TransactionLogger;

import static model.WithdrawRule.*;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class MiniBank
{
    private static final Path DATA_FILE = Paths.get("accounts.dat");
    private static final Path LOG_DIR = Paths.get("logs");
    private static final Path REPORT_FILE = Paths.get("report.txt");

    public static void main(String[] args)
    {
        runAllTests();
        runMenu();
    }

    private static void runAllTests()
    {
        // 1. Test Validators
        System.out.println("=== Testing Validators ===");
        System.out.println("Mobile (9876543210): " + Validator.isValidMobile("9876543210"));
        System.out.println("Mobile (12345): " + Validator.isValidMobile("12345"));

        System.out.println("Email (test@test.com): " + Validator.isValidEmail("test@test.com"));
        System.out.println("Email (invalid-email): " + Validator.isValidEmail("invalid-email"));

        System.out.println("PAN (ABCDE1234F): " + Validator.isValidPan("ABCDE1234F"));
        System.out.println("PAN (12345): " + Validator.isValidPan("12345"));

        System.out.println("IFSC (SBIN0001234): " + Validator.isValidIfsc("SBIN0001234"));
        System.out.println("IFSC (INVALID): " + Validator.isValidIfsc("INVALID"));

        System.out.println("Name (John Doe): " + Validator.isValidName("John Doe"));
        System.out.println("Name (J): " + Validator.isValidName("J"));

        // 2. Test Command Parser
        System.out.println("\n=== Testing Command Parser ===");
        String input = "DEPOSIT AC0001 500";
        System.out.println("Input: " + input);
        Command cmd = CommandParser.parse(input);
        if (cmd != null)
        {
            System.out.println("Type: " + cmd.type());
            System.out.println("Account: " + cmd.accountNumber());
            System.out.println("Amount: " + cmd.amount());
        }

        // 3. Test BankService with polymorphism
        System.out.println("\n=== Banking Operations ===");
        BankService service = new BankService();

        SavingsAccount savings;
        CurrentAccount current;
        FixedDepositAccount fd;
        try
        {
            savings = service.openSavingsAccount("Alice", 5000, 500);
            current = service.openCurrentAccount("Bob", 2000, 1000);
            fd = service.openFixedDepositAccount("Charlie", 10000);
        }
        catch (InvalidAmountException e)
        {
            System.out.println("Error creating test accounts: " + e.getMessage());
            return;
        }

        Account[] accounts = { savings, current, fd };

        System.out.println("--- Interest Rates ---");
        for (Account acc : accounts)
        {
            System.out.println(acc.getOwnerName() + " (" + acc.getClass().getSimpleName() + "): " + acc.interestRate() + "%");
        }

        // 4. Test WithdrawRule - Anonymous Class
        System.out.println("\n--- WithdrawRule (Anonymous Class) ---");
        WithdrawRule ruleAnon = new WithdrawRule()
        {
            @Override
            public boolean allow(Account account, long amount)
            {
                return account.canWithdraw(amount) && amount <= 5000;
            }
        };
        System.out.println("Rule allows 3000 from Savings: " + ruleAnon.allow(savings, 3000));

        // 5. Test WithdrawRule - Lambda Expression
        System.out.println("\n--- WithdrawRule (Lambda) ---");
        WithdrawRule ruleLambda = (account, amount) -> account.canWithdraw(amount) && amount % 100 == 0;
        System.out.println("Rule allows 2000 from Current: " + ruleLambda.allow(current, 2000));

        // 6. Test Premium marker interface
        System.out.println("\n--- Premium Accounts ---");
        for (Account acc : accounts)
        {
            if (acc instanceof Premium)
            {
                System.out.println(acc.getOwnerName() + " has a Premium Fixed Deposit Account");
            }
        }

        // 7. Test StatementFormatter
        System.out.println("\n--- Account Statements ---");
        for (Account acc : accounts)
        {
            System.out.println(StatementFormatter.buildStatement(acc));
            System.out.println();
        }

        // 8. Test AnnotationValidator with negative balance
        System.out.println("\n--- Annotation Validation ---");
        try
        {
            Constructor<?> ctor = SavingsAccount.class.getDeclaredConstructor(String.class, long.class, long.class);
            ctor.setAccessible(true);
            SavingsAccount badAccount = (SavingsAccount) ctor.newInstance("Test", -100, 500);
            String[] errors = AnnotationValidator.validate(badAccount);
            for (String err : errors)
            {
                System.out.println("Validation Error: " + err);
            }
        }
        catch (Exception e)
        {
            System.out.println("Error creating test account: " + e.getMessage());
        }

        // 9. Test Exception Handling
        System.out.println("\n--- Exception Handling Tests ---");

        try
        {
            savings.deposit(-100);
        }
        catch (InvalidAmountException e)
        {
            System.out.println("Caught: " + e.getMessage());
        }

        try
        {
            savings.withdraw(0);
        }
        catch (InvalidAmountException | InsufficientFundsException e)
        {
            System.out.println("Caught: " + e.getMessage());
        }

        try
        {
            savings.withdraw(10000);
        }
        catch (InvalidAmountException | InsufficientFundsException e)
        {
            if (e instanceof InsufficientFundsException)
            {
                System.out.println("Caught: " + e.getMessage() + " (Shortfall: " + ((InsufficientFundsException) e).getShortfall() + ")");
            }
            else
            {
                System.out.println("Caught: " + e.getMessage());
            }
        }

        // Test transfer with try-catch-finally
        System.out.println("\n--- Transfer Test ---");
        try
        {
            savings.transfer(current, 2000);
            System.out.println("Transfer successful");
            System.out.println("Savings balance: " + savings.getBalance());
            System.out.println("Current balance: " + current.getBalance());
        }
        catch (BankException e)
        {
            System.out.println("Transfer failed: " + e.getMessage());
        }
        finally
        {
            System.out.println("Finally block executed after transfer attempt");
        }

        // Test try-with-resources
        System.out.println("\n--- Try-with-resources Test ---");
        try (TransactionLogger logger = new TransactionLogger("Deposit Operation"))
        {
            savings.deposit(500);
            System.out.println("Deposited 500, new balance: " + savings.getBalance());
        }
        catch (InvalidAmountException e)
        {
            System.out.println("Error: " + e.getMessage());
        }

        // 10. Thread Safety Test
        System.out.println("\n--- Thread Safety Test (Race Condition) ---");
        SavingsAccount sharedAccount = new SavingsAccount("Shared", 0, 0);
        Thread[] threads = new Thread[10];
        for (int i = 0; i < threads.length; i++)
        {
            threads[i] = new Thread(new model.AccountWorker(sharedAccount, 1000, 10), "Worker-" + i);
            threads[i].start();
        }
        for (Thread t : threads)
        {
            try
            {
                t.join();
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("Expected balance: " + (10 * 1000 * 10));
        System.out.println("Actual balance:   " + sharedAccount.getBalance());

        // 11. Persistence Test
        System.out.println("\n--- Persistence Test ---");
        try
        {
            Files.createDirectories(LOG_DIR);

            Account[] originalAccounts = { savings, current, fd };
            StatePersister.save(originalAccounts, DATA_FILE);
            System.out.println("Saved " + originalAccounts.length + " accounts to " + DATA_FILE);

            TransactionLog.append(LOG_DIR.resolve("transactions.log"), "DEPOSIT 500");
            TransactionLog.append(LOG_DIR.resolve("transactions.log"), "WITHDRAW 200");
            TransactionLog.append(LOG_DIR.resolve("transactions.log"), "DEPOSIT 1000");

            Account[] loadedAccounts = StatePersister.load(DATA_FILE);
            System.out.println("Loaded " + loadedAccounts.length + " accounts:");
            for (Account acc : loadedAccounts)
            {
                System.out.println("  " + acc);
            }

            ReportGenerator.generateReport(LOG_DIR, REPORT_FILE);
            System.out.println("Report generated at " + REPORT_FILE);
        }
        catch (Exception e)
        {
            System.err.println("Persistence test error: " + e.getMessage());
        }

        // 12. Collections Framework Test
        System.out.println("\n--- AccountStore Test (Collections) ---");
        model.AccountStore store = new model.AccountStore();
        SavingsAccount sa1 = new SavingsAccount("David", 3000, 500);
        CurrentAccount ca1 = new CurrentAccount("Eve", 5000, 2000);
        FixedDepositAccount fd1 = new FixedDepositAccount("Frank", 20000);

        store.add(sa1);
        store.add(ca1);
        store.add(fd1);

        System.out.println("Accounts sorted by balance:");
        List<Account> sorted = store.sortedByBalance();
        for (Account acc : sorted)
        {
            System.out.println("  " + acc);
        }

        // 13. Generic Repository Test
        System.out.println("\n--- Repository Test (Generic) ---");
        Repository<Account> repo = new Repository<>();
        SavingsAccount sa2 = new SavingsAccount("Grace", 4000, 500);
        CurrentAccount ca2 = new CurrentAccount("Heidi", 6000, 2000);
        FixedDepositAccount fd2 = new FixedDepositAccount("Ivan", 30000);

        repo.saveAll(sa2, ca2, fd2);
        System.out.println("Saved 3 accounts via saveAll");

        Account fetched = repo.findById(sa2.getAccountNumber());
        System.out.println("Fetched by ID: " + fetched);

        System.out.println("All accounts in repository:");
        for (Account acc : repo.findAll())
        {
            System.out.println("  " + acc);
        }

        Double[] balances = {1000.0, 2000.0, 3000.0};
        System.out.println("Average balance: " + Repository.average(balances));
    }

    private static void runMenu()
    {
        BankService service = new BankService();
        TransactionProcessor processor = new TransactionProcessor();

        // Load saved accounts on startup
        try
        {
            if (Files.exists(DATA_FILE))
            {
                Account[] loaded = StatePersister.load(DATA_FILE);
                for (Account acc : loaded)
                {
                    service.getRepository().save(acc);
                    processor.addAccount(acc);
                }
                System.out.println("\nLoaded " + loaded.length + " accounts from storage.");
            }
        }
        catch (Exception e)
        {
            System.err.println("Error loading accounts: " + e.getMessage());
        }

        // Add existing accounts from tests to processor
        for (Account acc : service.listAccounts())
        {
            processor.addAccount(acc);
        }

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running)
        {
            printMenu();
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine().trim();

            try
            {
                switch (choice)
                {
                    case "1" -> openAccountMenu(service, scanner);
                    case "2" -> depositMenu(service, scanner);
                    case "3" -> withdrawMenu(service, scanner);
                    case "4" -> transferMenu(service, scanner);
                    case "5" -> statementMenu(service, scanner);
                    case "6" -> listAccountsMenu(service);
                    case "7" -> batchProcessMenu(processor, scanner);
                    case "8" -> generateReportMenu();
                    case "9" ->
                    {
                        // Save and exit
                        saveAccounts(service);
                        processor.stop();
                        System.out.println("Accounts saved. Goodbye!");
                        running = false;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            }
            catch (Exception e)
            {
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
    }

    private static void printMenu()
    {
        System.out.println("\n=== MiniBank Menu ===");
        System.out.println("1. Open Account");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Transfer");
        System.out.println("5. Statement");
        System.out.println("6. List Accounts");
        System.out.println("7. Batch Process Transactions");
        System.out.println("8. Generate Report");
        System.out.println("9. Save & Exit");
    }

    private static void openAccountMenu(BankService service, Scanner scanner)
    {
        System.out.println("\n--- Open Account ---");
        System.out.println("1. Savings Account");
        System.out.println("2. Current Account");
        System.out.println("3. Fixed Deposit Account");
        System.out.print("Choose type: ");
        String type = scanner.nextLine().trim();

        System.out.print("Owner Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Opening Balance: ");
        long balance = Long.parseLong(scanner.nextLine().trim());

        try
        {
            switch (type)
            {
                case "1" ->
                {
                    System.out.print("Minimum Balance: ");
                    long minBal = Long.parseLong(scanner.nextLine().trim());
                    SavingsAccount acc = service.openSavingsAccount(name, balance, minBal);
                    System.out.println("Account created: " + acc.getAccountNumber());
                }
                case "2" ->
                {
                    System.out.print("Overdraft Limit: ");
                    long overdraft = Long.parseLong(scanner.nextLine().trim());
                    CurrentAccount acc = service.openCurrentAccount(name, balance, overdraft);
                    System.out.println("Account created: " + acc.getAccountNumber());
                }
                case "3" ->
                {
                    FixedDepositAccount acc = service.openFixedDepositAccount(name, balance);
                    System.out.println("Account created: " + acc.getAccountNumber());
                }
                default -> System.out.println("Invalid account type.");
            }
        }
        catch (Exception e)
        {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void depositMenu(BankService service, Scanner scanner)
    {
        System.out.print("Account Number: ");
        String accNum = scanner.nextLine().trim();
        System.out.print("Amount: ");
        long amount = Long.parseLong(scanner.nextLine().trim());

        try
        {
            service.deposit(accNum, amount);
            System.out.println("Deposit successful.");
        }
        catch (Exception e)
        {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void withdrawMenu(BankService service, Scanner scanner)
    {
        System.out.print("Account Number: ");
        String accNum = scanner.nextLine().trim();
        System.out.print("Amount: ");
        long amount = Long.parseLong(scanner.nextLine().trim());

        try
        {
            service.withdraw(accNum, amount);
            System.out.println("Withdrawal successful.");
        }
        catch (Exception e)
        {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void transferMenu(BankService service, Scanner scanner)
    {
        System.out.print("From Account: ");
        String from = scanner.nextLine().trim();
        System.out.print("To Account: ");
        String to = scanner.nextLine().trim();
        System.out.print("Amount: ");
        long amount = Long.parseLong(scanner.nextLine().trim());

        try
        {
            service.transfer(from, to, amount);
            System.out.println("Transfer successful.");
        }
        catch (Exception e)
        {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void statementMenu(BankService service, Scanner scanner)
    {
        System.out.print("Account Number: ");
        String accNum = scanner.nextLine().trim();

        try
        {
            String stmt = service.statement(accNum);
            System.out.println(stmt);
        }
        catch (Exception e)
        {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void listAccountsMenu(BankService service)
    {
        System.out.println("\n--- All Accounts ---");
        List<Account> accounts = service.listAccounts();
        if (accounts.isEmpty())
        {
            System.out.println("No accounts found.");
        }
        else
        {
            for (Account acc : accounts)
            {
                System.out.println("  " + acc);
            }
        }
    }

    private static void batchProcessMenu(TransactionProcessor processor, Scanner scanner)
    {
        System.out.println("\n--- Batch Process Transactions ---");
        System.out.println("Enter transactions (one per line, format: TYPE ACCOUNT AMOUNT)");
        System.out.println("Example: DEPOSIT AC0001 500");
        System.out.println("Type 'done' to finish and process batch.");

        List<String> batch = new java.util.ArrayList<>();
        while (true)
        {
            System.out.print("> ");
            String line = scanner.nextLine().trim();
            if (line.equalsIgnoreCase("done"))
            {
                break;
            }
            if (!line.isEmpty())
            {
                batch.add(line);
            }
        }

        if (batch.isEmpty())
        {
            System.out.println("No transactions to process.");
            return;
        }

        try
        {
            for (String txn : batch)
            {
                Command cmd = CommandParser.parse(txn);
                if (cmd == null)
                {
                    System.out.println("Skipping invalid: " + txn);
                    continue;
                }

                processor.submit(() ->
                {
                    try
                    {
                        switch (cmd.type())
                        {
                            case DEPOSIT ->
                            {
                                Account acc = processor.getAccount(cmd.accountNumber());
                                if (acc != null) acc.deposit(cmd.amount());
                            }
                            case WITHDRAW ->
                            {
                                Account acc = processor.getAccount(cmd.accountNumber());
                                if (acc != null) acc.withdraw(cmd.amount());
                            }
                            case TRANSFER ->
                            {
                                // Transfer requires special handling with two accounts
                                // For batch, we'll skip or handle separately
                            }
                        }
                        TransactionLog.append(LOG_DIR.resolve("transactions.log"), txn);
                    }
                    catch (Exception e)
                    {
                        System.err.println("Transaction failed: " + txn + " - " + e.getMessage());
                    }
                });
            }

            processor.stop();
            System.out.println("Batch processing complete.");
        }
        catch (Exception e)
        {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void generateReportMenu()
    {
        try
        {
            ReportGenerator.generateReport(LOG_DIR, REPORT_FILE);
            System.out.println("Report generated at " + REPORT_FILE);
        }
        catch (Exception e)
        {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void saveAccounts(BankService service)
    {
        try
        {
            Account[] accounts = service.listAccounts().toArray(new Account[0]);
            StatePersister.save(accounts, DATA_FILE);
            System.out.println("Saved " + accounts.length + " accounts.");
        }
        catch (Exception e)
        {
            System.err.println("Error saving accounts: " + e.getMessage());
        }
    }
}