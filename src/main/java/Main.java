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
        final int viewEmployeesOption = 1;
        final int hireEmployeesOption = 2;
        final int fireEmployeesOption = 3;
        final int setPricesOption = 4;
        final int generateFinancialReportOption = 5;
        final int exitOption = 6;
        switch (choice) {
            case viewEmployeesOption:
                owner.viewEmployees();
                return true;
            case hireEmployeesOption:
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
            case fireEmployeesOption:
                owner.fireEmployee(scanner);
                return true;
            case setPricesOption:
                PartsSpecialist.setPartPrice(scanner);
                return true;
            case generateFinancialReportOption:
                Accountant.generateFinancialReport();
                return true;
            case exitOption:
                return false;
            default:
                System.out.println("Неизвестная команда. Введите число от 1 до 6");
                return true;
        }
    }

    private static boolean handleManagerMenu(Manager manager, int choice, Scanner scanner) {
        final int createAppointmentOption = 1;
        final int viewAllAppointmentsOption = 2;
        final int updateAppointmentsOption = 3;
        final int updateAppointmentsCostOption = 4;
        final int viewCompletedOption = 5;
        final int exitOption = 6;
        switch (choice) {
            case createAppointmentOption:
                manager.createAppointment(scanner);
                return true;
            case viewAllAppointmentsOption:
                manager.viewAllAppointments();
                return true;
            case updateAppointmentsOption:
                manager.updateAppointmentStatus(scanner);
                return true;
            case updateAppointmentsCostOption:
                manager.updateAppointmentCost(scanner);
                return true;
            case viewCompletedOption:
                manager.viewCompletedAppointments();
                return true;
            case exitOption:
                return false;
            default:
                System.out.println("Неизвестная команда.");
                return true;
        }
    }

    private static boolean handleMechanicMenu(Mechanic mechanic, int choice, Scanner scanner) {
        final int viewCurrentTasksOption = 1;
        final int updateRepairStatusOption = 2;
        final int recordUsedPartsOption = 3;
        final int markAsDoneOption = 4;
        final int exitOption = 5;
        switch (choice) {
            case viewCurrentTasksOption:
                mechanic.viewCurrentTasks();
                return true;
            case updateRepairStatusOption:
                mechanic.updateRepairStatus(scanner);
                return true;
            case recordUsedPartsOption:
                mechanic.recordUsedParts(scanner);
                return true;
            case markAsDoneOption:
                mechanic.markAsCompleted(scanner);
                return true;
            case exitOption:
                return false;
            default:
                System.out.println("Неизвестная команда. Введите число от 1 до 5");
                return true;
        }
    }

    private static boolean handlePartsSpecialistMenu(PartsSpecialist specialist, int choice, Scanner scanner) {
        final int viewInventoryOption = 1;
        final int addPartsOption = 2;
        final int removePartsOption = 3;
        final int orderFromSupplierOption = 4;
        final int exitOption = 5;
        switch (choice) {
            case viewInventoryOption:
                PartsSpecialist.viewInventory();
                return true;
            case addPartsOption:
                specialist.addParts(scanner);
                return true;
            case removePartsOption:
                specialist.removeParts(scanner);
                return true;
            case orderFromSupplierOption:
                specialist.orderFromSupplier(scanner);
                return true;
            case exitOption:
                return false;
            default:
                System.out.println("Неизвестная команда. Введите число от 1 до 5");
                return true;
        }
    }

    private static boolean handleAccountantMenu(Accountant accountant, int choice, Scanner scanner) {
        final int generateFinancialReportOption = 1;
        final int calculateSalaryOption = 2;
        final int recordExpensesOption = 3;
        final int recordIncomeOption = 4;
        final int exitOption = 5;
        switch (choice) {
            case generateFinancialReportOption:
                Accountant.generateFinancialReport();
                return true;
            case calculateSalaryOption:
                accountant.calculateSalary(scanner);
                return true;
            case recordExpensesOption:
                Accountant.recordExpense(scanner);
                return true;
            case recordIncomeOption:
                accountant.recordIncome(scanner);
                return true;
            case exitOption:
                return false;
            default:
                System.out.println("Неизвестна команда. Введите число от 1-5.");
                return true;
        }
    }
}