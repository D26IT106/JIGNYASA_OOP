
import java.util.Scanner;

public class MiniBank {

    // Record from Practical 1
    record BankInfo(String name, String branch) {
    }

    // Enum from Practical 1
    enum MenuOption {
        OPEN_ACCOUNT,
        DEPOSIT,
        WITHDRAW,
        TRANSFER,
        EXIT
     }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // ----------------------------------------
        // Bank information
        // ----------------------------------------

        BankInfo bank = new BankInfo(
                "MiniBank",
                "Main Branch"
        );

        System.out.println("================================");
        System.out.println("          " + bank.name());
        System.out.println("          " + bank.branch());
        System.out.println("================================");

        // ----------------------------------------
        // 1. Test Mobile Validator
        // ----------------------------------------

        System.out.println("\n----- Mobile Validation -----");

        System.out.println(
                "9876543210 -> "
                        + Validator.isValidMobile("9876543210")
        );

        System.out.println(
                "12345 -> "
                        + Validator.isValidMobile("12345")
        );

        // ----------------------------------------
        // 2. Test Email Validator
        // ----------------------------------------

        System.out.println("\n----- Email Validation -----");

        System.out.println(
                "rahul@gmail.com -> "
                        + Validator.isValidEmail("rahul@gmail.com")
        );

        System.out.println(
                "rahul@gmail -> "
                        + Validator.isValidEmail("rahul@gmail")
        );

        // ----------------------------------------
        // 3. Test PAN Validator
        // ----------------------------------------

        System.out.println("\n----- PAN Validation -----");

        System.out.println(
                "ABCDE1234F -> "
                        + Validator.isValidPan("ABCDE1234F")
        );

        System.out.println(
                "ABC1234F -> "
                        + Validator.isValidPan("ABC1234F")
        );

        // ----------------------------------------
        // 4. Test IFSC Validator
        // ----------------------------------------

        System.out.println("\n----- IFSC Validation -----");

        System.out.println(
                "SBIN0001234 -> "
                        + Validator.isValidIfsc("SBIN0001234")
        );

        System.out.println(
                "SBI1234 -> "
                        + Validator.isValidIfsc("SBI1234")
        );

        // ----------------------------------------
        // 5. Test Command Parser
        // ----------------------------------------

        System.out.println("\n----- Command Parser -----");

        String input = "DEPOSIT AC0001 500";

        Command command = CommandParser.parse(input);

        System.out.println("Original command : " + input);

        System.out.println(
                "Transaction Type : " + command.type()
        );

        System.out.println(
                "Account Number   : " + command.accountNumber()
        );

        System.out.println(
                "Amount           : " + command.amount()
        );

        // ----------------------------------------
        // 6. Test Statement Formatter
        // ----------------------------------------

        System.out.println("\n----- Statement Formatter -----");

        Account account = new Account(
                "Rahul",
                5000
        );

        account.deposit(2000);
        account.withdraw(500);

        String statement =
                StatementFormatter.buildStatement(account);

        System.out.println(statement);

        scanner.close();
    }
}

