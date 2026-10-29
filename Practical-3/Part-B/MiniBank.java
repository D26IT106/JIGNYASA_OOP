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

        Customer.Address address = new Customer.Address(
                "123 Main Street",
                "Anand",
                "388001"
        );

        customer1.setAddress(address);

        Customer customer2 = new Customer(
                "Khushi",
                "khushi@gmail.com",
                "9876543211"
        );

        Account account1 = new Account("Jignyasa", 5000);
        Account account2 = new Account("Khushi", 10000);
        Account account3 = new Account("Saniya", 15000);

        account1.deposit(2000);
        account2.withdraw(2000);

        System.out.println("\n----- Accounts -----");

        System.out.println(account1);
        System.out.println(account2);
        System.out.println(account3);

        System.out.println("\n----- Account Comparison -----");

        System.out.println("account1 equals account2: "
                + account1.equals(account2));

        System.out.println("account1 equals account1: "
                + account1.equals(account1));

        System.out.println("\n----- instanceof Test -----");

        if (account1 instanceof Account) {
            System.out.println("account1 is an Account object.");
        }

        if (customer1 instanceof Customer) {
            System.out.println("customer1 is a Customer object.");
        }

        System.out.println("\n----- Customer Address -----");

        System.out.println("Customer: " + customer1.getName());
        System.out.println("Address: " +
                customer1.getAddress().getLine());
        System.out.println("City: " +
                customer1.getAddress().getCity());
        System.out.println("Pincode: " +
                customer1.getAddress().getPincode());

        System.out.println("\n----- Clone Test -----");

        Customer clonedCustomer = customer1.clone();

        System.out.println("Original Customer ID: "
                + customer1.getCustomerId());

        System.out.println("Cloned Customer ID: "
                + clonedCustomer.getCustomerId());

        System.out.println("Clone created successfully: "
                + (customer1 != clonedCustomer));
    }
}