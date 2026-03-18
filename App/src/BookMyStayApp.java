import java.util.*;

// Reservation class representing a confirmed booking
class Reservation {
    private String reservationId;
    private String customerName;
    private String roomType;
    private int nights;

    public Reservation(String reservationId, String customerName, String roomType, int nights) {
        this.reservationId = reservationId;
        this.customerName = customerName;
        this.roomType = roomType;
        this.nights = nights;
    }

    public String getReservationId() {
        return reservationId;
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
        return "Reservation ID: " + reservationId +
                ", Customer: " + customerName +
                ", Room Type: " + roomType +
                ", Nights: " + nights;
    }
}

// BookingHistory class - stores confirmed reservations
class BookingHistory {
    private List<Reservation> reservations;

    public BookingHistory() {
        reservations = new ArrayList<>();
    }

    // Add confirmed booking
    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    // Retrieve all reservations (read-only style)
    public List<Reservation> getAllReservations() {
        return Collections.unmodifiableList(reservations);
    }
}

// BookingReportService - generates reports
class BookingReportService {

    // Display all bookings
    public void displayAllBookings(List<Reservation> reservations) {
        System.out.println("\n--- Booking History ---");
        for (Reservation r : reservations) {
            System.out.println(r);
        }
    }

    // Generate summary report
    public void generateSummary(List<Reservation> reservations) {
        System.out.println("\n--- Booking Summary Report ---");

        int totalBookings = reservations.size();
        int totalNights = 0;

        Map<String, Integer> roomTypeCount = new HashMap<>();

        for (Reservation r : reservations) {
            totalNights += r.getNights();

            roomTypeCount.put(
                    r.getRoomType(),
                    roomTypeCount.getOrDefault(r.getRoomType(), 0) + 1
            );
        }

        System.out.println("Total Bookings: " + totalBookings);
        System.out.println("Total Nights Booked: " + totalNights);

        System.out.println("\nRoom Type Distribution:");
        for (Map.Entry<String, Integer> entry : roomTypeCount.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}

// Main class
public class UseCase8BookingHistoryReport {

    public static void main(String[] args) {

        // Initialize booking history and report service
        BookingHistory bookingHistory = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        // Simulate confirmed bookings
        System.out.println("Adding confirmed bookings...\n");

        bookingHistory.addReservation(new Reservation("R001", "Alice", "Deluxe", 3));
        bookingHistory.addReservation(new Reservation("R002", "Bob", "Standard", 2));
        bookingHistory.addReservation(new Reservation("R003", "Charlie", "Suite", 5));
        bookingHistory.addReservation(new Reservation("R004", "Diana", "Deluxe", 1));

        // Admin retrieves booking history
        List<Reservation> storedReservations = bookingHistory.getAllReservations();

        // Display all bookings
        reportService.displayAllBookings(storedReservations);

        // Generate summary report
        reportService.generateSummary(storedReservations);
    }
}