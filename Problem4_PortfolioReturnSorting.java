import java.util.*;

public class Problem4_PortfolioReturnSorting {
    static class Asset { String id; double ret; double vol; Asset(String id,double ret,double vol){this.id=id;this.ret=ret;this.vol=vol;} public String toString(){return id+":"+ret+"%";} }

    public static void mergeSortByReturn(Asset[] arr) {
        if (arr.length<2) return;
        Asset[] tmp = new Asset[arr.length];
        mergeRec(arr,tmp,0,arr.length-1);
    }
    private static void mergeRec(Asset[] arr, Asset[] tmp, int l, int r) {
        if (l>=r) return; int m=(l+r)/2; mergeRec(arr,tmp,l,m); mergeRec(arr,tmp,m+1,r); mergeAssets(arr,tmp,l,m,r);
    }
    private static void mergeAssets(Asset[] arr, Asset[] tmp, int l, int m, int r) {
        int i=l,j=m+1,k=l; while (i<=m && j<=r) tmp[k++] = (arr[i].ret <= arr[j].ret) ? arr[i++] : arr[j++]; while (i<=m) tmp[k++]=arr[i++]; while (j<=r) tmp[k++]=arr[j++]; for (k=l;k<=r;k++) arr[k]=tmp[k];
    }

    public static void quickSortByReturnDescVolAsc(Asset[] arr) { quickRec(arr,0,arr.length-1); }
    private static void quickRec(Asset[] arr, int l, int r) {
        if (l>=r) return; int p = partition(arr,l,r); quickRec(arr,l,p-1); quickRec(arr,p+1,r);
    }
    private static int partition(Asset[] arr, int l, int r) {
        Asset pivot = arr[(l+r)/2];
        while (l<=r) {
            while (arr[l].ret < pivot.ret || (arr[l].ret==pivot.ret && arr[l].vol > pivot.vol)) l++;
            while (arr[r].ret > pivot.ret || (arr[r].ret==pivot.ret && arr[r].vol < pivot.vol)) r--;
            if (l<=r) { Asset t=arr[l]; arr[l]=arr[r]; arr[r]=t; l++; r--; }
        }
        return l-1;
    }

    public static void main(String[] args) {
        Asset[] a = { new Asset("AAPL",12,1.2), new Asset("TSLA",8,3.4), new Asset("GOOG",15,1.0) };
        mergeSortByReturn(a);
        System.out.println("Merge: " + Arrays.toString(a));
        quickSortByReturnDescVolAsc(a);
        System.out.println("Quick desc: " + Arrays.toString(a));
    }
}
