import exception.BankException;
import model.Account;

public class TransactionTask implements Runnable {
    private final Account account;
    private final long amount;
    private final boolean deposit;

    public TransactionTask(Account account, long amount, boolean deposit) {
        this.account = account;
        this.amount = amount;
        this.deposit = deposit;
    }

    @Override
    public void run() {
        String action = deposit ? "deposit" : "withdrawal";
        try {
            if (deposit) {
                account.deposit(amount);
            } else {
                account.withdraw(amount);
            }
            System.out.println(Thread.currentThread().getName()
                    + " completed " + action + " of Rs. " + amount);
        } catch (BankException e) {
            System.out.println(Thread.currentThread().getName()
                    + " failed " + action + ": " + e.getMessage());
        }
    }
}
