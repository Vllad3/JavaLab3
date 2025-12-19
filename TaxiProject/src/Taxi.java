public class Taxi extends Thread {
    private String id;
    private Dispatcher dispatcher;
    private Point currentLocation;
    private Request currentRequest;
    private boolean available = true;
    private int completedTrips = 0;

    public Taxi(String id, Dispatcher dispatcher) {
        this.id = id;
        this.dispatcher = dispatcher;
        this.currentLocation = Point.randomPoint();
    }

    public void run() {
        System.out.println(id + " начал работу в " + currentLocation);

        try {
            while (!Thread.currentThread().isInterrupted()) {
                if (available) {
                    Thread.sleep(100);
                    dispatcher.assignIfPossible(this);
                }

                if (currentRequest != null) {
                    executeTrip();
                }
            }
        } catch (InterruptedException e) {
        }

        System.out.println(id + " закончил работу");
    }

    private void executeTrip() throws InterruptedException {
        available = false;

        System.out.println(id + " взял заказ " + currentRequest);

        double toClient = currentLocation.distanceTo(currentRequest.getPickup());
        System.out.println(id + " едет за клиентом (" + String.format("%.1f", toClient) + " ед.)");
        Thread.sleep((long)(toClient * 100));

        currentLocation = currentRequest.getPickup();
        System.out.println(id + " забрал клиента");

        double tripDistance = currentRequest.getDistance();
        System.out.println(id + " везет клиента (" + String.format("%.1f", tripDistance) + " ед.)");
        Thread.sleep((long)(tripDistance * 100));

        currentLocation = currentRequest.getDestination();
        completedTrips++;
        System.out.println(id + " доставил клиента. Всего поездок: " + completedTrips);

        dispatcher.notifyCompletion(this, currentRequest);
        currentRequest = null;
        available = true;
    }

    public synchronized boolean assignRequest(Request request) {
        if (available) {
            currentRequest = request;
            return true;
        }
        return false;
    }

    public synchronized boolean isAvailable() {
        return available;
    }

    public synchronized Point getLocation() {
        return currentLocation;
    }

    public String getTaxiId() {
        return id;
    }
}