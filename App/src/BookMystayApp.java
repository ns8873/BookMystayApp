import java.util.*;

// -------------------- Domain Model --------------------

abstract class Room {

    private int beds;
    private int size;
    private double price;

    public Room(int beds, int size, double price) {
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    public int getBeds() {
        return beds;
    }

    public int getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    public abstract String getRoomType();
}

class SingleRoom extends Room {
    public SingleRoom() {
        super(1, 20, 100);
    }

    public String getRoomType() {
        return "Single";
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super(2, 35, 180);
    }

    public String getRoomType() {
        return "Double";
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super(3, 60, 350);
    }

    public String getRoomType() {
        return "Suite";
    }
}

// -------------------- Inventory (State Holder) --------------------

class RoomInventory {

    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single", 5);
        inventory.put("Double", 3);
        inventory.put("Suite", 2);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }
}

// -------------------- Reservation Request --------------------

class Reservation {

    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void displayRequest() {
        System.out.println("Guest: " + guestName + " requested " + roomType + " room");
    }
}

// -------------------- Booking Request Queue --------------------

class BookingRequestQueue {

    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    // Add request to queue
    public void submitRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Request added for " + reservation.getGuestName());
    }

    // View queued requests
    public void displayQueue() {

        System.out.println("\nCurrent Booking Queue (FIFO Order)\n");

        for (Reservation r : queue) {
            r.displayRequest();
        }
    }
}

// -------------------- Application Entry --------------------

public class HotelApp {

    public static void main(String[] args) {

        // Initialize inventory (not modified in this stage)
        RoomInventory inventory = new RoomInventory();

        // Initialize booking queue
        BookingRequestQueue requestQueue = new BookingRequestQueue();

        // Guests submit booking requests
        Reservation r1 = new Reservation("Alice", "Single");
        Reservation r2 = new Reservation("Bob", "Suite");
        Reservation r3 = new Reservation("Charlie", "Single");
        Reservation r4 = new Reservation("David", "Double");

        requestQueue.submitRequest(r1);
        requestQueue.submitRequest(r2);
        requestQueue.submitRequest(r3);
        requestQueue.submitRequest(r4);

        // Display queue order
        requestQueue.displayQueue();

        System.out.println("\nRequests stored in arrival order. Allocation will happen later.");
    }
}