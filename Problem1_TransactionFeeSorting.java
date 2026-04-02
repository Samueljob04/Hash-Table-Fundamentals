import java.util.*;

public class Problem1_TransactionFeeSorting {
    static class Transaction {
        final String id;
        final double fee;
        final long timestamp; // epoch seconds
        Transaction(String id, double fee, long timestamp) { this.id=id; this.fee=fee; this.timestamp=timestamp; }
        public String toString(){ return id+":"+fee+"@"+timestamp; }
    }

    // Bubble sort by fee ascending. Returns passes and swaps info.
    public static Map<String,Integer> bubbleSortByFee(List<Transaction> list) {
        int n = list.size();
        int passes = 0, swaps = 0;
        for (int i = 0; i < n - 1; i++) {
            passes++;
            boolean swapped = false;
            for (int j = 0; j < n - 1 - i; j++) {
                if (list.get(j).fee > list.get(j+1).fee) {
                    Collections.swap(list, j, j+1);
                    swaps++; swapped = true;
                }
            }
            if (!swapped) break; // early termination
        }
        Map<String,Integer> stats = new HashMap<>();
        stats.put("passes", passes); stats.put("swaps", swaps);
        return stats;
    }

    // Insertion sort by fee asc, then timestamp asc (stable)
    public static void insertionSortByFeeThenTimestamp(List<Transaction> list) {
        for (int i = 1; i < list.size(); i++) {
            Transaction key = list.get(i);
            int j = i - 1;
            while (j >= 0 && (list.get(j).fee > key.fee || (list.get(j).fee == key.fee && list.get(j).timestamp > key.timestamp))) {
                list.set(j+1, list.get(j));
                j--;
            }
            list.set(j+1, key);
        }
    }

    public static List<Transaction> flagHighFeeOutliers(List<Transaction> list, double threshold) {
        List<Transaction> out = new ArrayList<>();
        for (Transaction t : list) if (t.fee > threshold) out.add(t);
        return out;
    }

    public static void main(String[] args) {
        List<Transaction> txs = new ArrayList<>();
        txs.add(new Transaction("id1", 10.5, 10_000));
        txs.add(new Transaction("id2", 25.0, 9_800));
        txs.add(new Transaction("id3", 5.0, 10_150));

        // Bubble sort (small batches)
        List<Transaction> copy1 = new ArrayList<>(txs);
        Map<String,Integer> stats = bubbleSortByFee(copy1);
        System.out.println("Bubble sorted by fee: " + copy1 + " // passes: " + stats.get("passes") + ", swaps: " + stats.get("swaps"));

        // Insertion sort (fee + timestamp)
        List<Transaction> copy2 = new ArrayList<>(txs);
        insertionSortByFeeThenTimestamp(copy2);
        System.out.println("Insertion sorted by fee+timestamp: " + copy2);

        // Outliers
        System.out.println("High-fee outliers (>50): " + flagHighFeeOutliers(txs, 50.0));
    }
}
