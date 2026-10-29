import model.Account;
import model.SavingsAccount;

public class MiniBank {

    public static void main(String[] args)
            throws InterruptedException {

        System.out.println(
                "================================"
        );

        System.out.println(
                "       MiniBank - Threads"
        );

        System.out.println(
                "================================"
        );

        int numberOfThreads = 10;
        int times = 1000;
        long amount = 10;

        long expectedBalance =
                numberOfThreads * times * amount;

        // ========================================
        // WITHOUT SYNCHRONIZATION
        // ========================================

        System.out.println(
                "\n===== WITHOUT SYNCHRONIZATION ====="
        );

        Account.setSynchronizedMode(false);

        Account account1 =
                new SavingsAccount(
                        "Rahul",
                        0,
                        0
                );

        Thread[] threads =
                new Thread[numberOfThreads];

        for (int i = 0;
             i < numberOfThreads;
             i++) {

            AccountWorker worker =
                    new AccountWorker(
                            account1,
                            times,
                            amount
                    );

            threads[i] =
                    new Thread(
                            worker,
                            "Worker-" + (i + 1)
                    );

            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println(
                "\nExpected Balance: Rs. "
                        + expectedBalance
        );

        System.out.println(
                "Actual Balance:   Rs. "
                        + account1.getBalance()
        );

        // ========================================
        // WITH SYNCHRONIZATION
        // ========================================

        System.out.println(
                "\n===== WITH SYNCHRONIZATION ====="
        );

        Account.setSynchronizedMode(true);

        Account account2 =
                new SavingsAccount(
                        "Priya",
                        0,
                        0
                );

        Thread[] synchronizedThreads =
                new Thread[numberOfThreads];

        for (int i = 0;
             i < numberOfThreads;
             i++) {

            AccountWorker worker =
                    new AccountWorker(
                            account2,
                            times,
                            amount
                    );

            synchronizedThreads[i] =
                    new Thread(
                            worker,
                            "Sync-Worker-" + (i + 1)
                    );

            synchronizedThreads[i].start();
        }

        for (Thread thread :
                synchronizedThreads) {

            thread.join();
        }

        System.out.println(
                "\nExpected Balance: Rs. "
                        + expectedBalance
        );

        System.out.println(
                "Actual Balance:   Rs. "
                        + account2.getBalance()
        );

        System.out.println(
                "\nAll threads completed."
        );
    }
}