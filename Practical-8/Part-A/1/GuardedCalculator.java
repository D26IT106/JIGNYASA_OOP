import java.util.Scanner;
class DivideByZeroException extends Exception {
    DivideByZeroException(String message) {
        super(message);
    }
}
public class GuardedCalculator {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean success = false;
        while (!success) {
            try {
                System.out.print("Enter first number: ");
                double num1 = Double.parseDouble(sc.nextLine());
                System.out.print("Enter operator (+, -, *, /): ");
                char operator = sc.nextLine().charAt(0);
                System.out.print("Enter second number: ");
                double num2 = Double.parseDouble(sc.nextLine());
                double result;
                switch (operator) {
                    case '+':
                        result = num1 + num2;
                        break;
                    case '-':
                        result = num1 - num2;
                        break;
                    case '*':
                        result = num1 * num2;
                        break;
                    case '/':
                        if (num2 == 0) {
                            throw new DivideByZeroException(
                                "Cannot divide by zero."
                            );
                        }
                        result = num1 / num2;
                        break;
                    default:
                        System.out.println("Invalid operator.");
                        continue;
                }
                System.out.println("Result = " + result);
                success = true;
            } catch (NumberFormatException e) {
                System.out.println(
                    "Invalid number input. Please enter valid numbers."
                );
            } catch (DivideByZeroException e) {
                System.out.println(
                    "Error: " + e.getMessage()
                );
            } finally {
                System.out.println("Attempt logged.");
                System.out.println("-------------------------");
            }
        }
        System.out.println("Valid calculation completed.");
        sc.close();
    }
}
