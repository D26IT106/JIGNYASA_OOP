package model;

import exception.BankException;
import exception.InvalidAmountException;
import exception.InsufficientFundsException;
import model.annotation.Id;
import model.annotation.Positive;

import java.io.Serializable;

public abstract class Account implements Transactable, InterestBearing, Serializable, Comparable<Account>
{
    private static final long serialVersionUID = 1L;

    @Id
    private final String accountNumber;
    private String ownerName;
    @Positive
    protected long balance;
    private boolean active;

    private static long accountCounter = 0;

    private static String generateAccountNumber()
    {
        return String.format("AC%04d", ++accountCounter);
    }

    public Account(String ownerName, long openingBalance)
    {
        this.accountNumber = generateAccountNumber();
        this.ownerName = ownerName;
        this.balance = openingBalance;
        this.active = true;
    }

    public Account(String ownerName)
    {
        this(ownerName, 0);
    }

    private transient final Object lock = new Object();

    @Override
    public void deposit(long amount) throws InvalidAmountException
    {
        synchronized (lock)
        {
            if (amount <= 0)
            {
                throw new InvalidAmountException("Deposit amount must be positive");
            }
            balance += amount;
        }
    }

    @Override
    public void withdraw(long amount) throws InvalidAmountException, InsufficientFundsException
    {
        synchronized (lock)
        {
            if (amount <= 0)
            {
                throw new InvalidAmountException("Withdrawal amount must be positive");
            }
            if (!canWithdraw(amount))
            {
                long shortfall = amount - getBalance();
                throw new InsufficientFundsException("Insufficient funds", shortfall);
            }
            balance -= amount;
        }
    }

    // Transfer method using try-catch-finally
    public void transfer(Account to, long amount) throws BankException
    {
        try
        {
            this.withdraw(amount);
            to.deposit(amount);
        }
        catch (InvalidAmountException | InsufficientFundsException e)
        {
            throw new BankException("Transfer failed: " + e.getMessage());
        }
        finally
        {
            // Could log transfer attempt here
        }
    }

    // Safe transfer with deadlock avoidance: lock accounts in consistent order (by accountNumber)
    public static void transferSafely(Account from, Account to, long amount) throws BankException, InvalidAmountException, InsufficientFundsException
    {
        if (from == null || to == null)
        {
            throw new BankException("Accounts cannot be null");
        }
        if (from == to)
        {
            return;
        }

        // Determine lock order by accountNumber
        Account firstLock = from.getAccountNumber().compareTo(to.getAccountNumber()) < 0 ? from : to;
        Account secondLock = firstLock == from ? to : from;

        synchronized (firstLock.lock)
        {
            synchronized (secondLock.lock)
            {
                from.withdraw(amount);
                to.deposit(amount);
            }
        }
    }

    public abstract double interestRate();
    public abstract boolean canWithdraw(long amount);

    public String getAccountNumber()
    {
        return accountNumber;
    }

    public String getOwnerName()
    {
        return ownerName;
    }

    @Override
    public long getBalance()
    {
        return balance;
    }

    public boolean isActive()
    {
        return active;
    }

    @Override
    public String toString()
    {
        return "Account[" + accountNumber + ", Owner: " + ownerName + ", Balance: " + balance + "]";
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return accountNumber.equals(account.accountNumber);
    }

    @Override
    public int hashCode()
    {
        return accountNumber.hashCode();
    }

    @Override
    public int compareTo(Account other)
    {
        return this.accountNumber.compareTo(other.accountNumber);
    }
}