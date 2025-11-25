package model;

import java.time.LocalDate;

public class Batch {

    private int batchId;
    private int medicineId;
    private String composition;       // e.g., "Paracetamol 500mg"
    private int quantity;
    private LocalDate mfgDate;
    private LocalDate expDate;
    private Integer slotId;           // assigned shelf/slot

    public Batch() {}

    public Batch(int medicineId, String composition, int quantity,
                 LocalDate mfgDate, LocalDate expDate) {

        this.medicineId = medicineId;
        this.composition = composition != null ? composition.trim() : null;
        this.quantity = quantity;
        this.mfgDate = mfgDate;
        this.expDate = expDate;
    }

    public int getBatchId() {
        return batchId;
    }

    public void setBatchId(int batchId) {
        this.batchId = batchId;
    }

    public int getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition != null ? composition.trim() : null;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDate getMfgDate() {
        return mfgDate;
    }

    public void setMfgDate(LocalDate mfgDate) {
        this.mfgDate = mfgDate;
    }

    public LocalDate getExpDate() {
        return expDate;
    }

    public void setExpDate(LocalDate expDate) {
        this.expDate = expDate;
    }

    public Integer getSlotId() {
        return slotId;
    }

    public void setSlotId(Integer slotId) {
        this.slotId = slotId;
    }

    @Override
    public String toString() {
        return "Batch{" +
                "batchId=" + batchId +
                ", medicineId=" + medicineId +
                ", composition='" + composition + '\'' +
                ", quantity=" + quantity +
                ", mfgDate=" + mfgDate +
                ", expDate=" + expDate +
                ", slotId=" + slotId +
                '}';
    }
}
