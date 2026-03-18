import java.util.*;

// Add-On Service Model
class AddOnService {
    String serviceName;
    double cost;

    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    @Override
    public String toString() {
        return serviceName + " ($" + cost + ")";
    }
}

// Add-On Service Manager
class AddOnServiceManager {

    // Map: Reservation ID -> List of Add-On Services
    private Map<String, List<AddOnService>> serviceMap = new HashMap<>();

    // Add services to a reservation
    public void addService(String reservationId, AddOnService service) {
        serviceMap.putIfAbsent(reservationId, new ArrayList<>());
        serviceMap.get(reservationId).add(service);

        System.out.println("Added service '" + service.serviceName +
                "' to Reservation ID: " + reservationId);
    }

    // Get services for a reservation
    public List<AddOnService> getServices(String reservationId) {
        return serviceMap.getOrDefault(reservationId, new ArrayList<>());
    }

    // Calculate total cost of services
    public double calculateTotalCost(String reservationId) {
        List<AddOnService> services = serviceMap.get(reservationId);
        if (services == null) return 0.0;

        double total = 0;
        for (AddOnService s : services) {
            total += s.cost;
        }
        return total;
    }

    // Display services
    public void displayServices(String reservationId) {
        List<AddOnService> services = getServices(reservationId);

        System.out.println("\nServices for Reservation ID: " + reservationId);
        if (services.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }

        for (AddOnService s : services) {
            System.out.println("- " + s);
        }

        System.out.println("Total Add-On Cost: $" + calculateTotalCost(reservationId));
    }
}

// Main Class
public class BookMyStayApp {

    public static void main(String[] args) {

        BookMyStayApp = new BookMyStayApp();

        // Simulated reservation IDs (from Use Case 6)
        String res1 = "RES-1001";
        String res2 = "RES-1002";

        // Create add-on services
        AddOnService breakfast = new AddOnService("Breakfast", 15.0);
        AddOnService spa = new AddOnService("Spa Access", 40.0);
        AddOnService pickup = new AddOnService("Airport Pickup", 25.0);
        AddOnService wifi = new AddOnService("Premium WiFi", 10.0);

        // Guest selects services
        manager.addService(res1, breakfast);
        manager.addService(res1, wifi);
        manager.addService(res1, spa);

        manager.addService(res2, pickup);

        // Display services and cost
        manager.displayServices(res1);
        manager.displayServices(res2);
    }
}