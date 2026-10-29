import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        ItemService service = new ItemService();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while(running)
        {
            System.out.println("\n1. Add Item");
            System.out.println("2. View Item Count");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch(choice)
            {
                case 1:
                    System.out.print("Enter item ID: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter item name: ");
                    String name = scanner.nextLine();
                    service.addItem(new Item(id, name));
                    System.out.println("Item added successfully.");
                    break;

                case 2:
                    System.out.println("Total items: " + service.getItemCount());
                    break;

                case 3:
                    running = false;
                    System.out.println("Exiting application.");
                    break;

                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
        scanner.close();
    }
}