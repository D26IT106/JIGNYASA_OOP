class Cinema {
    int seatsLeft = 5;

    synchronized boolean book() {
        if (seatsLeft > 0) {
            try {
                Thread.sleep(10); // increases chance of race condition
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            seatsLeft--;
            return true;
        }
        return false;
    }
}

public class SeatBookingRace {
    public static void main(String[] args) throws InterruptedException {

        Cinema cinema = new Cinema();

        int successfulBookings = 0;
        Thread[] threads = new Thread[10];

        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> {
                if (cinema.book()) {
                    System.out.println(Thread.currentThread().getName()
                            + " booked a seat");
                } else {
                    System.out.println(Thread.currentThread().getName()
                            + " failed");
                }
            });
            threads[i].start();
        }

        for (Thread t : threads) {
            t.join();
        }

        System.out.println("Seats left: " + cinema.seatsLeft);
    }
}