import java.util.*;

// Custom Exception for invalid bookings
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation class
class Reservation {
    private String customerName;
    private String roomType;
    private int nights;

    public Reservation(String customerName, String roomType, int nights) {
        this.customerName = customerName;
        this.roomType = roomType;
        this.nights = nights;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getNights() {
        return nights;
    }

    @Override
    public String toString() {
        return "Customer: " + customerName +
                ", Room Type: " + roomType +
                ", Nights: " + nights;
    }
}

// Validator class
class BookingValidator {

    private static final Set<String> VALID_ROOM_TYPES =
            new HashSet<>(Arrays.asList("Standard", "Deluxe", "Suite"));

    public static void validate(String customerName, String roomType, int nights,
                                Map<String, Integer> inventory)
            throws InvalidBookingException {

        // Validate customer name
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new InvalidBookingException("Customer name cannot be empty.");
        }

        // Validate room type
        if (!VALID_ROOM_TYPES.contains(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }

        // Validate nights
        if (nights <= 0) {
            throw new InvalidBookingException("Number of nights must be greater than 0.");
        }

        // Validate inventory availability
        if (!inventory.containsKey(roomType)) {
            throw new InvalidBookingException("Room type not found in inventory.");
        }

        if (inventory.get(roomType) <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + roomType);
        }
    }
}

// Booking service
class BookingService {

    private Map<String, Integer> inventory;

    public BookingService() {
        inventory = new HashMap<>();
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 1);
        inventory.put("Suite", 1);
    }

    public Reservation createBooking(String customerName, String roomType, int nights)
            throws InvalidBookingException {

        // Fail-fast validation
        BookingValidator.validate(customerName, roomType, nights, inventory);

        // Safe state update (after validation)
        inventory.put(roomType, inventory.get(roomType) - 1);

        return new Reservation(customerName, roomType, nights);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " Rooms Available: " + entry.getValue());
        }
    }
}

// Main class
public class UseCase9ErrorHandlingValidation {

    public static void main(String[] args) {

        BookingService bookingService = new BookingService();

        // Test scenarios
        String[][] testInputs = {
                {"Alice", "Deluxe", "2"},
                {"", "Standard", "1"},            // Invalid name
                {"Bob", "Luxury", "2"},           // Invalid room type
                {"Charlie", "Suite", "0"},        // Invalid nights
                {"Diana", "Deluxe", "1"},         // Might fail if inventory empty
                {"Eve", "Deluxe", "1"}            // Overbooking scenario
        };

        for (String[] input : testInputs) {
            try {
                String name = input[0];
                String roomType = input[1];
                int nights = Integer.parseInt(input[2]);

                System.out.println("\nProcessing booking for " + name + "...");

                Reservation reservation =
                        bookingService.createBooking(name, roomType, nights);

                System.out.println("Booking Confirmed: " + reservation);

            } catch (InvalidBookingException e) {
                // Graceful failure handling
                System.out.println("Booking Failed: " + e.getMessage());
            } catch (Exception e) {
                // Catch unexpected errors
                System.out.println("Unexpected Error: " + e.getMessage());
            }
        }

        // Display final inventory state
        bookingService.displayInventory();
    }
}