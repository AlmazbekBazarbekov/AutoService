package com.autoservice.model;

import com.autoservice.service.FileService;
import java.util.List;
import java.util.Scanner;

public class Manager extends User {
    public Manager(String username, String password) {
        super(username, password, "MANAGER");
    }

    @Override
    public void showMenu() {
        System.out.println("\n=== Меню менеджера ===");
        System.out.println("1. Создать запись на обслуживание");
        System.out.println("2. Просмотреть записи");
        System.out.println("3. Изменить статус записи");
        System.out.println("4. Консультация клиента");
        System.out.println("5. Выход");
    }

    public void createAppointment(Scanner scanner) {
        System.out.println("\nСоздание новой записи:");
        System.out.print("Имя клиента: ");
        String client = scanner.nextLine();
        System.out.print("Модель авто: ");
        String car = scanner.nextLine();
        System.out.print("Услуга: ");
        String service = scanner.nextLine();
        System.out.print("Дата (гггг-мм-дд): ");
        String date = scanner.nextLine();

        // Генерируем ID (в реальном проекте нужно более надежное решение)
        int newId = FileService.readFile("data/appointments.csv").size() + 1;

        String record = String.join(",",
                String.valueOf(newId),
                client,
                car,
                service,
                date,
                "Запланировано" // Статус по умолчанию
        );

        FileService.appendToFile("data/appointments.csv", record);
        System.out.println("Запись успешно создана! ID: " + newId);
    }

    public void viewAppointments() {
        List<String> appointments = FileService.readFile("data/appointments.csv");
        System.out.println("\nСписок записей:");
        for (String app : appointments) {
            String[] data = app.split(",");
            System.out.printf("ID: %s | Клиент: %s | Авто: %s | Услуга: %s | Дата: %s | Статус: %s\n",
                    data[0], data[1], data[2], data[3], data[4], data[5]);
        }
    }
}
