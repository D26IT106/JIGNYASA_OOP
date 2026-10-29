package util;

public class CommandParser
{
    public static Command parse(String line)
    {
        if (line == null || line.isBlank())
        {
            return null;
        }

        String[] parts = line.split("\\s+");
        if (parts.length != 3)
        {
            throw new IllegalArgumentException("Invalid command format. Expected: TYPE ACCOUNT AMOUNT");
        }

        try
        {
            TransactionType type = TransactionType.valueOf(parts[0].toUpperCase());
            String accountNumber = parts[1];
            long amount = Long.parseLong(parts[2]);
            return new Command(type, accountNumber, amount);
        }
        catch (IllegalArgumentException e)
        {
            throw new IllegalArgumentException("Error parsing command: " + e.getMessage());
        }
    }
}