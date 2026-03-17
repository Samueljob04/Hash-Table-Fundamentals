import java.util.*;
import java.util.concurrent.*;

public class Problem3_DNSCacheWithTTL {
    static class DNSEntry {
        final String ip;
        final long expiry; // epoch ms
        DNSEntry(String ip, long expiry) { this.ip = ip; this.expiry = expiry; }
    }

    private final ConcurrentHashMap<String, DNSEntry> cache = new ConcurrentHashMap<>();
    private final ScheduledExecutorService cleaner = Executors.newSingleThreadScheduledExecutor();
    private final int capacity;

    public Problem3_DNSCacheWithTTL(int capacity) {
        this.capacity = capacity;
        cleaner.scheduleAtFixedRate(this::removeExpired, 1, 1, TimeUnit.SECONDS);
    }

    public String resolve(String domain, int ttlSeconds) {
        long now = System.currentTimeMillis();
        DNSEntry e = cache.get(domain);
        if (e != null && e.expiry > now) return e.ip; // HIT

        // MISS -> query upstream (simulated)
        String ip = queryUpstream(domain);
        put(domain, ip, ttlSeconds);
        return ip;
    }

    private String queryUpstream(String domain) {
        // simulate upstream DNS
        return "192.0.2." + Math.abs(domain.hashCode() % 254 + 1);
    }

    private void put(String domain, String ip, int ttlSeconds) {
        if (cache.size() >= capacity) evictLRU();
        cache.put(domain, new DNSEntry(ip, System.currentTimeMillis() + ttlSeconds * 1000L));
    }

    private void removeExpired() {
        long now = System.currentTimeMillis();
        for (Map.Entry<String, DNSEntry> ent : cache.entrySet()) {
            if (ent.getValue().expiry <= now) cache.remove(ent.getKey(), ent.getValue());
        }
    }

    // Simple LRU eviction using access timestamps
    private void evictLRU() {
        String lruKey = null;
        long oldest = Long.MAX_VALUE;
        for (Map.Entry<String, DNSEntry> ent : cache.entrySet()) {
            // expiry used as proxy; entries closer to expiry considered older
            if (ent.getValue().expiry < oldest) {
                oldest = ent.getValue().expiry;
                lruKey = ent.getKey();
            }
        }
        if (lruKey != null) cache.remove(lruKey);
    }

    public void shutdown() { cleaner.shutdownNow(); }

    public static void main(String[] args) throws Exception {
        Problem3_DNSCacheWithTTL dns = new Problem3_DNSCacheWithTTL(1000);
        System.out.println(dns.resolve("google.com", 3));
        Thread.sleep(2000);
        System.out.println(dns.resolve("google.com", 3));
        Thread.sleep(2000);
        System.out.println(dns.resolve("google.com", 3)); // likely refreshed
        dns.shutdown();
    }
}
