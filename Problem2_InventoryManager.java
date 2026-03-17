import java.util.Map;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Problem2_InventoryManager {
    // productId -> stock count
    private final ConcurrentHashMap<String, AtomicInteger> stock = new ConcurrentHashMap<>();
    // productId -> waiting queue of userIds (FIFO)
    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<Long>> waitingLists = new ConcurrentHashMap<>();

    public void addProduct(String productId, int count) {
        stock.put(productId, new AtomicInteger(count));
        waitingLists.putIfAbsent(productId, new ConcurrentLinkedQueue<>());
    }

    public int checkStock(String productId) {
        AtomicInteger v = stock.get(productId);
        return v == null ? 0 : v.get();
    }

    // Attempts to purchase in O(1) amortized time, thread-safe
    public boolean purchaseItem(String productId, long userId) {
        AtomicInteger ai = stock.get(productId);
        if (ai == null) return false;
        while (true) {
            int current = ai.get();
            if (current <= 0) {
                // add to waiting list
                waitingLists.get(productId).add(userId);
                return false; // added to waitlist
            }
            if (ai.compareAndSet(current, current - 1)) {
                return true; // success
            }
            // otherwise retry (CAS loop)
        }
    }

    // When stock is restocked, process waiting list in FIFO order
    public void restock(String productId, int amount) {
        stock.putIfAbsent(productId, new AtomicInteger(0));
        waitingLists.putIfAbsent(productId, new ConcurrentLinkedQueue<>());
        AtomicInteger ai = stock.get(productId);
        ai.addAndGet(amount);
        // try to fulfill waiting list
        Queue<Long> q = waitingLists.get(productId);
        while (ai.get() > 0) {
            Long uid = q.poll();
            if (uid == null) break;
            if (purchaseItem(productId, uid)) {
                // fulfilled for uid
            }
        }
    }

    // sample usage
    public static void main(String[] args) throws InterruptedException {
        Problem2_InventoryManager manager = new Problem2_InventoryManager();
        manager.addProduct("IPHONE15_256GB", 100);

        // simulate concurrent purchases
        ExecutorService ex = Executors.newFixedThreadPool(50);
        CountDownLatch latch = new CountDownLatch(105);
        for (int i = 0; i < 105; i++) {
            final long userId = 1000 + i;
            ex.execute(() -> {
                boolean ok = manager.purchaseItem("IPHONE15_256GB", userId);
                System.out.println("user " + userId + " purchase: " + (ok ? "Success" : "Waiting"));
                latch.countDown();
            });
        }
        latch.await();
        ex.shutdown();

        System.out.println("Remaining stock: " + manager.checkStock("IPHONE15_256GB"));
    }
}
