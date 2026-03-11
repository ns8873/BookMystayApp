import java.util.HashMap;
import java.util.Map;

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

    public void displayDetails() {
        System.out.println("Room Type: " + getRoomType());
        System.out.println("Beds: " + beds);
        System.out.println("Size: " + size + " sqm");
        System.out.println("Price: $" + price);
    }
}

// Concrete Room Types

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

    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();

        inventory.put("Single", 5);
        inventory.put("Double", 0);   // Example: unavailable
        inventory.put("Suite", 2);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }
}

// -------------------- Search Service (Read Only) --------------------

class SearchService {

    private RoomInventory inventory;

    public SearchService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void searchAvailableRooms(Room[] rooms) {

        System.out.println("Available Rooms\n");

        for (Room room : rooms) {

            int available = inventory.getAvailability(room.getRoomType());

            // Defensive validation
            if (available > 0) {

                room.displayDetails();
                System.out.println("Available: " + available);
                System.out.println();
            }
        }
    }
}

// -------------------- Application Entry --------------------

public class HotelApp {

    public static void main(String[] args) {

        // Create room domain objects
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Store rooms for searching
        Room[] rooms = {single, doubleRoom, suite};

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Initialize search service
        SearchService searchService = new SearchService(inventory);

        // Guest initiates search
        searchService.searchAvailableRooms(rooms);

        System.out.println("Search completed. Inventory state unchanged.");
    }
}