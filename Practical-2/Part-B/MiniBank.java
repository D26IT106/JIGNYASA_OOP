import java.util.Scanner;

public class MiniBank {

    record BankInfo(String name, String branch) {}

    enum MenuOption {
        OPEN_ACCOUNT,
        DEPOSIT,
        WITHDRAW,
        TRANSFER,
        EXIT
    }

    public static void main(String[] args) {

        BankInfo bank = new BankInfo("MiniBank", "Main Branch");

        System.out.println("================================");
        System.out.println(bank);
        System.out.println("================================");

        Customer customer1 = new Customer(
                "Jignyasa",
                "jignyasa@gmail.com",
                "9876543210"
        );

        Customer customer2 = new Customer(
                "Khushi",
                "khushi@gmail.com",
                "9876543211"
        );

        Customer customer3 = new Customer(
                "Saniya",
                "saniya@gmail.com",
                "9876543212"
        );

        Account[] accounts = {
                new Account(customer1.getName(), 5000),
                new Account(customer2.getName()),
                new Account(customer3.getName(), 10000)
        };

        accounts[0].deposit(2000);
        accounts[0].withdraw(1000);

        accounts[1].deposit(5000);
        accounts[1].withdraw(1500);

        accounts[2].deposit(3000);

        System.out.println("\n----- Account Details -----");

        for (Account account : accounts) {
            System.out.println("Account Number: " +
                    account.getAccountNumber());
            System.out.println("Owner: " +
                    account.getOwnerName());
            System.out.println("Balance: Rs." +
                    account.getBalance());
            System.out.println("Active: " +
                    account.isActive());
            System.out.println();
        }

        Scanner scanner = new Scanner(System.in);
        scanner.close();
    }
}