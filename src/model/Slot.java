package model;

public class Slot {

    private int slotId;
    private String zone;              // e.g., "A" block
    private String shelfNumber;       // e.g., "A-1", "B-3"
    private String compositionTag;    // which composition is stored here
    private int capacity;             // max quantity slot can hold
    private int currentQuantity;      // current filled qty

    public Slot() {}

    public Slot(String zone, String shelfNumber, int capacity) {
        this.zone = zone;
        this.shelfNumber = shelfNumber;
        this.capacity = capacity;
        this.currentQuantity = 0;
        this.compositionTag = null;
    }

    public int getSlotId() {
        return slotId;
    }

    public void setSlotId(int slotId) {
        this.slotId = slotId;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getShelfNumber() {
        return shelfNumber;
    }

    public void setShelfNumber(String shelfNumber) {
        this.shelfNumber = shelfNumber;
    }

    public String getCompositionTag() {
        return compositionTag;
    }

    public void setCompositionTag(String compositionTag) {
        this.compositionTag = compositionTag;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCurrentQuantity() {
        return currentQuantity;
    }

    public void setCurrentQuantity(int currentQuantity) {
        this.currentQuantity = currentQuantity;
    }

    public int freeSpace() {
        return capacity - currentQuantity;
    }

    public boolean isEmpty() {
        return currentQuantity <= 0;
    }

    @Override
    public String toString() {
        return "Slot{" +
                "slotId=" + slotId +
                ", zone='" + zone + '\'' +
                ", shelfNumber='" + shelfNumber + '\'' +
                ", compositionTag='" + compositionTag + '\'' +
                ", capacity=" + capacity +
                ", currentQuantity=" + currentQuantity +
                '}';
    }
}
