import java.util.*;

public class Problem8_ParkingLotOpenAddressing {
    enum Status { EMPTY, OCCUPIED, DELETED }
    static class Spot { String plate; Status status; Spot() { status = Status.EMPTY; } }

    private final Spot[] table;
    private final int capacity;

    public Problem8_ParkingLotOpenAddressing(int capacity) {
        this.capacity = capacity; this.table = new Spot[capacity];
        for (int i=0;i<capacity;i++) table[i]=new Spot();
    }

    private int hash(String plate) { return Math.abs(plate.hashCode()) % capacity; }

    public int parkVehicle(String plate) {
        int h = hash(plate);
        for (int i=0;i<capacity;i++) {
            int idx = (h + i) % capacity;
            if (table[idx].status == Status.EMPTY || table[idx].status == Status.DELETED) {
                table[idx].plate = plate; table[idx].status = Status.OCCUPIED; return idx;
            }
        }
        return -1; // full
    }

    public boolean exitVehicle(String plate) {
        int h = hash(plate);
        for (int i=0;i<capacity;i++) {
            int idx = (h + i) % capacity;
            if (table[idx].status == Status.EMPTY) return false;
            if (table[idx].status == Status.OCCUPIED && plate.equals(table[idx].plate)) {
                table[idx].status = Status.DELETED; table[idx].plate = null; return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        Problem8_ParkingLotOpenAddressing p = new Problem8_ParkingLotOpenAddressing(500);
        System.out.println(p.parkVehicle("ABC-1234"));
        System.out.println(p.parkVehicle("ABC-1235"));
        System.out.println(p.exitVehicle("ABC-1234"));
    }
}
