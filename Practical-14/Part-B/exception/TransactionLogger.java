package exception;

public class TransactionLogger implements AutoCloseable
{
    private final String operation;

    public TransactionLogger(String operation)
    {
        this.operation = operation;
        System.out.println("[LOG] Starting: " + operation);
    }

    @Override
    public void close()
    {
        System.out.println("[LOG] Finished: " + operation);
    }
}