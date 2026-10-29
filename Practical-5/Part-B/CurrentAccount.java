
public class CurrentAccount extends Account {

    private long overdraftLimit;

    // Constructor
    public CurrentAccount(
            String ownerName,
            long openingBalance,
            long overdraftLimit) {

        super(ownerName, openingBalance);

        this.overdraftLimit = overdraftLimit;
    }

    // Current account has 0% interest
    @Override
    public double interestRate() {
        return 0.0;
    }

    // Withdrawal is allowed up to the overdraft limit
    @Override
    public boolean canWithdraw(long amount) {

        return amount > 0
                && getBalance() - amount >= -overdraftLimit;
    }

    // Getter
    public long getOverdraftLimit() {
        return overdraftLimit;
    }
}
