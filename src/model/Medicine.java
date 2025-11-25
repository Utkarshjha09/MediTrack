package model;

public class Medicine {

    private int medicineId;
    private String name;
    private String manufacturer;
    private String supplier;

    public Medicine() {}

    public Medicine(String name, String manufacturer, String supplier) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.supplier = supplier;
    }

    public int getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    @Override
    public String toString() {
        return "Medicine{" +
                "id=" + medicineId +
                ", name='" + name + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", supplier='" + supplier + '\'' +
                '}';
    }
}
