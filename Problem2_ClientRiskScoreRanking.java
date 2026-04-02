import java.util.*;

public class Problem2_ClientRiskScoreRanking {
    static class Client { String id; int risk; double balance; Client(String id,int risk,double balance){this.id=id;this.risk=risk;this.balance=balance;} public String toString(){return id+":"+risk+":"+balance;} }

    public static int bubbleSortByRiskAsc(Client[] arr) {
        int n = arr.length; int swaps=0;
        for (int i=0;i<n-1;i++){
            for (int j=0;j<n-1-i;j++){
                if (arr[j].risk > arr[j+1].risk) { Client tmp=arr[j]; arr[j]=arr[j+1]; arr[j+1]=tmp; swaps++; }
            }
        }
        return swaps;
    }

    public static void insertionSortByRiskDescThenBalance(Client[] arr) {
        for (int i=1;i<arr.length;i++){
            Client key = arr[i]; int j = i-1;
            while (j>=0 && (arr[j].risk < key.risk || (arr[j].risk==key.risk && arr[j].balance < key.balance))) {
                arr[j+1]=arr[j]; j--; }
            arr[j+1]=key;
        }
    }

    public static List<Client> topKHighRisk(Client[] arr, int k) {
        List<Client> list = Arrays.asList(arr);
        list.sort((a,b)->Integer.compare(b.risk,a.risk));
        return list.subList(0, Math.min(k, list.size()));
    }

    public static void main(String[] args) {
        Client[] arr = { new Client("clientC",80,1000), new Client("clientA",20,5000), new Client("clientB",50,2000) };
        int swaps = bubbleSortByRiskAsc(arr);
        System.out.println("Bubble asc: " + Arrays.toString(arr) + " // swaps: " + swaps);
        insertionSortByRiskDescThenBalance(arr);
        System.out.println("Insertion desc: " + Arrays.toString(arr));
        System.out.println("Top risks: " + topKHighRisk(arr,3));
    }
}
