public class TransactionBuffer {
    private final Runnable[] buffer;
    private int head = 0;
    private int tail = 0;
    private int count = 0;

    public TransactionBuffer(int capacity) {
        buffer = new Runnable[capacity];
    }

    public synchronized void put(Runnable task) throws InterruptedException {
        while (count == buffer.length) {
            wait();
        }
        buffer[tail] = task;
        tail = (tail + 1) % buffer.length;
        count++;
        notify();
    }

    // A null item is used as the stop signal for the consumer.
    public synchronized Runnable take() throws InterruptedException {
        while (count == 0) {
            wait();
        }
        Runnable task = buffer[head];
        buffer[head] = null;
        head = (head + 1) % buffer.length;
        count--;
        notify();
        return task;
    }
}
