/**
 * ============================================================
 * MAIN CLASS - UseCase10BookingCancellation
 * ============================================================
 *
 * Use Case 10: Booking Cancellation & Inventory Rollback
 *
 * Description:
 * This class demonstrates safe cancellation of bookings
 * using rollback logic with Stack (LIFO).
 *
 * @version 10.0
 */

import java.util.*;

// -------------------- RESERVATION --------------------
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId; // allocated room ID

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public String getReservationId() { return reservationId; }
    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
    public String getRoomId() { return roomId; }
}

// -------------------- INVENTORY --------------------
class RoomInventory {

    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 0);
        inventory.put("Double Room", 0);
        inventory.put("Suite Room", 0);
    }

    public void increaseAvailability(String roomType) {
        inventory.put(roomType, inventory.getOrDefault(roomType, 0) + 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> e : inventory.entrySet()) {
            System.out.println(e.getKey() + " : " + e.getValue());
        }
    }
}

// -------------------- BOOKING HISTORY --------------------
class BookingHistory {

    private List<Reservation> history = new ArrayList<>();

    public void add(Reservation r) {
        history.add(r);
    }

    public void remove(String reservationId) {
        history.removeIf(r -> r.getReservationId().equals(reservationId));
    }

    public boolean exists(String reservationId) {
        for (Reservation r : history) {
            if (r.getReservationId().equals(reservationId)) return true;
        }
        return false;
    }

    public Reservation get(String reservationId) {
        for (Reservation r : history) {
            if (r.getReservationId().equals(reservationId)) return r;
        }
        return null;
    }
}

// -------------------- CANCELLATION SERVICE --------------------
class CancellationService {

    private RoomInventory inventory;
    private BookingHistory history;

    // Stack for rollback (stores released room IDs)
    private Stack<String> rollbackStack = new Stack<>();

    public CancellationService(RoomInventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
    }

    public void cancel(String reservationId) {

        System.out.println("\nProcessing cancellation for: " + reservationId);

        // Validate existence
        if (!history.exists(reservationId)) {
            System.out.println("Cancellation FAILED: Invalid or already cancelled booking.");
            return;
        }

        // Fetch reservation
        Reservation r = history.get(reservationId);

        // STEP 1: Push room ID to rollback stack (LIFO)
        rollbackStack.push(r.getRoomId());

        // STEP 2: Restore inventory
        inventory.increaseAvailability(r.getRoomType());

        // STEP 3: Remove from booking history
        history.remove(reservationId);

        // Confirmation
        System.out.println("Cancellation SUCCESS for " + r.getGuestName());
        System.out.println("Released Room ID: " + r.getRoomId());
    }

    public void showRollbackStack() {
        System.out.println("\nRollback Stack (LIFO): " + rollbackStack);
    }
}

// -------------------- MAIN CLASS --------------------
public class UseCase10BookingCancellation {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingHistory history = new BookingHistory();

        // Simulated confirmed bookings
        history.add(new Reservation("R1", "Alice", "Single Room", "SI101"));
        history.add(new Reservation("R2", "Bob", "Double Room", "DO201"));

        CancellationService service = new CancellationService(inventory, history);

        // Valid cancellation
        service.cancel("R1");

        // Invalid (already cancelled)
        service.cancel("R1");

        // Invalid (non-existent)
        service.cancel("R99");

        // Display rollback stack
        service.showRollbackStack();

        // Display inventory
        inventory.displayInventory();

        System.out.println("\nRollback handled correctly with LIFO behavior.");
    }
}