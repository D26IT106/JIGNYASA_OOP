package model;

import exception.BankException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;

import model.annotation.Id;
import model.annotation.Positive;

import java.util.Objects;
import java.io.Serializable;

import service.InterestBearing;
import service.Transactable;

public abstract class Account
        implements Transactable, InterestBearing, Serializable, Comparable<Account> {

    private static final long serialVersionUID = 1L;

    @Id
    private final String accountNumber;

    private String ownerName;

    @Positive
    private long balance;

    private boolean active;

    private static long accountCounter = 0;

    // Practical 8
    private static boolean synchronizedMode = false;

    private static String generateAccountNumber() {

        accountCounter++;

        return String.format(
                "AC%04d",
                accountCounter
        );
    }

    public Account(
            String ownerName,
            long openingBalance) {

        this.accountNumber =
                generateAccountNumber();

        this.ownerName = ownerName;
        this.balance = openingBalance;
        this.active = true;
    }

    public Account(String ownerName) {
        this(ownerName, 0);
    }

    @Override
    public void deposit(long amount)
            throws InvalidAmountException {

        if (amount <= 0) {

            throw new InvalidAmountException(
                    "Deposit amount must be greater than zero."
            );
        }

        if (synchronizedMode) {

            synchronized (this) {
                performDeposit(amount);
            }

        } else {

            performDeposit(amount);
        }
    }

    private void performDeposit(long amount) {

        long currentBalance = balance;

        // Creates a race condition for Practical 8
        Thread.yield();

        balance = currentBalance + amount;
    }

    @Override
    public boolean withdraw(long amount)
            throws InsufficientFundsException,
            InvalidAmountException {

        if (amount <= 0) {

            throw new InvalidAmountException(
                    "Withdrawal amount must be greater than zero."
            );
        }

        if (synchronizedMode) {

            synchronized (this) {
                return performWithdraw(amount);
            }

        } else {

            return performWithdraw(amount);
        }
    }

    private boolean performWithdraw(long amount)
            throws InsufficientFundsException {

        if (!canWithdraw(amount)) {

            long shortfall =
                    amount - balance;

            if (shortfall < 0) {
                shortfall = 0;
            }

            throw new InsufficientFundsException(
                    "Insufficient funds for withdrawal.",
                    shortfall
            );
        }

        long currentBalance = balance;

        // Creates a race condition for Practical 8
        Thread.yield();

        balance = currentBalance - amount;

        return true;
    }

    public void transfer(
            Account to,
            long amount)
            throws BankException {

        try {

            if (to == null) {

                throw new BankException(
                        "Destination account does not exist."
                );
            }

            withdraw(amount);

            to.deposit(amount);

        }
        catch (InsufficientFundsException |
               InvalidAmountException e) {

            throw e;
        }
        finally {

            System.out.println(
                    "Transfer operation completed."
            );
        }
    }

    public abstract double interestRate();

    public abstract boolean canWithdraw(long amount);

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public synchronized long getBalance() {
        return balance;
    }

    public boolean isActive() {
        return active;
    }


    @Override
    public int compareTo(Account other) {
        return this.accountNumber.compareTo(other.accountNumber);
    }

    @Override
    public String toString() {

        return "Account{" +
                "accountNumber='" +
                accountNumber + '\'' +
                ", ownerName='" +
                ownerName + '\'' +
                ", balance=" +
                balance +
                '}';
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;

        if (!(o instanceof Account))
            return false;

        Account account =
                (Account) o;

        return accountNumber.equals(
                account.accountNumber
        );
    }

    @Override
    public int hashCode() {

        return Objects.hash(
                accountNumber
        );
    }

    // Practical 8
    public static void setSynchronizedMode(
            boolean value) {

        synchronizedMode = value;
    }
}