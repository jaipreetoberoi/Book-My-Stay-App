/**
 * ============================================================
 * MAIN CLASS - UseCase12DataPersistenceRecovery
 * ============================================================
 *
 * Use Case 12: Data Persistence & System Recovery
 *
 * Description:
 * This class demonstrates saving and restoring system state
 * (inventory + booking history) using serialization.
 *
 * @version 12.0
 */

import java.io.*;
import java.util.*;

// -------------------- RESERVATION --------------------
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() { return reservationId; }
    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }

    public void display() {
        System.out.println(reservationId + " | " + guestName + " | " + roomType);
    }
}

// -------------------- SYSTEM STATE --------------------
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    Map<String, Integer> inventory;
    List<Reservation> bookingHistory;

    public SystemState(Map<String, Integer> inventory, List<Reservation> bookingHistory) {
        this.inventory = inventory;
        this.bookingHistory = bookingHistory;
    }
}

// -------------------- PERSISTENCE SERVICE --------------------
class PersistenceService {

    private static final String FILE_NAME = "hotel_state.ser";

    // Save state to file
    public void save(SystemState state) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(state);
            System.out.println("System state saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    // Load state from file
    public SystemState load() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            System.out.println("System state loaded successfully!");
            return (SystemState) in.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No previous data found. Starting fresh.");
        } catch (Exception e) {
            System.out.println("Error loading data. Starting with safe defaults.");
        }
        return null;
    }
}

// -------------------- MAIN CLASS --------------------
public class UseCase12DataPersistenceRecovery {

    public static void main(String[] args) {

        PersistenceService service = new PersistenceService();

        // Try loading previous state
        SystemState state = service.load();

        Map<String, Integer> inventory;
        List<Reservation> history;

        if (state != null) {
            // Restore data
            inventory = state.inventory;
            history = state.bookingHistory;

            System.out.println("\nRecovered Booking History:");
            for (Reservation r : history) {
                r.display();
            }

        } else {
            // Initialize fresh data
            inventory = new HashMap<>();
            inventory.put("Single Room", 2);
            inventory.put("Double Room", 1);
            inventory.put("Suite Room", 1);

            history = new ArrayList<>();

            // Simulate bookings
            history.add(new Reservation("R1", "Alice", "Single Room"));
            history.add(new Reservation("R2", "Bob", "Suite Room"));

            System.out.println("\nNew system initialized.");
        }

        // Display inventory
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> e : inventory.entrySet()) {
            System.out.println(e.getKey() + " : " + e.getValue());
        }

        // Save state before shutdown
        SystemState newState = new SystemState(inventory, history);
        service.save(newState);

        System.out.println("\nSystem ready with persistent state!");
    }
}