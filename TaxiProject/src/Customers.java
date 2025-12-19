import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Customers implements Runnable {
    private BlockingQueue<Request> queue;
    private AtomicInteger counter = new AtomicInteger(1);
    private boolean generating = true;

    public Customers(BlockingQueue<Request> queue) {
        this.queue = queue;
    }

    public void run() {
        System.out.println("Генератор клиентов запущен");

        try {
            while (generating) {
                Point start = Point.randomPoint();
                Point end = Point.randomPoint();

                String id = "R" + counter.getAndIncrement();
                Request request = new Request(id, start, end);

                queue.put(request);
                System.out.println("Создан новый заказ: " + request);

                Thread.sleep(1500);
            }
        } catch (InterruptedException e) {
        }

        System.out.println("Генератор остановлен");
    }

    public void stop() {
        generating = false;
    }
}
