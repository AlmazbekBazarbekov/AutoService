package model;

import DB.DBconnection;
import java.sql.*;
import java.util.Scanner;

public class Accountant extends User {
    public Accountant(String username, String password) {
        super(username, password, "ACCOUNTANT");
    }

    @Override
    public void showMenu() {
        System.out.println("\n=== Меню бухгалтера ===");
        System.out.println("1. Финансовый отчет");
        System.out.println("2. Расчет зарплаты");
        System.out.println("3. Учет расходов");
        System.out.println("4. Учет доходов");
        System.out.println("5. Выход");
    }

    public static void generateFinancialReport() {
        try (Connection connection = DBconnection.getConnection()) {
            // Get completed appointments count
            String appointmentsQuery = "SELECT COUNT(*) FROM appointments WHERE status = 'Completed'";
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(appointmentsQuery)) {
                rs.next();
                int completedAppointments = rs.getInt(1);
                System.out.println("\n=== Финансовый отчет ===");
                System.out.printf("Всего завершенных услуг: %d\n", completedAppointments);
            }

            // Calculate total income and expenses
            String financesQuery = "SELECT type, SUM(amount) FROM finances GROUP BY type";
            double totalIncome = 0;
            double totalExpenses = 0;

            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(financesQuery)) {
                while (rs.next()) {
                    String type = rs.getString(1);
                    double amount = rs.getDouble(2);
                    System.out.println("Тип: " + type + ", Сумма: " + amount);
                    if (type.equals("Income")) {
                        totalIncome = amount;
                    } else {
                        totalExpenses = amount;
                    }
                }
            }

            System.out.printf("Общий доход: %.2f руб.\n", totalIncome);
            System.out.printf("Общие расходы: %.2f руб.\n", totalExpenses);
            System.out.printf("Прибыль: %.2f руб.\n", (totalIncome - totalExpenses));

        } catch (SQLException e) {
            System.err.println("Ошибка при генерации финансового отчета: " + e.getMessage());
        }
    }

    public void calculateSalary(Scanner scanner) {
        System.out.print("Имя сотрудника: ");
        String employee = scanner.nextLine();
        System.out.print("Количество отработанных часов: ");
        double hours = Double.parseDouble(scanner.nextLine());
        System.out.print("Ставка в час: ");
        double rate = Double.parseDouble(scanner.nextLine());

        double salary = hours * rate;
        System.out.printf("Зарплата %s: %.2f руб.\n", employee, salary);

        try (Connection connection = DBconnection.getConnection()) {
            String insertQuery = "INSERT INTO finances (description, type, amount, employee) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(insertQuery)) {
                pstmt.setString(1, "Зарплата");
                pstmt.setString(2, "Расход");
                pstmt.setDouble(3, salary);
                pstmt.setString(4, employee);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при сохранении данных о зарплате: " + e.getMessage());
        }
    }
    public void recordIncome(Scanner scanner) {
        System.out.print("Income amount: ");
        double amount = Double.parseDouble(scanner.nextLine());
        System.out.print("Income source: ");
        String source = scanner.nextLine();

        String sql = "INSERT INTO finances (description, type, amount, transaction_date) " +
                "VALUES (?, 'Income', ?, CURRENT_DATE)";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, source);
            pstmt.setDouble(2, amount);
            pstmt.executeUpdate();

            System.out.printf("Recorded income: %.2f RUB from %s\n", amount, source);

        } catch (SQLException e) {
            System.err.println("Error recording income: " + e.getMessage());
        }
    }
    public static void recordExpense(Scanner scanner) {
        System.out.print("Expense amount: ");
        double amount = Double.parseDouble(scanner.nextLine());
        System.out.print("Expense source: ");
        String description = scanner.nextLine();
        System.out.print("Employee's name: ");
        String employee = scanner.nextLine();
        String sql = "INSERT INTO finances (description, type, amount, employee, transaction_date) " +
                "VALUES (?, 'Expense', ?, ?, CURRENT_DATE)";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, description);
            pstmt.setDouble(2, amount);
            pstmt.setString(3, employee);
            pstmt.executeUpdate();

            System.out.printf("Recorded expense: %.2f RUB for %s\n", amount, description);

        } catch (SQLException e) {
            System.err.println("Error recording expense: " + e.getMessage());
        }
    }
}