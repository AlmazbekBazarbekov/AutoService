package model;

import main.java.service.FileService;
import java.util.List;

public class Owner extends User {
    public Owner(String username, String password) {
        super(username, password, "OWNER");
    }

    @Override
    public void showMenu() {
        System.out.println("\n=== Меню владельца ===");
        System.out.println("1. Список сотрудников");
        System.out.println("2. Нанять сотрудника");
        System.out.println("3. Установить цены");
        System.out.println("4. Финансовый отчет");
        System.out.println("5. Выход");
    }

    public void hireEmployee(String username, String password, String role) {
        String employeeData = String.join(",", username, password, role);
        FileService.appendToFile("data/users.csv", employeeData);
        System.out.println("Сотрудник " + username + " нанят!");
    }

    public List<String> viewEmployees() {
        return FileService.readFile("data/users.csv");
    }
}
