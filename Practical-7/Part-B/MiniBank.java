import model.Account;
import model.Customer;
import model.CurrentAccount;
import model.FixedDepositAccount;
import model.SavingsAccount;

import service.WithdrawRule;
import service.Premium;

import util.CommandParser;
import util.StatementFormatter;
import util.AnnotationValidator;

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

        Scanner scanner = new Scanner(System.in);

        // ----------------------------------------
        // Bank information
        // ----------------------------------------

        BankInfo bank = new BankInfo(
                "MiniBank",
                "Main Branch"
        );

        System.out.println("================================");
        System.out.println("          " + bank.name());
        System.out.println("          " + bank.branch());
        System.out.println("================================");

        // ----------------------------------------
        // Create different account types
        // ----------------------------------------

        SavingsAccount savings =
                new SavingsAccount(
                        "Rahul",
                        5000,
                        1000
                );

        CurrentAccount current =
                new CurrentAccount(
                        "Priya",
                        3000,
                        2000
                );

        FixedDepositAccount fixedDeposit =
                new FixedDepositAccount(
                        "Amit",
                        10000
                );

        // ----------------------------------------
        // Store all account types in Account[]
        // ----------------------------------------

        Account[] accounts = {
                savings,
                current,
                fixedDeposit
        };

        // ----------------------------------------
        // Runtime polymorphism
        // ----------------------------------------

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

            System.out.println("Account Type   : "
                    + account.getClass().getSimpleName());
        }

        // ----------------------------------------
        // Test withdrawals
        // ----------------------------------------

        System.out.println("\n----- Withdrawal Tests -----");

        boolean savingsResult =
                savings.withdraw(3000);

        System.out.println(
                "Savings withdrawal of Rs.3000: "
                        + savingsResult
        );

        boolean currentResult =
                current.withdraw(4500);

        System.out.println(
                "Current withdrawal of Rs.4500: "
                        + currentResult
        );

        boolean fixedResult =
                fixedDeposit.withdraw(1000);

        System.out.println(
                "Fixed Deposit withdrawal of Rs.1000: "
                        + fixedResult
        );

        // ----------------------------------------
        // Pattern instanceof check
        // ----------------------------------------

        System.out.println("\n----- instanceof Check -----");

        for (Account account : accounts) {

            if (account instanceof SavingsAccount savingsAccount) {

                System.out.println(
                        savingsAccount.getOwnerName()
                                + " is a Savings Account."
                );

                System.out.println(
                        "Minimum Balance: Rs. "
                                + savingsAccount.getMinBalance()
                );
            }
        }


        // ----------------------------------------
        // Practical 7: Annotation Validation
        // ----------------------------------------

        System.out.println("\n----- Annotation Validation -----");

        // A negative balance is intentionally used to test @Positive.
        SavingsAccount invalidAccount =
                new SavingsAccount("TestUser", -500, 0);

        String[] errors = AnnotationValidator.validate(invalidAccount);

        if (errors.length == 0) {
            System.out.println("Account is valid.");
        } else {
            System.out.println("Validation Errors:");
            for (String error : errors) {
                System.out.println("- " + error);
            }
        }

        scanner.close();
    }
}

