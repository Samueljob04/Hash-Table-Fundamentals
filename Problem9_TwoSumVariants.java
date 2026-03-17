import java.util.*;

public class Problem9_TwoSumVariants {
    public static List<int[]> twoSum(int[] arr, int target) {
        Map<Integer,Integer> map = new HashMap<>();
        List<int[]> res = new ArrayList<>();
        for (int i=0;i<arr.length;i++) {
            int comp = target - arr[i];
            if (map.containsKey(comp)) res.add(new int[]{map.get(comp), i});
            map.put(arr[i], i);
        }
        return res;
    }

    public static List<int[]> twoSumWithinWindow(int[] arr, int[] times, int target, int windowSeconds) {
        Map<Integer,Integer> map = new HashMap<>();
        List<int[]> res = new ArrayList<>();
        for (int i=0;i<arr.length;i++) {
            int comp = target - arr[i];
            if (map.containsKey(comp) && (times[i] - times[map.get(comp)]) <= windowSeconds) res.add(new int[]{map.get(comp), i});
            map.put(arr[i], i);
        }
        return res;
    }

    // K-sum simple backtracking (not optimized for large N)
    public static List<List<Integer>> kSum(int[] arr, int k, int target) {
        List<List<Integer>> res = new ArrayList<>();
        Arrays.sort(arr);
        backtrack(arr, k, target, 0, new ArrayList<>(), res);
        return res;
    }

    private static void backtrack(int[] arr, int k, int target, int start, List<Integer> cur, List<List<Integer>> res) {
        if (k == 0) { if (target==0) res.add(new ArrayList<>(cur)); return; }
        for (int i=start;i<arr.length;i++) {
            cur.add(arr[i]);
            backtrack(arr, k-1, target-arr[i], i+1, cur, res);
            cur.remove(cur.size()-1);
        }
    }

    public static void main(String[] args) {
        int[] arr = {500,300,200};
        System.out.println(twoSum(arr,500));
        System.out.println(kSum(arr,3,1000));
    }
}
