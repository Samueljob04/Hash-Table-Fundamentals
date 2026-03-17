import java.util.*;

public class Problem10_MultiLevelCache {
    // L1: access-order LinkedHashMap
    static class LRUCache<K,V> extends LinkedHashMap<K,V> {
        private final int capacity;
        LRUCache(int capacity) { super(capacity, 0.75f, true); this.capacity = capacity; }
        protected boolean removeEldestEntry(Map.Entry<K,V> eldest) { return size() > capacity; }
    }

    private final LRUCache<String, String> l1;
    private final Map<String, String> l2 = new HashMap<>();
    private final Map<String, String> l3 = new HashMap<>();
    private final Map<String, Integer> accessCount = new HashMap<>();

    public Problem10_MultiLevelCache() { l1 = new LRUCache<>(10000); }

    public String getVideo(String id) {
        if (l1.containsKey(id)) return l1.get(id);
        if (l2.containsKey(id)) {
            String v = l2.get(id); l1.put(id,v); return v;
        }
        // L3
        String v = l3.get(id);
        if (v != null) {
            l2.put(id,v);
            accessCount.put(id, accessCount.getOrDefault(id,0)+1);
        }
        return v;
    }

    public void putVideoL3(String id, String data) { l3.put(id,data); }

    public static void main(String[] args) {
        Problem10_MultiLevelCache c = new Problem10_MultiLevelCache();
        c.putVideoL3("video_123", "data123");
        System.out.println(c.getVideo("video_123"));
        System.out.println(c.getVideo("video_123"));
    }
}
