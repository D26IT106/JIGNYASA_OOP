class Counter {
    int count = 0;

    synchronized void increment() {
        count++;
    }
}

public class CounterRace {
    public static void main(String[] args) throws InterruptedException {

        Counter counter = new Counter();

        int threads = 10;
        int increments = 1000;

        Thread[] t = new Thread[threads];

        for (int i = 0; i < threads; i++) {
            t[i] = new Thread(() -> {
                for (int j = 0; j < increments; j++) {
                    counter.increment();
                }
            });
            t[i].start();
        }

        for (Thread thread : t) {
            thread.join();
        }

        System.out.println("Expected: " + (threads * increments));
        System.out.println("Actual: " + counter.count);
    }
}