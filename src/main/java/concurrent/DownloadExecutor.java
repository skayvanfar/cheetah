package concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DownloadExecutor {

    private static final int N_CPUS = Runtime.getRuntime().availableProcessors();

    // Customize this as needed
    private static final ExecutorService executor = new ThreadPoolExecutor(
            N_CPUS + 1,
            N_CPUS * 2,
            60L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(),
            new MyThreadFactory("DownloadPool"),
            new ThreadPoolExecutor.CallerRunsPolicy()
    );

    private DownloadExecutor() {
        // Utility class; no instantiation
    }

    public static ExecutorService getExecutor() {
        return executor;
    }

    public static void shutdown() {
        executor.shutdown();
    }
}

