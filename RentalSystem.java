import java.util.InputMismatchException;
import java.util.Scanner;

abstract class Vehicle {
    private String licensePlate;
    private String model;
    private boolean isAvailable;

    public Vehicle(String licensePlate, String model) {
        this.licensePlate = licensePlate;
        this.model = model;
        this.isAvailable = true;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getModel() {
        return model;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public abstract double calculateRentalCost(int days);

    @Override
    public String toString() {
        return model + " (License: " + licensePlate + ")";
    }
}

class Car extends Vehicle {
    private double ratePerDay;

    public Car(String licensePlate, String model, double ratePerDay) {
        super(licensePlate, model);
        this.ratePerDay = ratePerDay;
    }

    @Override
    public double calculateRentalCost(int days) {
        return ratePerDay * days;
    }
}

class Bike extends Vehicle {
    private double ratePerDay;

    public Bike(String licensePlate, String model, double ratePerDay) {
        super(licensePlate, model);
        this.ratePerDay = ratePerDay;
    }

    @Override
    public double calculateRentalCost(int days) {
        return ratePerDay * days;
    }
}

class Customer {
    private String name;
    private String driverLicense;

    public Customer(String name, String driverLicense) {
        this.name = name;
        this.driverLicense = driverLicense;
    }

    public String getName() {
        return name;
    }

    public String getDriverLicense() {
        return driverLicense;
    }

    @Override
    public String toString() {
        return "Customer: " + name + " (Driver License: " + driverLicense + ")";
    }
}

// Rental class with exception handling
class Rental {
    private Vehicle vehicle;
    private Customer customer;
    private int rentalDays;
    private double totalCost;

    public Rental(Vehicle vehicle, Customer customer, int rentalDays) throws IllegalArgumentException {
        if (rentalDays <= 0) {
            throw new IllegalArgumentException("Rental days must be greater than zero.");
        }
        this.vehicle = vehicle;
        this.customer = customer;
        this.rentalDays = rentalDays;
        this.totalCost = vehicle.calculateRentalCost(rentalDays);
        vehicle.setAvailable(false);
    }

    public void completeRental() {
        vehicle.setAvailable(true);
        System.out.println("Rental completed for " + customer.getName() + ". Total cost: $" + totalCost);
    }

    @Override
    public String toString() {
        return "Customer: " + customer.getName() +
                "\nVehicle: " + vehicle.getModel() +
                "\nRental Days: " + rentalDays +
                "\nTotal Cost: $" + totalCost;
    }
}

// Multithreading: Rental Process
class RentalProcess extends Thread {
    private Rental rental;

    public RentalProcess(Rental rental) {
        this.rental = rental;
    }

    @Override
    public void run() {
        System.out.println("\nProcessing rental, please wait...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Rental process interrupted.");
        }
        System.out.println("\nRental Processed Successfully!");
        System.out.println(rental);
        rental.completeRental();
    }
}

// Main Class
public class RentalSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Vehicle vehicle = null;
        Customer customer = null;
        int rentalDays = 0;

        try {
            System.out.println("Enter vehicle type (Car/Bike): ");
            String vehicleType = scanner.nextLine();

            if (!vehicleType.equalsIgnoreCase("Car") && !vehicleType.equalsIgnoreCase("Bike")) {
                throw new IllegalArgumentException("Invalid vehicle type. Must be 'Car' or 'Bike'.");
            }

            System.out.println("Enter license plate: ");
            String licensePlate = scanner.nextLine();

            System.out.println("Enter model: ");
            String model = scanner.nextLine();

            System.out.println("Enter rate per day: ");
            double ratePerDay = scanner.nextDouble();
            scanner.nextLine();

            if (ratePerDay <= 0) {
                throw new IllegalArgumentException("Rate per day must be a positive number.");
            }

            if (vehicleType.equalsIgnoreCase("Car")) {
                vehicle = new Car(licensePlate, model, ratePerDay);
            } else {
                vehicle = new Bike(licensePlate, model, ratePerDay);
            }

            System.out.println("Enter customer name: ");
            String customerName = scanner.nextLine();

            System.out.println("Enter driver license number: ");
            String driverLicense = scanner.nextLine();

            customer = new Customer(customerName, driverLicense);

            System.out.println("Enter rental days: ");
            rentalDays = scanner.nextInt();

            Rental rental = new Rental(vehicle, customer, rentalDays);

            // Start rental processing in a separate thread
            RentalProcess rentalProcess = new RentalProcess(rental);
            rentalProcess.start();
            rentalProcess.join();

        } catch (InputMismatchException e) {
            System.out.println("Invalid input! Please enter the correct data type.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("Rental process was interrupted.");
        } finally {
            scanner.close();
            System.out.println("Thank you for using our rental system!");
        }
    }
}
