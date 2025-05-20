package service;

import model.*;
import DB.DBconnection;
import java.sql.*;

public class AuthService {
    public static User authenticate(String name, String password) {
        String sql = "SELECT name, role FROM users WHERE name = ? AND password = ?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String dbUsername = rs.getString("name");
                    String role = rs.getString("role");

                    // Verify we got a valid user (password already verified by SQL query)
                    if (dbUsername != null) {
                        return createUser(dbUsername, password, role);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error during authentication: " + e.getMessage());

        }
        return null;
    }

    private static User createUser(String username, String password, String role) {
        if (role == null) return null;

        switch (role.toUpperCase()) {
            case "OWNER":
                return new Owner(username, password);
            case "MANAGER":
                return new Manager(username, password);
            case "MECHANIC":
                return new Mechanic(username, password);
            case "PARTS_SPECIALIST":
                return new PartsSpecialist(username, password);
            case "ACCOUNTANT":
                return new Accountant(username, password);
            default:
                return null;
        }
    }
}