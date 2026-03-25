/**
 * ============================================================
 * MAIN CLASS - UseCase3InventorySetup
 * ============================================================
 *
 * Use Case 3: Centralized Room Inventory Management
 *
 * Description:
 * This class demonstrates centralized inventory management
 * using a HashMap to maintain room availability.
 *
 * This replaces scattered variables with a single
 * source of truth.
 *
 * @version 3.1
 */

import java.util.HashMap;
import java.util.Map;

// Inventory Class (Encapsulates all inventory logic)
class RoomInventory {

    private HashMap<String, Integer> inventory;

    // Constructor - initialize inventory
    public RoomInventory() {
        inventory = new HashMap<>();

        // Initial room availability
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    // Get availability of a specific room type
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Update availability (increase/decrease)
    public void updateAvailability(String roomType, int countChange) {
        int current = getAvailability(roomType);
        int updated = current + countChange;

        if (updated < 0) {
            System.out.println("Error: Not enough rooms available for " + roomType);
        } else {
            inventory.put(roomType, updated);
        }
    }

    // Display full inventory
    public void displayInventory() {
        System.out.println("========== ROOM INVENTORY ==========");

        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue() + " rooms available");
        }
    }
}

public class UseCase3InventorySetup {

    /**
     * Application entry point.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Display initial inventory
        inventory.displayInventory();

        System.out.println("\n--- Updating Inventory ---");

        // Simulate booking (reduce rooms)
        inventory.updateAvailability("Single Room", -1);

        // Simulate cancellation (increase rooms)
        inventory.updateAvailability("Suite Room", +1);

        // Display updated inventory
        System.out.println();
        inventory.displayInventory();

        System.out.println("\nInventory management executed successfully!");
    }
}