
public class SavingsAccount extends Account {

    private long minBalance;

    // Constructor
    public SavingsAccount(
            String ownerName,
            long openingBalance,
            long minBalance) {

        super(ownerName, openingBalance);

        this.minBalance = minBalance;
    }

    // Interest rate is 4%
    @Override
    public double interestRate() {
        return 4.0;
    }

    // Withdrawal is allowed only if
    // balance remains >= minimum balance
    @Override
    public boolean canWithdraw(long amount) {
        return amount > 0
                && getBalance() - amount >= minBalance;
    }

    // Getter
    public long getMinBalance() {
        return minBalance;
    }
}

