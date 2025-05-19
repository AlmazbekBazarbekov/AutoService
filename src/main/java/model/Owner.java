package model;

import DB.DBconnection;
import java.sql.*;
import java.util.Scanner;

public class Owner extends User {
    public Owner(String name, String password) {
        super(name, password, "OWNER");
    }

    @Override
    public void showMenu() {
        System.out.println("\n=== Меню владельца ===");
        System.out.println("1. Список сотрудников");
        System.out.println("2. Нанять сотрудника");
        System.out.println("3. Уволить сотрудника");
        System.out.println("4. Установить цены");
        System.out.println("5. Финансовый отчет");
        System.out.println("6. Выход");
    }

    public void hireEmployee(String name, String password, String role, int salary) {
        String sql = "INSERT INTO users (name, password, role, salary) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, password);
            pstmt.setString(3, role);
            pstmt.setInt(4, salary);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Сотрудник " + name + " нанят!");
            } else {
                System.out.println("Не удалось нанять сотрудника");
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при найме сотрудника: " + e.getMessage());
        }
    }

    public void fireEmployee(Scanner scanner) {
        System.out.print("\nВведите имя сотрудника для увольнения: ");
        String employeeName = scanner.nextLine();

        String sql = "DELETE FROM users WHERE name = ?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, employeeName);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Сотрудник " + employeeName + " уволен.");
            } else {
                System.out.println("Сотрудник с именем " + employeeName + " не найден.");
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при увольнении сотрудника: " + e.getMessage());
        }
    }

    public void viewEmployees() {
        String sql = "SELECT id, name, role, salary FROM users ORDER BY name";

        try (Connection conn = DBconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n=== Список сотрудников ===");
            System.out.printf("%-5s %-20s %-15s %-10s%n", "ID", "Имя", "Должность", "Зарплата");
            System.out.println("--------------------------------------------------");

            while (rs.next()) {
                System.out.printf("%-5d %-20s %-15s %-10d%n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("role"),
                        rs.getInt("salary"));
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при получении списка сотрудников: " + e.getMessage());
        }
    }
}