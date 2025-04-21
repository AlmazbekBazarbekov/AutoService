package com.autoservice.model;

import com.autoservice.service.FileService;
import java.util.List;
import java.util.Scanner;

public class Mechanic extends User {
    public Mechanic(String username, String password) {
        super(username, password, "MECHANIC");
    }

    @Override
    public void showMenu() {
        System.out.println("\n=== Меню механика ===");
        System.out.println("1. Мои текущие задания");
        System.out.println("2. Обновить статус ремонта");
        System.out.println("3. Записать использованные запчасти");
        System.out.println("4. Отметить как завершенное");
        System.out.println("5. Выход");
    }

    public void viewMyTasks() {
        List<String> appointments = FileService.readFile("data/appointments.csv");
        System.out.println("\nМои задания:");
        for (String app : appointments) {
            String[] data = app.split(",");
            if (data.length > 6 && data[6].equals(this.getUsername())) {
                System.out.printf("ID: %s | Авто: %s | Услуга: %s | Статус: %s\n",
                        data[0], data[2], data[3], data[5]);
            }
        }
    }

    public void updateRepairStatus(Scanner scanner) {
        System.out.print("Введите ID записи: ");
        String id = scanner.nextLine();

        List<String> appointments = FileService.readFile("data/appointments.csv");
        boolean found = false;

        for (int i = 0; i < appointments.size(); i++) {
            String[] data = appointments.get(i).split(",");
            if (data[0].equals(id)) {
                System.out.print("Новый статус (В работе/Завершено/Ожидает запчастей): ");
                String newStatus = scanner.nextLine();
                data[5] = newStatus;
                appointments.set(i, String.join(",", data));
                found = true;
                break;
            }
        }

        if (found) {
            FileService.writeFile("data/appointments.csv", appointments);
            System.out.println("Статус обновлен!");
        } else {
            System.out.println("Запись не найдена!");
        }
    }
}
