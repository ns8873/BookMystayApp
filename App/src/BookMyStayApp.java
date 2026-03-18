import java.util.*;

// Booking Request Model
class BookingRequest {
    String customerName;
    String roomType;

    public BookingRequest(String customerName, String roomType) {
        this.customerName = customerName;
        this.roomType = roomType;
    }
}

// Inventory Service
class InventoryService {
    private Map<String, Integer> inventory = new HashMap<>();

    public InventoryService() {
        inventory.put("Single", 2);
        inventory.put("Double", 2);
        inventory.put("Suite", 1);
    }

    public boolean isAvailable(String roomType) {
        return inventory.getOrDefault(roomType, 0) > 0;
    }

    public void decrement(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }

    public void displayInventory() {
        System.out.println("Current Inventory: " + inventory);
    }
}

// Room Allocation Service
public class BookMyStayApp {

    // Queue for booking requests (FIFO)
    private Queue<BookingRequest> requestQueue = new LinkedList<>();

    // Set to ensure unique room IDs
    private Set<String> allocatedRoomIds = new HashSet<>();

    // Map roomType -> Set of room IDs
    private Map<String, Set<String>> roomAllocationMap = new HashMap<>();

    private InventoryService inventoryService = new InventoryService();

    // Add booking request
    public void addRequest(BookingRequest request) {
        requestQueue.offer(request);
    }

    // Generate unique room ID
    private String generateRoomId(String roomType) {
        String roomId;
        do {
            roomId = roomType.substring(0, 2).toUpperCase() + "-" + UUID.randomUUID().toString().substring(0, 5);
        } while (allocatedRoomIds.contains(roomId));
        return roomId;
    }

    // Process bookings
    public void processBookings() {
        while (!requestQueue.isEmpty()) {
            BookingRequest request = requestQueue.poll();
            System.out.println("\nProcessing request for: " + request.customerName);

            // Check availability
            if (!inventoryService.isAvailable(request.roomType)) {
                System.out.println("No rooms available for type: " + request.roomType);
                continue;
            }

            // Generate unique room ID
            String roomId = generateRoomId(request.roomType);

            // Atomic operation: allocate + update structures
            allocatedRoomIds.add(roomId);

            roomAllocationMap.putIfAbsent(request.roomType, new HashSet<>());
            roomAllocationMap.get(request.roomType).add(roomId);

            inventoryService.decrement(request.roomType);

            // Confirm reservation
            System.out.println("Reservation Confirmed!");
            System.out.println("Customer: " + request.customerName);
            System.out.println("Room Type: " + request.roomType);
            System.out.println("Allocated Room ID: " + roomId);
        }
    }

    public void displayAllocations() {
        System.out.println("\nRoom Allocations:");
        for (String type : roomAllocationMap.keySet()) {
            System.out.println(type + " -> " + roomAllocationMap.get(type));
        }
    }

    public static void main(String[] args) {

        BookMyStayApp service = new BookMyStayApp();

        // Add booking requests (FIFO)
        service.addRequest(new BookingRequest("Alice", "Single"));
        service.addRequest(new BookingRequest("Bob", "Double"));
        service.addRequest(new BookingRequest("Charlie", "Single"));
        service.addRequest(new BookingRequest("David", "Suite"));
        service.addRequest(new BookingRequest("Eve", "Suite")); // should fail (only 1 Suite)

        // Process all bookings
        service.processBookings();

        // Display results
        service.displayAllocations();
        service.inventoryService.displayInventory();
    }
}