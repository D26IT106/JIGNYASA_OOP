import model.Account;
import model.CurrentAccount;
import model.FixedDepositAccount;
import model.SavingsAccount;
import util.Repository;

public class MiniBank {
    public static void main(String[] args) {

        Repository<Account> repository = new Repository<>();

        Account rahul = new SavingsAccount("Rahul", 5000, 1000);
        Account priya = new CurrentAccount("Priya", 3000, 2000);
        Account amit = new FixedDepositAccount("Amit", 10000);

        repository.saveAll(rahul, priya, amit);

        System.out.println("\n----- Find Account by ID -----");
        Account found = repository.findById(rahul.getAccountNumber());
        System.out.println(found != null ? found : "Account not found.");

        System.out.println("\n----- All Repository Accounts -----");
        for (Account account : repository.findAll()) {
            System.out.println(account);
            System.out.println("Type: " + account.getClass().getSimpleName());
        }

        System.out.println("\n----- Bounded Generic Average -----");
        Integer[] integerValues = {10, 20, 30, 40};
        Double[] doubleValues = {2.5, 5.0, 7.5};
        System.out.println("Integer average: "
                + Repository.average(integerValues));
        System.out.println("Double average: "
                + Repository.average(doubleValues));

        System.out.println("\n----- Delete Account -----");
        boolean deleted = repository.delete(priya.getAccountNumber());
        System.out.println("Priya's account deleted: " + deleted);
        System.out.println("Accounts remaining: " + repository.findAll().size());

    }
}
