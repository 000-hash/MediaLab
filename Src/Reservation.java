public class Reservation implements Displayable {
    private String id;
    private Student student;
    private Equipment equipment;
    private int days;
    private ReservationStatus status;
    private double finalCost;

    public Reservation(String id, Student student, Equipment equipment, int days, DiscountPolicy discountPolicy) {
        this.id = id;
        this.student = student;
        this.equipment = equipment;
        this.days = days;
        this.status = ReservationStatus.ACTIVE;
        this.finalCost = calculateTotalCost(discountPolicy);
    }

    public double calculateTotalCost(DiscountPolicy discountPolicy) {
        double priceBeforeDiscount = equipment.calculateDailyPrice() * days;
        return discountPolicy.applyDiscount(student, priceBeforeDiscount);
    }

    public String getId() {
        return id;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public Student getStudent() {
        return student;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public double getFinalCost() {
        return finalCost;
    }

    public void returnReservation() {
        status = ReservationStatus.RETURNED;
        equipment.setAvailable(true);
    }

    @Override
    public String getDisplayText() {
        return id + " | student: " + student.getFullName()
                + " | sprzęt: " + equipment.getName()
                + " | dni: " + days
                + " | koszt: " + String.format("%.2f", finalCost) + " PLN"
                + " | status: " + status;
    }
}