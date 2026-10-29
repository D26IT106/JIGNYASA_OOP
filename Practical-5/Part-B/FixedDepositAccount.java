
public class FixedDepositAccount extends Account {

    // Constructor
    public FixedDepositAccount(
            String ownerName,
            long openingBalance) {

        super(ownerName, openingBalance);
    }

    // Fixed deposit has 7% interest
    @Override
    public double interestRate() {
        return 7.0;
    }

    // Fixed deposit cannot be withdrawn
    @Override
    public boolean canWithdraw(long amount) {
        return false;
    }
}

