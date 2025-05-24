package concurrent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class BackgroundExecutor {
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        t.setName("background-scheduler");
        return t;
    });

    public static ScheduledExecutorService getScheduler() {
        return scheduler;
    }

    public static void shutdown() {
        scheduler.shutdown();
    }
}
