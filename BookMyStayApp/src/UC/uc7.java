/**
 * ============================================================
 * MAIN CLASS - UseCase7AddOnServiceSelection
 * ============================================================
 *
 * Use Case 7: Add-On Service Selection
 *
 * Description:
 * This class demonstrates how optional services can be
 * attached to an existing reservation without modifying
 * booking or inventory logic.
 *
 * @version 7.0
 */

import java.util.*;

// -------------------- SERVICE CLASS --------------------
class Service {
    private String name;
    private double cost;

    public Service(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }

    public double getCost() {
        return cost;
    }

    public String getName() {
        return name;
    }

    public void display() {
        System.out.println("- " + name + " (₹" + cost + ")");
    }
}

// -------------------- ADD-ON SERVICE MANAGER --------------------
class AddOnServiceManager {

    // Map: Reservation ID → List of Services
    private Map<String, List<Service>> serviceMap;

    public AddOnServiceManager() {
        serviceMap = new HashMap<>();
    }

    // Add service to a reservation
    public void addService(String reservationId, Service service) {
        serviceMap.putIfAbsent(reservationId, new ArrayList<>());
        serviceMap.get(reservationId).add(service);

        System.out.println("Service added to Reservation " + reservationId + ": " + service.getName());
    }

    // Display services for a reservation
    public void displayServices(String reservationId) {
        System.out.println("\nServices for Reservation " + reservationId + ":");

        List<Service> services = serviceMap.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No services selected.");
            return;
        }

        for (Service s : services) {
            s.display();
        }
    }

    // Calculate total add-on cost
    public double calculateTotalCost(String reservationId) {
        List<Service> services = serviceMap.get(reservationId);

        if (services == null) return 0;

        double total = 0;
        for (Service s : services) {
            total += s.getCost();
        }

        return total;
    }
}

// -------------------- MAIN CLASS --------------------
public class UseCase7AddOnServiceSelection {

    public static void main(String[] args) {

        // Simulated reservation IDs (from Use Case 6)
        String reservation1 = "SI1";
        String reservation2 = "SU2";

        // Initialize service manager
        AddOnServiceManager manager = new AddOnServiceManager();

        // Create services
        Service breakfast = new Service("Breakfast", 500);
        Service wifi = new Service("WiFi", 200);
        Service spa = new Service("Spa Access", 1500);

        // Add services to reservations
        manager.addService(reservation1, breakfast);
        manager.addService(reservation1, wifi);

        manager.addService(reservation2, spa);

        // Display services
        manager.displayServices(reservation1);
        manager.displayServices(reservation2);

        // Calculate and display total cost
        System.out.println("\nTotal Add-On Cost for " + reservation1 + ": ₹" +
                manager.calculateTotalCost(reservation1));

        System.out.println("Total Add-On Cost for " + reservation2 + ": ₹" +
                manager.calculateTotalCost(reservation2));

        System.out.println("\nAdd-on services processed successfully!");
    }
}