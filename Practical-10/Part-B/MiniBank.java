import model.Account;
import model.SavingsAccount;

public class MiniBank {
    public static void main(String[] args) throws InterruptedException {

        // Make Account's deposit/withdraw methods thread-safe for the pool test.
        Account.setSynchronizedMode(true);
        Account account = new SavingsAccount("Rahul", 10000, 0);

        System.out.println("\n----- Managed Thread Pool -----");
        System.out.println("Starting balance: Rs. " + account.getBalance());

        TransactionProcessor processor = new TransactionProcessor();
        for (int i = 1; i <= 8; i++) {
            processor.submit(new TransactionTask(account, 500, true));
            processor.submit(new TransactionTask(account, 200, false));
        }
        processor.stop();
        System.out.println("Expected final balance: Rs. 12400");
        System.out.println("Actual final balance:   Rs. " + account.getBalance());
        System.out.println("All submitted transactions have completed.");

        System.out.println("\n----- Producer-Consumer (wait/notify) -----");
        TransactionBuffer buffer = new TransactionBuffer(3);
        Account consumerAccount = new SavingsAccount("Priya", 1000, 0);

        Thread consumer = new Thread(() -> {
            try {
                while (true) {
                    Runnable task = buffer.take();
                    if (task == null) {
                        System.out.println("Consumer received stop signal.");
                        break;
                    }
                    task.run();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Consumer interrupted.");
            }
        }, "Consumer");

        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    buffer.put(new TransactionTask(consumerAccount, 100L * i, true));
                    System.out.println("Producer queued deposit Rs. " + (100L * i));
                }
                buffer.put(null);
                System.out.println("Producer queued stop signal.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Producer interrupted.");
            }
        }, "Producer");

        consumer.start();
        producer.start();
        producer.join();
        consumer.join();
        System.out.println("Consumer account final balance: Rs. "
                + consumerAccount.getBalance());

        DeadlockDemo.runDemo();

    }
}
