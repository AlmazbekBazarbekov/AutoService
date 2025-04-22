package model;

import main.java.service.FileService;
import java.util.List;
import java.util.Scanner;

public class PartsSpecialist extends User {
    public PartsSpecialist(String username, String password) {
        super(username, password, "PARTS_SPECIALIST");
    }

    @Override
    public void showMenu() {
        System.out.println("\n=== Меню специалиста по запчастям ===");
        System.out.println("1. Просмотреть склад");
        System.out.println("2. Добавить запчасти");
        System.out.println("3. Списать запчасти");
        System.out.println("4. Заказать у поставщика");
        System.out.println("5. Выход");
    }

    public void viewInventory() {
        List<String> inventory = FileService.readFile("data/inventory.csv");
        System.out.println("\nТекущие запасы:");
        for (String item : inventory) {
            String[] data = item.split(",");
            System.out.printf("%s: %s шт. | Цена: %s руб.\n",
                    data[0], data[1], data[2]);
        }
    }

    public void addParts(Scanner scanner) {
        System.out.print("Название запчасти: ");
        String part = scanner.nextLine();
        System.out.print("Количество: ");
        String quantity = scanner.nextLine();
        System.out.print("Цена за единицу: ");
        String price = scanner.nextLine();

        String record = String.join(",", part, quantity, price);
        FileService.appendToFile("data/inventory.csv", record);
        System.out.println("Запчасти добавлены на склад!");
    }
}
