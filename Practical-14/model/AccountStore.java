package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountStore
{
    private final Map<String, Account> accounts = new HashMap<>();
    private final List<String> transactionHistory = new ArrayList<>();

    public void add(Account account)
    {
        accounts.put(account.getAccountNumber(), account);
    }

    public Account get(String accountNumber)
    {
        return accounts.get(accountNumber);
    }

    public List<Account> getAll()
    {
        return new ArrayList<>(accounts.values());
    }

    public void addTransaction(String description)
    {
        transactionHistory.add(description);
    }

    public List<String> getTransactionHistory()
    {
        return new ArrayList<>(transactionHistory);
    }

    public List<Account> sortedByBalance()
    {
        List<Account> list = new ArrayList<>(accounts.values());
        Collections.sort(list, new Comparator<Account>()
        {
            @Override
            public int compare(Account a1, Account a2)
            {
                return Long.compare(a1.getBalance(), a2.getBalance());
            }
        });
        return list;
    }
}