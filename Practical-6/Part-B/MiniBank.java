import model.Account;
import model.Customer;
import model.CurrentAccount;
import model.FixedDepositAccount;
import model.SavingsAccount;

import service.WithdrawRule;
import service.Premium;

import util.CommandParser;
import util.StatementFormatter;

import static util.Validator.isValidMobile;
import java.util.Scanner;

public class MiniBank {

    // Record from Practical 1
    record BankInfo(String name, String branch) {
    }

    // Enum from Practical 1
    enum MenuOption {
        OPEN_ACCOUNT,
        DEPOSIT,
        WITHDRAW,
        TRANSFER,
        EXIT
    }

    public static void main(String[] args) {

    BankInfo bank = new BankInfo("MiniBank", "Main Branch");

    System.out.println("================================");
    System.out.println("          " + bank.name());
    System.out.println("          " + bank.branch());
    System.out.println("================================");

    // Create accounts
    SavingsAccount savings =
            new SavingsAccount("Rahul", 5000, 1000);

    CurrentAccount current =
            new CurrentAccount("Priya", 3000, 2000);

    FixedDepositAccount fixedDeposit =
            new FixedDepositAccount("Amit", 10000);

    Account[] accounts = {savings, current, fixedDeposit};

    // 1. Runtime polymorphism and yearly interest
    System.out.println("\n----- Account Details -----");

    for (Account account : accounts) {
        System.out.println("\nAccount Number : "
                + account.getAccountNumber());
        System.out.println("Owner Name     : "
                + account.getOwnerName());
        System.out.println("Balance        : Rs. "
                + account.getBalance());
        System.out.println("Interest Rate  : "
                + account.interestRate() + "%");
        System.out.println("Yearly Interest: Rs. "
                + account.yearlyInterest());
        System.out.println("Account Type   : "
                + account.getClass().getSimpleName());
    }

    // 2. Transactable interface
    System.out.println("\n----- Transactable Interface -----");

    savings.deposit(500);
    System.out.println("Rahul's balance after deposit: Rs. "
            + savings.getBalance());

    // 3. WithdrawRule using anonymous class
    System.out.println("\n----- WithdrawRule: Anonymous Class -----");

    WithdrawRule anonymousRule = new WithdrawRule() {
        @Override
        public boolean allow(Account account, long amount) {
            return account.canWithdraw(amount);
        }
    };

    System.out.println("Can Rahul withdraw Rs. 1000? "
            + anonymousRule.allow(savings, 1000));

    // 4. WithdrawRule using lambda
    System.out.println("\n----- WithdrawRule: Lambda Expression -----");

    WithdrawRule lambdaRule =
            (account, amount) -> account.canWithdraw(amount);

    System.out.println("Can Priya withdraw Rs. 4500? "
            + lambdaRule.allow(current, 4500));

    // 5. Withdrawal tests
    System.out.println("\n----- Withdrawal Tests -----");

    System.out.println("Savings withdrawal of Rs. 3000: "
            + savings.withdraw(3000));

    System.out.println("Current withdrawal of Rs. 4500: "
            + current.withdraw(4500));

    System.out.println("Fixed Deposit withdrawal of Rs. 1000: "
            + fixedDeposit.withdraw(1000));

    // 6. Premium marker interface
    System.out.println("\n----- Premium Marker Interface -----");

    if (fixedDeposit instanceof Premium) {
        System.out.println("Amit's account implements Premium.");
    }

    // 7. Static import demonstration
    System.out.println("\n----- Static Import: Mobile Validation -----");

    System.out.println("9876543210 -> "
            + isValidMobile("9876543210"));

    System.out.println("12345 -> "
            + isValidMobile("12345"));

    System.out.println("\nMiniBank executed successfully.");
}
}

