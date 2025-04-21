package com.autoservice.model;

import com.autoservice.service.FileService;
import java.util.List;
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

    public void generateFinancialReport() {
        List<String> appointments = FileService.readFile("data/appointments.csv");
        List<String> finances = FileService.readFile("data/finances.csv");

        double totalIncome = 0;
        double totalExpenses = 0;

        for (String finance : finances) {
            String[] data = finance.split(",");
            if (data[1].equals("Доход")) {
                totalIncome += Double.parseDouble(data[2]);
            } else {
                totalExpenses += Double.parseDouble(data[2]);
            }
        }

        System.out.println("\n=== Финансовый отчет ===");
        System.out.printf("Всего завершенных услуг: %d\n", appointments.stream()
                .filter(a -> a.split(",")[5].equals("Завершено")).count());
        System.out.printf("Общий доход: %.2f руб.\n", totalIncome);
        System.out.printf("Общие расходы: %.2f руб.\n", totalExpenses);
        System.out.printf("Прибыль: %.2f руб.\n", (totalIncome - totalExpenses));
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

        String record = String.join(",",
                "Зарплата",
                "Расход",
                String.valueOf(salary),
                employee
        );
        FileService.appendToFile("data/finances.csv", record);
    }
}
