package model;

import DB.DBconnection;
import java.sql.*;
import java.util.Scanner;

public class Manager extends User {
    public Manager(String username, String password) {
        super(username, password, "MANAGER");
    }

    @Override
    public void showMenu() {
        System.out.println("\n=== Меню менеджера ===");
        System.out.println("1. Создать запись на обслуживание");
        System.out.println("2. Просмотреть все записи");
        System.out.println("3. Изменить статус записи");
        System.out.println("4. Обновить стоимость услуги");
        System.out.println("5. Просмотреть завершенные услуги");
        System.out.println("6. Выход");
    }

    public void createAppointment(Scanner scanner) {
        System.out.println("\nСоздание новой записи:");
        System.out.print("Имя клиента: ");
        String client = scanner.nextLine();
        System.out.print("Описание услуги: ");
        String service = scanner.nextLine();
        System.out.print("Дата (гггг-мм-дд): ");
        String date = scanner.nextLine();
        System.out.print("Стоимость: ");
        double cost = scanner.nextDouble();
        scanner.nextLine(); // consume newline

        String sql = "INSERT INTO appointments (client_name, service_description, appointment_date, status, cost) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING id";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, client);
            pstmt.setString(2, service);
            pstmt.setDate(3, Date.valueOf(date));
            pstmt.setString(4, "В процессе"); // Default status
            pstmt.setDouble(5, cost);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("\nЗапись успешно создана! ID: " + rs.getInt(1));
            }

        } catch (SQLException e) {
            System.err.println("Ошибка при создании записи: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Некорректный формат даты. Используйте гггг-мм-дд");
        }
    }

    public void viewAllAppointments() {
        String sql = "SELECT * FROM appointments ORDER BY appointment_date DESC";

        try (Connection conn = DBconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\nВсе записи на обслуживание:");
            printAppointmentsTable(rs);

        } catch (SQLException e) {
            System.err.println("Ошибка при получении записей: " + e.getMessage());
        }
    }

    public void viewCompletedAppointments() {
        String sql = "SELECT * FROM appointments WHERE status = 'Completed' ORDER BY appointment_date DESC";

        try (Connection conn = DBconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\nЗавершенные услуги:");
            printAppointmentsTable(rs);

        } catch (SQLException e) {
            System.err.println("Ошибка при получении записей: " + e.getMessage());
        }
    }

    private void printAppointmentsTable(ResultSet rs) throws SQLException {
        System.out.println("+-----+----------------------+------------------------------+--------------+----------------+------------+");
        System.out.println("| ID  | Клиент               | Услуга                       | Дата         | Статус         | Стоимость  |");
        System.out.println("+-----+----------------------+------------------------------+--------------+----------------+------------+");

        while (rs.next()) {
            System.out.printf("| %-3d | %-20s | %-28s | %-12s | %-14s | %-10.2f |\n",
                    rs.getInt("id"),
                    rs.getString("client_name"),
                    rs.getString("service_description"),
                    rs.getDate("appointment_date"),
                    rs.getString("status"),
                    rs.getDouble("cost")
            );
        }
        System.out.println("+-----+----------------------+------------------------------+--------------+----------------+------------+");
    }

    public void updateAppointmentStatus(Scanner scanner) {
        System.out.print("\nВведите ID записи для обновления: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // consume newline

        System.out.println("Доступные статусы:");
        System.out.println("1. В процессе");
        System.out.println("2. Завершено");
        System.out.println("3. Отменено");
        System.out.print("Выберите новый статус (1-3): ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        String newStatus = switch (choice) {
            case 1 -> "In progress";
            case 2 -> "Completed";
            case 3 -> "Cancelled";
            default -> throw new IllegalArgumentException("Неверный выбор статуса");
        };

        String sql = "UPDATE appointments SET status = ? WHERE id = ?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newStatus);
            pstmt.setInt(2, id);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Статус успешно изменен на: " + newStatus);
            } else {
                System.out.println("Запись с ID " + id + " не найдена.");
            }

        } catch (SQLException e) {
            System.err.println("Ошибка при обновлении статуса: " + e.getMessage());
        }
    }

    public void updateAppointmentCost(Scanner scanner) {
        System.out.print("\nВведите ID записи: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // consume newline
        System.out.print("Новая стоимость: ");
        double newCost = scanner.nextDouble();
        scanner.nextLine(); // consume newline

        String sql = "UPDATE appointments SET cost = ? WHERE id = ?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, newCost);
            pstmt.setInt(2, id);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Стоимость успешно обновлена!");
            } else {
                System.out.println("Запись с ID " + id + " не найдена.");
            }

        } catch (SQLException e) {
            System.err.println("Ошибка при обновлении стоимости: " + e.getMessage());
        }
    }
}