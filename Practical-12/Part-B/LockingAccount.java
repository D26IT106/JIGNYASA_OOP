public class LockingAccount {
    private final String accountNumber;
    private long balance;

    public LockingAccount(String accountNumber, long balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public long getBalance() {
        synchronized (this) {
            return balance;
        }
    }

    // Intentionally unsafe: opposite transfers may lock the accounts in opposite order.
    public static void transferUnsafe(LockingAccount from,
                                      LockingAccount to,
                                      long amount) {
        synchronized (from) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
            synchronized (to) {
                from.balance -= amount;
                to.balance += amount;
            }
        }
    }

    // Safe: both threads lock the lower account number first.
    public static void transferSafe(LockingAccount from,
                                    LockingAccount to,
                                    long amount) {
        LockingAccount first =
                from.accountNumber.compareTo(to.accountNumber) < 0 ? from : to;
        LockingAccount second = first == from ? to : from;

        synchronized (first) {
            synchronized (second) {
                from.balance -= amount;
                to.balance += amount;
            }
        }
    }
}
