public class Request {
    private final String id;
    private final Point pickup;
    private final Point destination;

    public Request(String id, Point pickup, Point destination) {
        this.id = id;
        this.pickup = pickup;
        this.destination = destination;
    }

    public double getDistance() {
        return pickup.distanceTo(destination);
    }

    public String getId() { return id; }
    public Point getPickup() { return pickup; }
    public Point getDestination() { return destination; }

    @Override
    public String toString() {
        return id + ": " + pickup + " -> " + destination;
    }
}