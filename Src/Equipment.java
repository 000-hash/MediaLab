public abstract class Equipment implements Displayable {
    private String id;
    private String name;
    private double baseDailyPrice;
    private boolean available;

    public Equipment(String id, String name, double baseDailyPrice) {
        this.id = id;
        this.name = name;
        this.baseDailyPrice = baseDailyPrice;
        this.available = true;
    }

    public abstract double calculateDailyPrice();

    public abstract String getDetails();

    public abstract String getType();

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getBaseDailyPrice() {
        return baseDailyPrice;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String getDisplayText() {
        String status = available ? "dostępny" : "niedostępny";

        return id + " | " + getType() + " | " + name
                + " | cena za dzień: " + String.format("%.2f", calculateDailyPrice()) + " PLN"
                + " | " + status
                + " | " + getDetails();
    }
}