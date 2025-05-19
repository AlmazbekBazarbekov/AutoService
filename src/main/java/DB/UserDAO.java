package DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    public static void createUser(String name, String role, String Password, int salary) {
        if (userExists(name)) {
            System.out.println("User with name '" + name + "' already exists!");
            return;
        }
        String sql = "INSERT INTO users (name, role, password, salary) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, role);
            pstmt.setString(3, Password);
            pstmt.setInt(4, salary);
            pstmt.executeUpdate();
            System.out.println("User created successfully!");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    private static boolean userExists(String name) {
        String sql = "SELECT COUNT(*) FROM users WHERE name = ?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.out.println("Error checking user existence: " + e.getMessage());
        }
        return false;
    }
    public static void getUser(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Role: " + rs.getString("role"));
                System.out.println("Password: " + rs.getString("password"));
                System.out.println("Salary: " + rs.getInt("salary"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void getAllUsers() {
        String sql = "SELECT * FROM users";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Role: " + rs.getString("role"));
                System.out.println("Password: " + rs.getString("password"));
                System.out.println("Salary: " + rs.getInt("salary"));
                System.out.println("---------------------");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void updateUser(int id, int newSalary) {
        String sql = "UPDATE users SET salary = ? WHERE id = ?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, newSalary);
            pstmt.setInt(2, id);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("User updated successfully!");
            } else {
                System.out.println("No user found with ID: " + id);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("User deleted successfully!");
            } else {
                System.out.println("No user found with ID: " + id);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
