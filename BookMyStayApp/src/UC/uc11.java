/**
 * ============================================================
 * MAIN CLASS - UseCase11ConcurrentBookingSimulation
 * ============================================================
 *
 * Use Case 11: Concurrent Booking Simulation (Thread Safety)
 *
 * Description:
 * This class simulates multiple users booking rooms concurrently
 * and ensures thread-safe allocation using synchronization.
 *
 * @version 11.0
 */

import java.util.*;

// -------------------- RESERVATION --------------------
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
}

// -------------------- THREAD-SAFE INVENTORY --------------------
class RoomInventory {

    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 2);
    }

    // synchronized critical section
    public synchronized boolean allocateRoom(String roomType) {

        int available = inventory.getOrDefault(roomType, 0);

        if (available > 0) {
            inventory.put(roomType, available - 1);
            return true;
        }
        return false;
    }

    public void displayInventory() {
        System.out.println("\nFinal Inventory: " + inventory);
    }
}

// -------------------- THREAD-SAFE QUEUE --------------------
class BookingQueue {

    private Queue<Reservation> queue = new LinkedList<>();

    public synchronized void addRequest(Reservation r) {
        queue.offer(r);
    }

    public synchronized Reservation getRequest() {
        return queue.poll();
    }
}

// -------------------- BOOKING PROCESSOR (THREAD) --------------------
class BookingProcessor extends Thread {

    private BookingQueue queue;
    private RoomInventory inventory;

    public BookingProcessor(BookingQueue queue, RoomInventory inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {

        while (true) {

            Reservation r;

            // synchronized fetch
            synchronized (queue) {
                r = queue.getRequest();
            }

            if (r == null) break;

            // critical section: allocation
            boolean success = inventory.allocateRoom(r.getRoomType());

            if (success) {
                System.out.println(Thread.currentThread().getName() +
                        " booked room for " + r.getGuestName());
            } else {
                System.out.println(Thread.currentThread().getName() +
                        " FAILED booking for " + r.getGuestName());
            }
        }
    }
}

// -------------------- MAIN CLASS --------------------
public class UseCase11ConcurrentBookingSimulation {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingQueue queue = new BookingQueue();

        // Simulate multiple guest requests
        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Single Room"));
        queue.addRequest(new Reservation("Charlie", "Single Room"));
        queue.addRequest(new Reservation("David", "Single Room"));

        // Create multiple threads
        BookingProcessor t1 = new BookingProcessor(queue, inventory);
        BookingProcessor t2 = new BookingProcessor(queue, inventory);

        t1.setName("Thread-1");
        t2.setName("Thread-2");

        // Start threads
        t1.start();
        t2.start();

        // Wait for completion
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Final state
        inventory.displayInventory();

        System.out.println("\nThread-safe booking completed!");
    }
}