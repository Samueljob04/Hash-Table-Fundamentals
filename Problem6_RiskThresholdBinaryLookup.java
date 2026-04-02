import java.util.*;

public class Problem6_RiskThresholdBinaryLookup {
    public static int linearSearch(int[] arr, int target) {
        int comps=0;
        for (int i=0;i<arr.length;i++) { comps++; if (arr[i]==target) { System.out.println("Comparisons: " + comps); return i; } }
        System.out.println("Comparisons: " + comps); return -1;
    }

    public static int binaryFloor(int[] arr, int target) {
        int low=0, high=arr.length-1, ans=-1, comps=0;
        while (low<=high) {
            int mid=(low+high)/2; comps++;
            if (arr[mid] <= target) { ans=arr[mid]; low=mid+1; } else { high=mid-1; }
        }
        System.out.println("Comparisons: " + comps); return ans;
    }

    public static int binaryCeil(int[] arr, int target) {
        int low=0, high=arr.length-1, ans=-1, comps=0;
        while (low<=high) {
            int mid=(low+high)/2; comps++;
            if (arr[mid] >= target) { ans=arr[mid]; high=mid-1; } else { low=mid+1; }
        }
        System.out.println("Comparisons: " + comps); return ans;
    }

    public static void main(String[] args) {
        int[] arr = {10,25,50,100};
        System.out.println("Linear: " + linearSearch(arr,30));
        System.out.println("Floor: " + binaryFloor(arr,30));
        System.out.println("Ceil: " + binaryCeil(arr,30));
    }
}
