package model;

import DB.DBconnection;
import java.sql.*;
import java.util.Scanner;

public class PartsSpecialist extends User {
    public PartsSpecialist(String name, String password) {
        super(name, password, "PARTS_SPECIALIST");
    }

    @Override
    public void showMenu() {
        System.out.println("\n=== Меню специалиста по запчастям ===");
        System.out.println("1. Посмотреть склад");
        System.out.println("2. Добавить запчасти");
        System.out.println("3. Убрать запчасти");
        System.out.println("4. Заказать у поставщика");
        System.out.println("5. Выход");
    }

    public void viewInventory() {
        String sql = "SELECT part_name, quantity, unit_price FROM inventory ORDER BY part_name";

        try (Connection conn = DBconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\nCurrent Inventory:");
            System.out.println("+------------------------------+----------+------------+");
            System.out.println("| Part Name                    | Quantity | Unit Price |");
            System.out.println("+------------------------------+----------+------------+");

            while (rs.next()) {
                System.out.printf("| %-28s | %-8d | %-10.2f |\n",
                        rs.getString("part_name"),
                        rs.getInt("quantity"),
                        rs.getDouble("unit_price"));
            }
            System.out.println("+------------------------------+----------+------------+");

        } catch (SQLException e) {
            System.err.println("Error viewing inventory: " + e.getMessage());
        }
    }

    public void addParts(Scanner scanner) {
        System.out.print("Part name: ");
        String partName = scanner.nextLine();
        System.out.print("Quantity: ");
        int quantity = scanner.nextInt();
        System.out.print("Unit price: ");
        double unitPrice = scanner.nextDouble();
        scanner.nextLine(); // consume newline

        String sql = "INSERT INTO inventory (part_name, quantity, unit_price) VALUES (?, ?, ?)";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, partName);
            pstmt.setInt(2, quantity);
            pstmt.setDouble(3, unitPrice);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Parts added to inventory successfully!");
            }

        } catch (SQLException e) {
            System.err.println("Error adding parts: " + e.getMessage());
        }
    }

    public void removeParts(Scanner scanner) {
        System.out.print("Part name to remove: ");
        String partName = scanner.nextLine();
        System.out.print("Quantity to remove: ");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // consume newline

        String sql = "UPDATE inventory SET quantity = quantity - ? WHERE part_name = ? AND quantity >= ?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, quantity);
            pstmt.setString(2, partName);
            pstmt.setInt(3, quantity);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Parts removed from inventory successfully!");
            } else {
                System.out.println("Failed to remove parts. Check if part exists or has sufficient quantity.");
            }

        } catch (SQLException e) {
            System.err.println("Error removing parts: " + e.getMessage());
        }
    }

    public void orderFromSupplier(Scanner scanner) {
        System.out.print("Part name to order: ");
        String partName = scanner.nextLine();
        System.out.print("Quantity to order: ");
        int quantity = scanner.nextInt();
        System.out.print("Unit price: ");
        double unitPrice = scanner.nextDouble();
        scanner.nextLine(); // consume newline

        String sql = "INSERT INTO supplier_orders (part_name, quantity, unit_price, order_date) " +
                "VALUES (?, ?, ?, CURRENT_DATE)";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, partName);
            pstmt.setInt(2, quantity);
            pstmt.setDouble(3, unitPrice);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Order placed with supplier successfully!");
            }

        } catch (SQLException e) {
            System.err.println("Error placing order: " + e.getMessage());
        }
    }
    public void setPartPrice(Scanner scanner) {
        viewInventory(); // Show current parts and prices
        System.out.print("\nEnter part name to update: ");
        String partName = scanner.nextLine();
        System.out.print("Enter new unit price: ");
        double newPrice = scanner.nextDouble();
        scanner.nextLine(); // consume newline

        String sql = "UPDATE inventory SET unit_price = ? WHERE part_name = ?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, newPrice);
            pstmt.setString(2, partName);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Price updated successfully!");
            } else {
                System.out.println("Part not found. Please check the name and try again.");
            }

        } catch (SQLException e) {
            System.err.println("Error updating price: " + e.getMessage());
        }
    }
}