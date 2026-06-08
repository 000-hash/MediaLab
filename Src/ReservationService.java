import java.util.Comparator;
import java.util.List;

public class ReservationService {
    private List<Student> students;
    private List<Equipment> equipmentList;
    private List<Reservation> reservations;
    private DiscountPolicy discountPolicy;
    private int nextReservationNumber = 1;

    public ReservationService(List<Student> students, List<Equipment> equipmentList, DiscountPolicy discountPolicy) {
        this.students = students;
        this.equipmentList = equipmentList;
        this.discountPolicy = discountPolicy;
        this.reservations = new java.util.ArrayList<>();
    }

    public Reservation createReservation(String studentId, String equipmentId, int days) {
        Student student = findStudentById(studentId);
        Equipment equipment = findEquipmentById(equipmentId);

        if (student == null) {
            System.out.println("Błąd: student nie istnieje.");
            return null;
        }

        if (equipment == null) {
            System.out.println("Błąd: sprzęt nie istnieje.");
            return null;
        }

        if (!equipment.isAvailable()) {
            System.out.println("Błąd: sprzęt " + equipmentId + " nie jest dostępny.");
            return null;
        }

        if (days < 1 || days > 14) {
            System.out.println("Błąd: liczba dni musi być od 1 do 14.");
            return null;
        }

        String reservationId = String.format("R%03d", nextReservationNumber);
        nextReservationNumber++;

        Reservation reservation = new Reservation(reservationId, student, equipment, days, discountPolicy);
        reservations.add(reservation);

        equipment.setAvailable(false);

        System.out.println("Utworzono rezerwację " + reservationId + ".");
        System.out.println("Sprzęt: " + equipment.getName());
        System.out.println("Koszt: " + String.format("%.2f", reservation.getFinalCost()) + " PLN");
        System.out.println("Status: " + reservation.getStatus());

        return reservation;
    }

    public void returnEquipment(String reservationId) {
        Reservation reservation = findReservationById(reservationId);

        if (reservation == null) {
            System.out.println("Błąd: rezerwacja nie istnieje.");
            return;
        }

        if (reservation.getStatus() != ReservationStatus.ACTIVE) {
            System.out.println("Błąd: rezerwacja nie jest aktywna.");
            return;
        }

        reservation.returnReservation();

        int points = (int) (reservation.getFinalCost() / 10);
        reservation.getStudent().addLoyaltyPoints(points);

        System.out.println("Zwrócono sprzęt. Student otrzymał " + points + " punktów lojalnościowych.");
    }

    public void printActiveReservations() {
        List<Reservation> activeReservations = reservations.stream()
                .filter(reservation -> reservation.getStatus() == ReservationStatus.ACTIVE)
                .toList();

        if (activeReservations.isEmpty()) {
            System.out.println("Brak aktywnych rezerwacji.");
            return;
        }

        activeReservations.forEach(reservation ->
                System.out.println(reservation.getDisplayText()));
    }

    public void printReport() {
        List<Reservation> returnedReservations = reservations.stream()
                .filter(reservation -> reservation.getStatus() == ReservationStatus.RETURNED)
                .toList();

        System.out.println("Zakończone rezerwacje:");

        if (returnedReservations.isEmpty()) {
            System.out.println("Brak zakończonych rezerwacji.");
        } else {
            returnedReservations.forEach(reservation ->
                    System.out.println(reservation.getDisplayText()));
        }

        double totalIncome = returnedReservations.stream()
                .mapToDouble(Reservation::getFinalCost)
                .sum();

        System.out.println("Łączny przychód: " + String.format("%.2f", totalIncome) + " PLN");

        Student bestStudent = findStudentWithMostPoints();

        if (bestStudent != null) {
            System.out.println("Student z największą liczbą punktów:");
            System.out.println(bestStudent.getDisplayText());
        }
    }

    private Student findStudentById(String id) {
        return students.stream()
                .filter(student -> student.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    private Equipment findEquipmentById(String id) {
        return equipmentList.stream()
                .filter(equipment -> equipment.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    private Reservation findReservationById(String id) {
        return reservations.stream()
                .filter(reservation -> reservation.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    private Student findStudentWithMostPoints() {
        return students.stream()
                .max(Comparator.comparingInt(Student::getLoyaltyPoints))
                .orElse(null);
    }
}