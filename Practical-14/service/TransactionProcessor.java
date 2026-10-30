package service;

import model.Account;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TransactionProcessor
{
    private final ExecutorService executor;
    private final ConcurrentHashMap<String, Account> accounts = new ConcurrentHashMap<>();

    public TransactionProcessor()
    {
        this.executor = Executors.newFixedThreadPool(4);
    }

    public void submit(Runnable task)
    {
        executor.execute(task);
    }

    public void stop() throws InterruptedException
    {
        executor.shutdown();
        if (!executor.awaitTermination(5, TimeUnit.SECONDS))
        {
            executor.shutdownNow();
        }
    }

    public ConcurrentHashMap<String, Account> getAccounts()
    {
        return accounts;
    }

    public void addAccount(Account account)
    {
        accounts.put(account.getAccountNumber(), account);
    }

    public Account getAccount(String accountNumber)
    {
        return accounts.get(accountNumber);
    }
}