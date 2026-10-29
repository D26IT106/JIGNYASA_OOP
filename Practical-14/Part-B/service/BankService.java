package service;

import model.Account;
import model.CurrentAccount;
import model.FixedDepositAccount;
import model.Repository;
import model.SavingsAccount;
import util.Validator;
import exception.BankException;
import exception.InvalidAmountException;
import exception.InsufficientFundsException;
import exception.AccountNotFoundException;

import java.util.List;

public class BankService
{
    private final Repository<Account> repository = new Repository<>();

    public SavingsAccount openSavingsAccount(String ownerName, long openingBalance, long minBalance) throws InvalidAmountException
    {
        if (!Validator.isValidName(ownerName))
        {
            throw new InvalidAmountException("Invalid owner name");
        }
        if (openingBalance < 0)
        {
            throw new InvalidAmountException("Opening balance cannot be negative");
        }
        SavingsAccount acc = new SavingsAccount(ownerName, openingBalance, minBalance);
        repository.save(acc);
        return acc;
    }

    public CurrentAccount openCurrentAccount(String ownerName, long openingBalance, long overdraftLimit) throws InvalidAmountException
    {
        if (!Validator.isValidName(ownerName))
        {
            throw new InvalidAmountException("Invalid owner name");
        }
        if (openingBalance < 0)
        {
            throw new InvalidAmountException("Opening balance cannot be negative");
        }
        CurrentAccount acc = new CurrentAccount(ownerName, openingBalance, overdraftLimit);
        repository.save(acc);
        return acc;
    }

    public FixedDepositAccount openFixedDepositAccount(String ownerName, long openingBalance) throws InvalidAmountException
    {
        if (!Validator.isValidName(ownerName))
        {
            throw new InvalidAmountException("Invalid owner name");
        }
        if (openingBalance < 0)
        {
            throw new InvalidAmountException("Opening balance cannot be negative");
        }
        FixedDepositAccount acc = new FixedDepositAccount(ownerName, openingBalance);
        repository.save(acc);
        return acc;
    }

    public void deposit(String accountNumber, long amount) throws BankException
    {
        Account acc = findAccount(accountNumber);
        acc.deposit(amount);
    }

    public void withdraw(String accountNumber, long amount) throws BankException
    {
        Account acc = findAccount(accountNumber);
        acc.withdraw(amount);
    }

    public void transfer(String fromAccount, String toAccount, long amount) throws BankException
    {
        Account from = findAccount(fromAccount);
        Account to = findAccount(toAccount);
        Account.transferSafely(from, to, amount);
    }

    public String statement(String accountNumber) throws BankException
    {
        Account acc = findAccount(accountNumber);
        return util.StatementFormatter.buildStatement(acc);
    }

    public List<Account> listAccounts()
    {
        return repository.findAll();
    }

    private Account findAccount(String accountNumber) throws AccountNotFoundException
    {
        Account acc = repository.findById(accountNumber);
        if (acc == null)
        {
            throw new AccountNotFoundException("Account not found: " + accountNumber);
        }
        return acc;
    }

    public Repository<Account> getRepository()
    {
        return repository;
    }
}