import java.util.*;
import java.util.concurrent.*;

public class Problem5_RealTimeAnalytics {
    private final ConcurrentHashMap<String, AtomicInteger> pageViews = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ConcurrentSkipListSet<String>> uniqueVisitors = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AtomicInteger> sources = new ConcurrentHashMap<>();

    public void processEvent(String url, String userId, String source) {
        pageViews.computeIfAbsent(url, k -> new AtomicInteger(0)).incrementAndGet();
        uniqueVisitors.computeIfAbsent(url, k -> new ConcurrentSkipListSet<>()).add(userId);
        sources.computeIfAbsent(source, k -> new AtomicInteger(0)).incrementAndGet();
    }

    public List<String> topPages(int k) {
        PriorityQueue<Map.Entry<String, AtomicInteger>> pq = new PriorityQueue<>((a,b)->Integer.compare(a.getValue().get(), b.getValue().get()));
        for (Map.Entry<String, AtomicInteger> e : pageViews.entrySet()) {
            pq.add(e);
            if (pq.size() > k) pq.poll();
        }
        List<String> res = new ArrayList<>();
        while (!pq.isEmpty()) {
            Map.Entry<String, AtomicInteger> e = pq.poll();
            res.add(e.getKey() + " - " + e.getValue().get() + " views (" + uniqueVisitors.getOrDefault(e.getKey(), new ConcurrentSkipListSet<>()).size() + " unique)");
        }
        Collections.reverse(res);
        return res;
    }

    public static void main(String[] args) {
        Problem5_RealTimeAnalytics a = new Problem5_RealTimeAnalytics();
        a.processEvent("/article/breaking-news", "user_123", "google");
        a.processEvent("/article/breaking-news", "user_456", "facebook");
        System.out.println(a.topPages(10));
    }
}
