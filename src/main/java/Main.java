import DB.UserDAO;
import model.*;
import service.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        UserDAO.createUser("Amish", "Accountant", "Amish228", 130000);


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
        int viewEmployeesOption = 1;
        switch (choice) {
            case 1:
                owner.viewEmployees();
                return true;
            case 2:
                System.out.print("Имя нового сотрудника: ");
                String name = scanner.nextLine();
                System.out.print("Пароль: ");
                String pwd = scanner.nextLine();
                System.out.print("Роль: ");
                String role = scanner.nextLine();
                System.out.print("Заработная плата: ");
                int salary = Integer.parseInt(scanner.nextLine());
                owner.hireEmployee(name, pwd, role, salary);
                return true;
            case 3:
                owner.fireEmployee(scanner);
                return true;
            case 4:
                PartsSpecialist.setPartPrice(scanner);
                return true;
            case 5:
                Accountant.generateFinancialReport();
                return true;
            case 6:
                return false;
            default:
                System.out.println("Неизвестная команда. Введите число от 1 до 6");
                return true;
        }
    }

    private static boolean handleManagerMenu(Manager manager, int choice, Scanner scanner) {
        switch (choice) {
            case 1:
                manager.createAppointment(scanner);
                return true;
            case 2:
                manager.viewAllAppointments();
                return true;
            case 3:
                manager.updateAppointmentStatus(scanner);
                return true;
            case 4:
                manager.updateAppointmentCost(scanner);
                return true;
            case 5:
                manager.viewCompletedAppointments();
                return true;
            case 6:
                return false;
            default:
                System.out.println("Неизвестная команда.");
                return true;
        }
    }

    private static boolean handleMechanicMenu(Mechanic mechanic, int choice, Scanner scanner) {
        switch (choice) {
            case 1:
                mechanic.viewCurrentTasks();
                return true;
            case 2:
                mechanic.updateRepairStatus(scanner);
                return true;
            case 3:
                mechanic.recordUsedParts(scanner);
                return true;
            case 4:
                mechanic.markAsCompleted(scanner);
                return true;
            case 5:
                return false;
            default:
                System.out.println("Неизвестная команда. Введите число от 1 до 5");
                return true;
        }
    }

    private static boolean handlePartsSpecialistMenu(PartsSpecialist specialist, int choice, Scanner scanner) {
        switch (choice) {
            case 1:
                PartsSpecialist.viewInventory();
                return true;
            case 2:
                specialist.addParts(scanner);
                return true;
            case 3:
                specialist.removeParts(scanner);
                return true;
            case 4:
                specialist.orderFromSupplier(scanner);
                return true;
            case 5:
                return false;
            default:
                System.out.println("Неизвестная команда. Введите число от 1 до 5");
                return true;
        }
    }

    private static boolean handleAccountantMenu(Accountant accountant, int choice, Scanner scanner) {
        switch (choice) {
            case 1:
                Accountant.generateFinancialReport();
                return true;
            case 2:
                accountant.calculateSalary(scanner);
                return true;
            case 3:
                Accountant.recordExpense(scanner);
                return true;
            case 4:
                accountant.recordIncome(scanner);
                return true;
            case 5:
                return false;
            default:
                System.out.println("Unknown command. Please enter 1-5");
                return true;
        }
    }
}