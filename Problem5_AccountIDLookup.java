import java.util.*;

public class Problem5_AccountIDLookup {
    public static int linearFirst(String[] arr, String target) {
        int comps=0;
        for (int i=0;i<arr.length;i++) { comps++; if (arr[i].equals(target)) { System.out.println("Comparisons: " + comps); return i; } }
        System.out.println("Comparisons: " + comps); return -1;
    }

    public static int binarySearch(String[] arr, String target) {
        int low=0, high=arr.length-1, comps=0;
        while (low<=high) {
            int mid=(low+high)/2; comps++;
            int cmp = arr[mid].compareTo(target);
            if (cmp==0) { System.out.println("Comparisons: " + comps); return mid; }
            if (cmp < 0) low=mid+1; else high=mid-1;
        }
        System.out.println("Comparisons: " + comps); return -1;
    }

    public static int countOccurrences(String[] arr, String target) {
        int idx = binarySearch(arr,target); if (idx==-1) return 0;
        int count=1; int i=idx-1; while (i>=0 && arr[i].equals(target)) { count++; i--; }
        i=idx+1; while (i<arr.length && arr[i].equals(target)) { count++; i++; }
        return count;
    }

    public static void main(String[] args) {
        String[] logs = {"accB","accA","accB","accC"};
        Arrays.sort(logs);
        System.out.println("Sorted logs: " + Arrays.toString(logs));
        System.out.println("Linear first accB: " + linearFirst(logs, "accB"));
        System.out.println("Binary accB: " + binarySearch(logs, "accB") + ", count=" + countOccurrences(logs, "accB"));
    }
}
