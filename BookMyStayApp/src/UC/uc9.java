/**
 * ============================================================
 * MAIN CLASS - UseCase9ErrorHandlingValidation
 * ============================================================
 *
 * Use Case 9: Error Handling & Validation
 *
 * Description:
 * This class introduces validation and custom exception handling
 * to prevent invalid booking operations and maintain system stability.
 *
 * @version 9.0
 */

import java.util.*;

// -------------------- CUSTOM EXCEPTION --------------------
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// -------------------- RESERVATION --------------------
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// -------------------- INVENTORY --------------------
class RoomInventory {

    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 1);
        inventory.put("Double Room", 0);
        inventory.put("Suite Room", 1);
    }

    // Validate room type
    public void validateRoomType(String roomType) throws InvalidBookingException {
        if (!inventory.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }
    }

    // Validate availability
    public void validateAvailability(String roomType) throws InvalidBookingException {
        int available = inventory.get(roomType);
        if (available <= 0) {
            throw new InvalidBookingException("No rooms available for: " + roomType);
        }
    }

    // Safe allocation
    public void allocateRoom(String roomType) throws InvalidBookingException {
        validateRoomType(roomType);
        validateAvailability(roomType);

        int current = inventory.get(roomType);

        if (current - 1 < 0) {
            throw new InvalidBookingException("Inventory cannot go negative!");
        }

        inventory.put(roomType, current - 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

// -------------------- BOOKING SERVICE --------------------
class BookingService {

    private RoomInventory inventory;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void processBooking(Reservation reservation) {

        try {
            System.out.println("\nProcessing booking for " + reservation.getGuestName());

            // Validation + allocation
            inventory.allocateRoom(reservation.getRoomType());

            System.out.println("Booking SUCCESS for " + reservation.getGuestName());

        } catch (InvalidBookingException e) {
            // Graceful error handling
            System.out.println("Booking FAILED: " + e.getMessage());
        }
    }
}

// -------------------- MAIN CLASS --------------------
public class UseCase9ErrorHandlingValidation {

    public static void main(String[] args) {

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Initialize booking service
        BookingService bookingService = new BookingService(inventory);

        // Valid booking
        bookingService.processBooking(new Reservation("Alice", "Single Room"));

        // Invalid room type
        bookingService.processBooking(new Reservation("Bob", "Luxury Room"));

        // No availability
        bookingService.processBooking(new Reservation("Charlie", "Double Room"));

        // Valid booking
        bookingService.processBooking(new Reservation("David", "Suite Room"));

        // Try again (should fail due to no availability)
        bookingService.processBooking(new Reservation("Eve", "Suite Room"));

        // Final inventory
        inventory.displayInventory();

        System.out.println("\nSystem remained stable after handling errors.");
    }
}