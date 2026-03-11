import java.util.HashMap;
import java.util.Map;

// Abstract Room class (Domain Model)
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

    public void displayRoomInfo() {
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

    @Override
    public String getRoomType() {
        return "Single";
    }
}

class DoubleRoom extends Room {

    public DoubleRoom() {
        super(2, 35, 180);
    }

    @Override
    public String getRoomType() {
        return "Double";
    }
}

class SuiteRoom extends Room {

    public SuiteRoom() {
        super(3, 60, 350);
    }

    @Override
    public String getRoomType() {
        return "Suite";
    }
}

// Inventory Manager
class RoomInventory {

    private Map<String, Integer> inventory;

    // Constructor initializes inventory
    public RoomInventory() {
        inventory = new HashMap<>();

        // Register room availability
        inventory.put("Single", 5);
        inventory.put("Double", 3);
        inventory.put("Suite", 2);
    }

    // Get availability
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Update availability
    public void updateAvailability(String roomType, int newCount) {
        inventory.put(roomType, newCount);
    }

    // Display current inventory
    public void displayInventory() {
        System.out.println("Current Room Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " Rooms Available: " + entry.getValue());
        }
        System.out.println();
    }
}

// Application Entry Point
public class HotelApp {

    public static void main(String[] args) {

        // Create room objects
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Initialize inventory system
        RoomInventory inventory = new RoomInventory();

        System.out.println("Hotel Room Details\n");

        single.displayRoomInfo();
        System.out.println("Available: " + inventory.getAvailability(single.getRoomType()));
        System.out.println();

        doubleRoom.displayRoomInfo();
        System.out.println("Available: " + inventory.getAvailability(doubleRoom.getRoomType()));
        System.out.println();

        suite.displayRoomInfo();
        System.out.println("Available: " + inventory.getAvailability(suite.getRoomType()));
        System.out.println();

        // Display centralized inventory
        inventory.displayInventory();

        // Example update
        System.out.println("Updating Single room availability...\n");
        inventory.updateAvailability("Single", 4);

        inventory.displayInventory();
    }
}