import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("=== Система такси запущена ===");

        BlockingQueue<Request> queue = new ArrayBlockingQueue<>(20);

        Dispatcher dispatcher = new Dispatcher(queue);

        // Такси с разными цветами
        Taxi taxi1 = new Taxi("\u001B[31mКрасное такси\u001B[0m", dispatcher);
        Taxi taxi2 = new Taxi("\u001B[34mСинее такси\u001B[0m", dispatcher);
        Taxi taxi3 = new Taxi("\u001B[32mЗелёное такси\u001B[0m", dispatcher);
        Taxi taxi4 = new Taxi("\u001B[33mЖёлтое такси\u001B[0m", dispatcher);
        Taxi taxi5 = new Taxi("\u001B[35mФиолетовое такси\u001B[0m", dispatcher);

        dispatcher.addTaxi(taxi1);
        dispatcher.addTaxi(taxi2);
        dispatcher.addTaxi(taxi3);
        dispatcher.addTaxi(taxi4);
        dispatcher.addTaxi(taxi5);

        Customers generator = new Customers(queue);

        ExecutorService executor = Executors.newFixedThreadPool(7);

        dispatcher.start();
        executor.submit(generator);
        executor.submit(taxi1);
        executor.submit(taxi2);
        executor.submit(taxi3);
        executor.submit(taxi4);
        executor.submit(taxi5);

        Thread.sleep(3000);

        generator.stop();
        dispatcher.stop();

        taxi1.interrupt();
        taxi2.interrupt();
        taxi3.interrupt();
        taxi4.interrupt();
        taxi5.interrupt();

        executor.shutdown();
        executor.awaitTermination(3, TimeUnit.SECONDS);

        System.out.println("\n=== Система такси остановлена ===");
        System.out.println("Заказов в очереди: " + queue.size());
    }
}