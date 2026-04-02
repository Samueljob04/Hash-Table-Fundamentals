import java.util.*;

public class Problem3_HistoricalTradeVolumeAnalysis {
    // Merge sort stable
    public static void mergeSort(long[] arr) {
        if (arr.length < 2) return;
        long[] tmp = new long[arr.length];
        mergeSortRec(arr, tmp, 0, arr.length-1);
    }
    private static void mergeSortRec(long[] arr, long[] tmp, int l, int r) {
        if (l>=r) return;
        int m = (l+r)/2;
        mergeSortRec(arr,tmp,l,m);
        mergeSortRec(arr,tmp,m+1,r);
        merge(arr,tmp,l,m,r);
    }
    private static void merge(long[] arr, long[] tmp, int l, int m, int r) {
        int i=l, j=m+1, k=l;
        while (i<=m && j<=r) tmp[k++] = (arr[i] <= arr[j]) ? arr[i++] : arr[j++];
        while (i<=m) tmp[k++] = arr[i++];
        while (j<=r) tmp[k++] = arr[j++];
        for (k=l;k<=r;k++) arr[k]=tmp[k];
    }

    // Quick sort desc
    public static void quickSortDesc(long[] arr) { quickSortRec(arr,0,arr.length-1); }
    private static void quickSortRec(long[] arr, int l, int r) {
        if (l>=r) return;
        int p = partition(arr,l,r);
        quickSortRec(arr,l,p-1);
        quickSortRec(arr,p+1,r);
    }
    private static int partition(long[] arr, int l, int r) {
        long pivot = arr[(l+r)/2];
        while (l<=r) {
            while (arr[l] > pivot) l++;
            while (arr[r] < pivot) r--;
            if (l<=r) { long t=arr[l]; arr[l]=arr[r]; arr[r]=t; l++; r--; }
        }
        return l-1;
    }

    public static long mergeTwoAndSum(long[] a, long[] b) {
        int i=0,j=0; long sum=0;
        while (i<a.length && j<b.length) {
            if (a[i] <= b[j]) { sum += a[i++]; } else { sum += b[j++]; }
        }
        while (i<a.length) sum += a[i++];
        while (j<b.length) sum += b[j++];
        return sum;
    }

    public static void main(String[] args) {
        long[] arr = {500,100,300};
        mergeSort(arr);
        System.out.println("MergeSort asc: " + Arrays.toString(arr));
        quickSortDesc(arr);
        System.out.println("QuickSort desc: " + Arrays.toString(arr));
        long[] morning = {100,200}; long[] afternoon = {150,250};
        System.out.println("Merged total: " + mergeTwoAndSum(morning, afternoon));
    }
}
