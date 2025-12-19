import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class Dispatcher {
    private BlockingQueue<Request> queue;
    private List<Taxi> taxis = new CopyOnWriteArrayList<>();
    private boolean running = true;

    public Dispatcher(BlockingQueue<Request> queue) {
        this.queue = queue;
    }

    public void addTaxi(Taxi taxi) {
        taxis.add(taxi);
    }

    public void start() {
        Thread worker = new Thread(() -> {
            System.out.println("Диспетчер запущен");

            while (running) {
                try {
                    if (!queue.isEmpty()) {
                        Request request = queue.peek();
                        Taxi bestTaxi = findBestTaxi(request);

                        if (bestTaxi != null) {
                            queue.take();
                            System.out.println("Диспетчер назначил " + request.getId() + " такси " + bestTaxi.getTaxiId());
                        }
                    }
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    break;
                }
            }
            System.out.println("Диспетчер остановлен");
        });
        worker.start();
    }

    private Taxi findBestTaxi(Request request) {
        Taxi best = null;
        double minDist = Double.MAX_VALUE;

        for (Taxi taxi : taxis) {
            if (taxi.isAvailable()) {
                double dist = taxi.getLocation().distanceTo(request.getPickup());
                if (dist < minDist) {
                    minDist = dist;
                    best = taxi;
                }
            }
        }

        if (best != null && best.assignRequest(request)) {
            return best;
        }

        return null;
    }

    public synchronized void assignIfPossible(Taxi taxi) {
    }

    public void notifyCompletion(Taxi taxi, Request request) {
        System.out.println("Такси " + taxi.getTaxiId() + " завершило " + request.getId());
    }

    public void stop() {
        running = false;
    }
}
