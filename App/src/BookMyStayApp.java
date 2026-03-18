import java.io.*;
import java.util.*;

// Booking class
class Booking implements Serializable {
    int bookingId;
    String customerName;
    String roomType;

    public Booking(int bookingId, String customerName, String roomType) {
        this.bookingId = bookingId;
        this.customerName = customerName;
        this.roomType = roomType;
    }

    public String toString() {
        return bookingId + " - " + customerName + " - " + roomType;
    }
}

// Main System
public class UseCase12DataPersistenceRecovery {

    static List<Booking> bookings = new ArrayList<>();
    static Map<String, Integer> inventory = new HashMap<>();

    static final String FILE_NAME = "hotel_data.ser";

    // SAVE DATA
    public static void saveData() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME));
            oos.writeObject(bookings);
            oos.writeObject(inventory);
            oos.close();
            System.out.println("Data saved successfully!");
        } catch (Exception e) {
            System.out.println("Error saving data!");
        }
    }

    // LOAD DATA
    public static void loadData() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME));
            bookings = (List<Booking>) ois.readObject();
            inventory = (Map<String, Integer>) ois.readObject();
            ois.close();
            System.out.println("Data loaded successfully!");
        } catch (Exception e) {
            System.out.println("No previous data found. Starting fresh.");
        }
    }

    // ADD BOOKING
    public static void addBooking(int id, String name, String roomType) {
        if (inventory.getOrDefault(roomType, 0) > 0) {
            bookings.add(new Booking(id, name, roomType));
            inventory.put(roomType, inventory.get(roomType) - 1);
            System.out.println("Booking successful!");
        } else {
            System.out.println("Room not available!");
        }
    }

    // DISPLAY DATA
    public static void display() {
        System.out.println("\nBookings:");
        for (Booking b : bookings) {
            System.out.println(b);
        }

        System.out.println("\nInventory:");
        for (String key : inventory.keySet()) {
            System.out.println(key + " : " + inventory.get(key));
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // LOAD DATA AT START
        loadData();

        // Initialize inventory if empty
        if (inventory.isEmpty()) {
            inventory.put("Single", 2);
            inventory.put("Double", 2);
        }

        while (true) {
            System.out.println("\n1. Add Booking");
            System.out.println("2. View Data");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");
            int ch = sc.nextInt();

            switch (ch) {
                case 1:
                    System.out.print("Enter Booking ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Room Type (Single/Double): ");
                    String type = sc.nextLine();

                    addBooking(id, name, type);
                    break;

                case 2:
                    display();
                    break;

                case 3:
                    saveData(); // SAVE BEFORE EXIT
                    System.out.println("Exiting...");
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}