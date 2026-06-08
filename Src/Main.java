import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        List<Student> students = createStudents();
        List<Equipment> equipmentList = createEquipment();

        DiscountPolicy discountPolicy = new LoyaltyDiscountPolicy();
        ReservationService reservationService = new ReservationService(students, equipmentList, discountPolicy);

        int choice;

        do {
            printMenu();
            choice = readInt("Wybór: ");

            switch (choice) {
                case 1:
                    printStudents(students);
                    break;
                case 2:
                    printEquipment(equipmentList);
                    break;
                case 3:
                    createReservation(reservationService);
                    break;
                case 4:
                    returnEquipment(reservationService);
                    break;
                case 5:
                    reservationService.printActiveReservations();
                    break;
                case 6:
                    reservationService.printReport();
                    break;
                case 0:
                    System.out.println("Koniec programu.");
                    break;
                default:
                    System.out.println("Nieprawidłowy wybór.");
            }

        } while (choice != 0);
    }

    private static List<Student> createStudents() {
        List<Student> students = new ArrayList<>();

        students.add(new Student("S001", "Anna Kowalska", "12c", 120));
        students.add(new Student("S002", "Marek Nowak", "12c", 40));
        students.add(new Student("S003", "Julia Zielińska", "13a", 0));

        return students;
    }

    private static List<Equipment> createEquipment() {
        List<Equipment> equipmentList = new ArrayList<>();

        equipmentList.add(new LaptopSet("E001", "Lenovo ThinkPad Lab", 80, 32, true));
        equipmentList.add(new LaptopSet("E002", "Dell XPS Demo", 100, 16, false));
        equipmentList.add(new CameraKit("E003", "Sony Content Kit", 90, 3, true));
        equipmentList.add(new CameraKit("E004", "Canon Interview Kit", 70, 1, true));

        return equipmentList;
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("1. Wyświetl studentów");
        System.out.println("2. Wyświetl sprzęt");
        System.out.println("3. Utwórz rezerwację");
        System.out.println("4. Zwróć sprzęt");
        System.out.println("5. Pokaż aktywne rezerwacje");
        System.out.println("6. Pokaż raport");
        System.out.println("0. Zakończ");
    }

    private static void printStudents(List<Student> students) {
        students.forEach(student -> System.out.println(student.getDisplayText()));
    }

    private static void printEquipment(List<Equipment> equipmentList) {
        equipmentList.forEach(equipment -> System.out.println(equipment.getDisplayText()));
    }

    private static void createReservation(ReservationService reservationService) {
        System.out.print("Podaj id studenta: ");
        String studentId = scanner.nextLine();

        System.out.print("Podaj id sprzętu: ");
        String equipmentId = scanner.nextLine();

        int days = readInt("Podaj liczbę dni: ");

        reservationService.createReservation(studentId, equipmentId, days);
    }

    private static void returnEquipment(ReservationService reservationService) {
        System.out.print("Podaj id rezerwacji: ");
        String reservationId = scanner.nextLine();

        reservationService.returnEquipment(reservationId);
    }

    private static int readInt(String message) {
        while (true) {
            System.out.print(message);

            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Błąd: wpisz liczbę.");
            }
        }
    }
}