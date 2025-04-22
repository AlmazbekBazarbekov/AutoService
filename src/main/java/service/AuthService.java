package service;

import model.*;
import java.util.List;

public class AuthService {
    public static User authenticate(String username, String password) {
        List<String> users = FileService.readFile("data/users.csv");

        for (String userLine : users) {
            String[] userData = userLine.split(",");
            if (userData[0].equals(username) && userData[1].equals(password)) {
                return createUser(userData[0], userData[1], userData[2]);
            }
        }
        return null;
    }

    private static User createUser(String username, String password, String role) {
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
