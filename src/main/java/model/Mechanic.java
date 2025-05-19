package model;

import DB.DBconnection;
import java.sql.*;
import java.util.Scanner;

public class Mechanic extends User {
    public Mechanic(String username, String password) {
        super(username, password, "MECHANIC");
    }

    @Override
    public void showMenu() {
        System.out.println("\n=== Меню механика ===");
        System.out.println("1. Посмотреть текущие задания");
        System.out.println("2. Обновить статус заказа");
        System.out.println("3. Запись использованных запчастей");
        System.out.println("4. Mark as completed");
        System.out.println("5. Exit");
    }

    public void viewCurrentTasks() {
        String sql = "SELECT id, client_name, car_model, service_description, status " +
                "FROM appointments WHERE status != 'Completed' ORDER BY appointment_date";

        try (Connection conn = DBconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\nCurrent Tasks:");
            System.out.println("+-----+----------------------+----------------+------------------------------+----------------+");
            System.out.println("| ID  | Client               | Car Model      | Service                      | Status         |");
            System.out.println("+-----+----------------------+----------------+------------------------------+----------------+");

            while (rs.next()) {
                System.out.printf("| %-3d | %-20s | %-14s | %-28s | %-14s |\n",
                        rs.getInt("id"),
                        rs.getString("client_name"),
                        rs.getString("car_model"),
                        rs.getString("service_description"),
                        rs.getString("status"));
            }
            System.out.println("+-----+----------------------+----------------+------------------------------+----------------+");

        } catch (SQLException e) {
            System.err.println("Error retrieving tasks: " + e.getMessage());
        }
    }

    public void updateRepairStatus(Scanner scanner) {
        System.out.print("Enter task ID: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // consume newline

        System.out.println("Available statuses:");
        System.out.println("1. In Progress");
        System.out.println("2. Completed");
        System.out.println("3. Waiting for Parts");
        System.out.print("Select new status (1-3): ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        String newStatus = switch (choice) {
            case 1 -> "In Progress";
            case 2 -> "Completed";
            case 3 -> "Waiting for Parts";
            default -> throw new IllegalArgumentException("Invalid status choice");
        };

        String sql = "UPDATE appointments SET status = ? WHERE id = ?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newStatus);
            pstmt.setInt(2, id);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Status updated to: " + newStatus);

                if (newStatus.equals("Completed")) {
                    System.out.println("Task completed");
                }
            } else {
                System.out.println("Task not found");
            }

        } catch (SQLException e) {
            System.err.println("Error updating status: " + e.getMessage());
        }
    }

    public void recordUsedParts(Scanner scanner) {
        System.out.print("Enter task ID: ");
        int appointmentId = scanner.nextInt();
        scanner.nextLine(); // consume newline

        System.out.print("Part name: ");
        String partName = scanner.nextLine();

        System.out.print("Quantity used: ");
        int quantity = scanner.nextInt();

        System.out.print("Unit price: ");
        double unitPrice = scanner.nextDouble();
        scanner.nextLine(); // consume newline

        String sql = "INSERT INTO used_parts (appointment_id, part_name, quantity, unit_price) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, appointmentId);
            pstmt.setString(2, partName);
            pstmt.setInt(3, quantity);
            pstmt.setDouble(4, unitPrice);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Parts recorded successfully!");
                updateAppointmentCost(appointmentId);
            }

        } catch (SQLException e) {
            System.err.println("Error recording parts: " + e.getMessage());
        }
    }

    public void markAsCompleted(Scanner scanner) {
        System.out.print("Enter task ID to complete: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // consume newline

        String sql = "UPDATE appointments SET status = 'Completed', completion_time = CURRENT_TIMESTAMP " +
                "WHERE id = ?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Task marked as completed!");
            }

        } catch (SQLException e) {
            System.err.println("Error completing task: " + e.getMessage());
        }
    }



    private void updateAppointmentCost(int appointmentId) {
        String sql = "UPDATE appointments SET cost = " +
                "(SELECT COALESCE(SUM(quantity * unit_price), 0) FROM used_parts " +
                "WHERE appointment_id = ?) WHERE id = ?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, appointmentId);
            pstmt.setInt(2, appointmentId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error updating appointment cost: " + e.getMessage());
        }
    }
}