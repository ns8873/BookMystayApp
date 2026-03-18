import java.util.*;

// Custom Exception
class CancellationException extends Exception {
    public CancellationException(String message) {
        super(message);
    }
}

// Reservation class
class Reservation {
    private String reservationId;
    private String customerName;
    private String roomType;
    private String roomId;
    private boolean isActive;

    public Reservation(String reservationId, String customerName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.customerName = customerName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.isActive = true;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void cancel() {
        isActive = false;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Customer: " + customerName +
                ", Room Type: " + roomType +
                ", Room ID: " + roomId +
                ", Status: " + (isActive ? "ACTIVE" : "CANCELLED");
    }
}

// Booking Service (handles booking + inventory)
class BookingService {

    private Map<String, Integer> inventory = new HashMap<>();
    private Map<String, Reservation> reservations = new HashMap<>();
    private Stack<String> rollbackStack = new Stack<>();

    public BookingService() {
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 1);
        inventory.put("Suite", 1);
    }

    // Create booking
    public Reservation createBooking(String reservationId, String customerName, String roomType)
            throws Exception {

        if (!inventory.containsKey(roomType) || inventory.get(roomType) <= 0) {
            throw new Exception("Room not available: " + roomType);
        }

        String roomId = roomType.substring(0, 1) + (inventory.get(roomType));

        // Decrement inventory
        inventory.put(roomType, inventory.get(roomType) - 1);

        Reservation res = new Reservation(reservationId, customerName, roomType, roomId);
        reservations.put(reservationId, res);

        return res;
    }

    // Cancel booking (core logic for Use Case 10)
    public void cancelBooking(String reservationId) throws CancellationException {

        // Step 1: Validate existence
        if (!reservations.containsKey(reservationId)) {
            throw new CancellationException("Reservation not found: " + reservationId);
        }

        Reservation res = reservations.get(reservationId);

        // Step 2: Validate active status
        if (!res.isActive()) {
            throw new CancellationException("Reservation already cancelled: " + reservationId);
        }

        // Step 3: Record rollback info (LIFO)
        rollbackStack.push(res.getRoomId());

        // Step 4: Restore inventory
        String roomType = res.getRoomType();
        inventory.put(roomType, inventory.get(roomType) + 1);

        // Step 5: Update booking state
        res.cancel();

        System.out.println("Cancellation successful for Reservation ID: " + reservationId);
    }

    // Display reservations
    public void displayReservations() {
        System.out.println("\n--- Reservations ---");
        for (Reservation r : reservations.values()) {
            System.out.println(r);
        }
    }

    // Display inventory
    public void displayInventory() {
        System.out.println("\n--- Inventory ---");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    // Display rollback stack
    public void displayRollbackStack() {
        System.out.println("\n--- Rollback Stack (Recently Released Room IDs) ---");
        for (String roomId : rollbackStack) {
            System.out.println(roomId);
        }
    }
}

// Main class
public class UseCase10BookingCancellation {

    public static void main(String[] args) {

        BookingService service = new BookingService();

        try {
            // Create bookings
            System.out.println("Creating bookings...\n");

            Reservation r1 = service.createBooking("R001", "Alice", "Deluxe");
            Reservation r2 = service.createBooking("R002", "Bob", "Standard");

            System.out.println("Booking Confirmed: " + r1);
            System.out.println("Booking Confirmed: " + r2);

            service.displayInventory();

            // Perform cancellations
            System.out.println("\nProcessing cancellations...\n");

            service.cancelBooking("R001"); // valid
            service.cancelBooking("R001"); // duplicate cancellation
            service.cancelBooking("R999"); // non-existent

        } catch (CancellationException e) {
            System.out.println("Cancellation Failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Booking Error: " + e.getMessage());
        }

        // Final state
        service.displayReservations();
        service.displayInventory();
        service.displayRollbackStack();
    }
}