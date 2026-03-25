/**
 * ============================================================
 * MAIN CLASS - UseCase8BookingHistoryReport
 * ============================================================
 *
 * Use Case 8: Booking History & Reporting
 *
 * Description:
 * This class stores confirmed bookings in a history list
 * and generates reports for admin review.
 *
 * @version 8.0
 */

import java.util.*;

// -------------------- RESERVATION --------------------
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void display() {
        System.out.println("Reservation ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room Type: " + roomType);
    }
}

// -------------------- BOOKING HISTORY --------------------
class BookingHistory {

    // List maintains insertion order
    private List<Reservation> history;

    public BookingHistory() {
        history = new ArrayList<>();
    }

    // Add confirmed reservation
    public void addReservation(Reservation reservation) {
        history.add(reservation);
        System.out.println("Added to history: " + reservation.getReservationId());
    }

    // Retrieve all bookings
    public List<Reservation> getAllReservations() {
        return history;
    }
}

// -------------------- REPORT SERVICE --------------------
class BookingReportService {

    private BookingHistory history;

    public BookingReportService(BookingHistory history) {
        this.history = history;
    }

    // Display full booking history
    public void displayAllBookings() {
        System.out.println("\n========== BOOKING HISTORY ==========\n");

        for (Reservation r : history.getAllReservations()) {
            r.display();
        }
    }

    // Generate summary report
    public void generateSummaryReport() {
        System.out.println("\n========== SUMMARY REPORT ==========\n");

        Map<String, Integer> countMap = new HashMap<>();

        for (Reservation r : history.getAllReservations()) {
            String type = r.getRoomType();
            countMap.put(type, countMap.getOrDefault(type, 0) + 1);
        }

        for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
            System.out.println(entry.getKey() + " Bookings: " + entry.getValue());
        }
    }
}

// -------------------- MAIN CLASS --------------------
public class UseCase8BookingHistoryReport {

    public static void main(String[] args) {

        // Initialize booking history
        BookingHistory history = new BookingHistory();

        // Simulate confirmed bookings (from Use Case 6)
        history.addReservation(new Reservation("SI1", "Alice", "Single Room"));
        history.addReservation(new Reservation("SI2", "Bob", "Single Room"));
        history.addReservation(new Reservation("SU3", "Charlie", "Suite Room"));

        // Initialize report service
        BookingReportService reportService = new BookingReportService(history);

        // Display booking history
        reportService.displayAllBookings();

        // Generate summary report
        reportService.generateSummaryReport();

        System.out.println("\nReporting completed successfully!");
    }
}