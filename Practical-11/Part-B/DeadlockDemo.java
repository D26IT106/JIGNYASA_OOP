import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class DeadlockDemo {
    public static void runDemo() {
        System.out.println("\n----- Deadlock and Lock Ordering -----");

        LockingAccount unsafeA = new LockingAccount("A001", 1000);
        LockingAccount unsafeB = new LockingAccount("A002", 1000);

        Thread t1 = new Thread(
                () -> LockingAccount.transferUnsafe(unsafeA, unsafeB, 10),
                "Unsafe-A-to-B");
        Thread t2 = new Thread(
                () -> LockingAccount.transferUnsafe(unsafeB, unsafeA, 10),
                "Unsafe-B-to-A");

        // Daemon threads ensure a demonstrated deadlock cannot prevent program exit.
        t1.setDaemon(true);
        t2.setDaemon(true);
        t1.start();
        t2.start();

        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        long[] deadlocked = bean.findDeadlockedThreads();
        if (deadlocked != null && deadlocked.length > 0) {
            System.out.println("Deadlock detected: transfers acquired locks in opposite order.");
        } else {
            System.out.println("Opposite lock order was demonstrated; deadlock was not detected this run.");
        }

        LockingAccount safeA = new LockingAccount("A001", 1000);
        LockingAccount safeB = new LockingAccount("A002", 1000);
        Thread safe1 = new Thread(
                () -> LockingAccount.transferSafe(safeA, safeB, 100),
                "Safe-A-to-B");
        Thread safe2 = new Thread(
                () -> LockingAccount.transferSafe(safeB, safeA, 50),
                "Safe-B-to-A");
        safe1.start();
        safe2.start();

        try {
            safe1.join();
            safe2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Safe transfer uses account-number lock ordering.");
        System.out.println("Account A001 balance: Rs. " + safeA.getBalance());
        System.out.println("Account A002 balance: Rs. " + safeB.getBalance());
    }
}
