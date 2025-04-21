import com.autoservice.model.*;
import com.autoservice.service.AuthService;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== АВТОСЕРВИС ===");
        System.out.print("Логин: ");
        String username = scanner.nextLine();
        System.out.print("Пароль: ");
        String password = scanner.nextLine();

        User user = AuthService.authenticate(username, password);

        if (user != null) {
            System.out.println("\nДобро пожаловать, " + user.getUsername());
            user.showMenu();

            boolean running = true;
            while (running) {
                System.out.print("\nВыберите действие: ");
                int choice = Integer.parseInt(scanner.nextLine());

                switch (user.getRole()) {
                    case "OWNER":
                        running = handleOwnerMenu((Owner) user, choice, scanner);
                        break;
                    case "MANAGER":
                        running = handleManagerMenu((Manager) user, choice, scanner);
                        break;
                    case "MECHANIC":
                        running = handleMechanicMenu((Mechanic) user, choice, scanner);
                        break;
                    case "PARTS_SPECIALIST":
                        running = handlePartsSpecialistMenu((PartsSpecialist) user, choice, scanner);
                        break;
                    case "ACCOUNTANT":
                        running = handleAccountantMenu((Accountant) user, choice, scanner);
                        break;
                }
            }
        } else {
            System.out.println("Неверные учетные данные!");
        }

        scanner.close();
    }

    private static boolean handleOwnerMenu(Owner owner, int choice, Scanner scanner) {
        switch (choice) {
            case 1:
                owner.viewEmployees().forEach(System.out::println);
                return true;
            case 2:
                System.out.print("Имя нового сотрудника: ");
                String name = scanner.nextLine();
                System.out.print("Пароль: ");
                String pwd = scanner.nextLine();
                System.out.print("Роль: ");
                String role = scanner.nextLine();
                owner.hireEmployee(name, pwd, role);
                return true;
            case 5:
                return false;
            default:
                System.out.println("Неизвестная команда");
                return true;
        }
    }

    private static boolean handleManagerMenu(Manager manager, int choice, Scanner scanner) {
        switch (choice) {
            case 1:
                manager.createAppointment(scanner);
                return true;
            case 2:
                manager.viewAppointments();
                return true;
            case 5:
                return false;
            default:
                System.out.println("Неизвестная команда");
                return true;
        }
    }

    private static boolean handleMechanicMenu(Mechanic mechanic, int choice, Scanner scanner) {
        switch (choice) {
            case 1:
                mechanic.viewMyTasks();
                return true;
            case 2:
                mechanic.updateRepairStatus(scanner);
                return true;
            case 3:
                System.out.print("Введите ID записи: ");
                String id = scanner.nextLine();
                System.out.print("Введите использованные запчасти: ");
                String parts = scanner.nextLine();
                // Здесь можно добавить логику учета запчастей
                System.out.println("Запчасти учтены для записи " + id);
                return true;
            case 4:
                System.out.print("Введите ID завершенной работы: ");
                String completeId = scanner.nextLine();
                // Логика отметки о завершении
                System.out.println("Работа " + completeId + " отмечена как завершенная");
                return true;
            case 5:
                return false;
            default:
                System.out.println("Неизвестная команда");
                return true;
        }
    }

    private static boolean handlePartsSpecialistMenu(PartsSpecialist specialist, int choice, Scanner scanner) {
        switch (choice) {
            case 1:
                specialist.viewInventory();
                return true;
            case 2:
                specialist.addParts(scanner);
                return true;
            case 3:
                System.out.print("Введите название запчасти для списания: ");
                String partName = scanner.nextLine();
                System.out.print("Введите количество: ");
                int quantity = Integer.parseInt(scanner.nextLine());
                // Логика списания запчастей
                System.out.printf("Списано %d единиц %s\n", quantity, partName);
                return true;
            case 4:
                System.out.print("Введите запчасть для заказа: ");
                String orderPart = scanner.nextLine();
                System.out.print("Введите количество: ");
                int orderQty = Integer.parseInt(scanner.nextLine());
                // Логика заказа у поставщика
                System.out.printf("Заказано %d единиц %s у поставщика\n", orderQty, orderPart);
                return true;
            case 5:
                return false;
            default:
                System.out.println("Неизвестная команда");
                return true;
        }
    }

    private static boolean handleAccountantMenu(Accountant accountant, int choice, Scanner scanner) {
        switch (choice) {
            case 1:
                accountant.generateFinancialReport();
                return true;
            case 2:
                accountant.calculateSalary(scanner);
                return true;
            case 3:
                System.out.print("Введите сумму расхода: ");
                double expense = Double.parseDouble(scanner.nextLine());
                System.out.print("Введите описание: ");
                String description = scanner.nextLine();
                // Логика учета расхода
                System.out.printf("Учтен расход %.2f руб. на %s\n", expense, description);
                return true;
            case 4:
                System.out.print("Введите сумму дохода: ");
                double income = Double.parseDouble(scanner.nextLine());
                System.out.print("Введите источник: ");
                String source = scanner.nextLine();
                // Логика учета дохода
                System.out.printf("Учтен доход %.2f руб. от %s\n", income, source);
                return true;
            case 5:
                return false;
            default:
                System.out.println("Неизвестная команда");
                return true;
        }
    }
}