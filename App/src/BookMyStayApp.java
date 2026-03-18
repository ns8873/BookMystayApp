import java.util.*;

// Booking Request class
class BookingRequest {
    private String customerName;
    private String roomType;

    public BookingRequest(String customerName, String roomType) {
        this.customerName = customerName;
        this.roomType = roomType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// Shared Booking System
class BookingSystem {

    private Map<String, Integer> inventory = new HashMap<>();
    private Queue<BookingRequest> bookingQueue = new LinkedList<>();

    public BookingSystem() {
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 1);
        inventory.put("Suite", 1);
    }

    // Add request to queue (synchronized)
    public synchronized void addRequest(BookingRequest request) {
        bookingQueue.add(request);
        System.out.println("Request added: " + request.getCustomerName() + " -> " + request.getRoomType());
    }

    // Process request (critical section)
    public void processRequest() {

        BookingRequest request;

        synchronized (this) {
            if (bookingQueue.isEmpty()) {
                return;
            }
            request = bookingQueue.poll();
        }

        // Simulate processing delay
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Critical section for inventory update
        synchronized (this) {
            String roomType = request.getRoomType();

            if (inventory.containsKey(roomType) && inventory.get(roomType) > 0) {
                inventory.put(roomType, inventory.get(roomType) - 1);
                System.out.println(Thread.currentThread().getName() +
                        " SUCCESS: Booked " + roomType +
                        " for " + request.getCustomerName());
            } else {
                System.out.println(Thread.currentThread().getName() +
                        " FAILED: No rooms available for " +
                        request.getCustomerName());
            }
        }
    }

    public void displayInventory() {
        System.out.println("\nFinal Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}

// Worker Thread
class BookingProcessor extends Thread {

    private BookingSystem system;

    public BookingProcessor(BookingSystem system, String name) {
        super(name);
        this.system = system;
    }

    @Override
    public void run() {
        // Each thread tries to process multiple requests
        for (int i = 0; i < 3; i++) {
            system.processRequest();
        }
    }
}

// Main class
public class UseCase11ConcurrentBookingSimulation {

    public static void main(String[] args) {

        BookingSystem system = new BookingSystem();

        // Simulate multiple guest requests
        system.addRequest(new BookingRequest("Alice", "Deluxe"));
        system.addRequest(new BookingRequest("Bob", "Standard"));
        system.addRequest(new BookingRequest("Charlie", "Suite"));
        system.addRequest(new BookingRequest("Diana", "Standard"));
        system.addRequest(new BookingRequest("Eve", "Deluxe")); // Overbooking case

        // Create multiple threads (simulating concurrent users)
        BookingProcessor t1 = new BookingProcessor(system, "Thread-1");
        BookingProcessor t2 = new BookingProcessor(system, "Thread-2");
        BookingProcessor t3 = new BookingProcessor(system, "Thread-3");

        // Start threads
        t1.start();
        t2.start();
        t3.start();

        // Wait for threads to finish
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Final system state
        system.displayInventory();
    }
}